/*
 * Class: Simulation
 * 
 * Created on Mar 31, 2013
 * 
 * (c) Copyright Novellus Systems, Inc., unpublished work, created 2003
 * All use, disclosure, and/or reproduction of this material is prohibited
 * unless authorized in writing.  All Rights Reserved.
 * Rights in this program belong to:
 * Novellus Systems, Inc.
 * 4000 N. First Street
 * San Jose, CA
 */
package net.hentschel.hottub.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import net.hentschel.hottub.Application;
import net.hentschel.hottub.Blower;
import net.hentschel.hottub.ButtonField;
import net.hentschel.hottub.Heater;
import net.hentschel.hottub.Pump;
import net.hentschel.hottub.Temperature;
import net.hentschel.hottub.io.IDigitalIO.State;

/**
 * <tt>Simulation</tt> ...
 */
class Simulation
{

    static final Simulation INSTANCE = new Simulation();

    private Temperature temperature;

    private Map<String, Pin> buttons = new HashMap<String, Pin>();
    private Map<Pin, String> relays = new HashMap<Pin, String>();
    private Map<Pin, IDigitalIO.State> inputstate = new HashMap<Pin, IDigitalIO.State>();
    private Map<Pin, IDigitalIO.State> outputstate = new HashMap<Pin, IDigitalIO.State>();
    private Map<Pin, SimDigitalInput> siminputs = new HashMap<Pin, SimDigitalInput>();

    private Pin heaterpin;

    private Runnable inputrunner;

    /**
     * Constructs a new <tt>Simulation</tt>.
     */
    private Simulation()
    {
        this.temperature = new Temperature(90.0, Temperature.Unit.Fahrenheit);

        buttons.put(ButtonField.LEFT, RaspiPin.GPIO_05);
        buttons.put(ButtonField.BOTTOM, RaspiPin.GPIO_04);
        buttons.put(ButtonField.CENTER, RaspiPin.GPIO_03);
        buttons.put(ButtonField.TOP, RaspiPin.GPIO_02);
        buttons.put(ButtonField.RIGHT, RaspiPin.GPIO_00);

        inputstate.put(RaspiPin.GPIO_00, IDigitalIO.State.Off);
        inputstate.put(RaspiPin.GPIO_02, IDigitalIO.State.Off);
        inputstate.put(RaspiPin.GPIO_03, IDigitalIO.State.Off);
        inputstate.put(RaspiPin.GPIO_04, IDigitalIO.State.Off);
        inputstate.put(RaspiPin.GPIO_05, IDigitalIO.State.Off);

        relays.put(RaspiPin.GPIO_01, ILCDScreen.BACKLITE);
        relays.put(RaspiPin.GPIO_07, Pump.ID);
        relays.put(RaspiPin.GPIO_15, Heater.ID);
        relays.put(RaspiPin.GPIO_16, Blower.ID);

        this.heaterpin = RaspiPin.GPIO_15;

        outputstate.put(RaspiPin.GPIO_01, IDigitalIO.State.Off);
        outputstate.put(RaspiPin.GPIO_06, IDigitalIO.State.Off);
        outputstate.put(RaspiPin.GPIO_07, IDigitalIO.State.Off);
        outputstate.put(RaspiPin.GPIO_08, IDigitalIO.State.Off);

        this.inputrunner = new Runnable()
        {

            public void run()
            {
                try
                {
                    while (true)
                    {
                        p(">");
                        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                        String input = in.readLine();
                        for (String button : buttons.keySet())
                        {
                            if (button.startsWith(input))
                            {
                                Pin pin = buttons.get(button);
                                SimDigitalInput sim = siminputs.get(pin);
                                if(sim != null)
                                {
                                    p("simulating keypress for " + button);
                                    inputstate.put(pin, IDigitalIO.State.On);
                                    sim.simulateInputOn();
                                    Thread.sleep(200);
                                    inputstate.put(pin, IDigitalIO.State.Off);
                                    sim.simulateInputOff();
                                    continue;
                                }
                            }
                        }
                    }
                }
                catch (IOException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        new Thread(this.inputrunner).start();
    }

    void createTemperatureSensor(int bus, int device)
    {
        this.p("created temp sensor at bus " + bus + ", device id " + device);
    }

    private Temperature getNewSimulatedTemperature()
    {
        Temperature.Unit unit = Temperature.Unit.Fahrenheit;
        if (this.heaterIsOn())
        {
            if(this.temperature.getIn(unit) < 105)
            {
                this.temperature = new Temperature(this.temperature.getIn(unit) + 0.1, unit);
            }
            return this.temperature;
        }
        else
        {
            if(this.temperature.getIn(unit) > 88)
            {
                this.temperature = new Temperature(this.temperature.getIn(unit) - 0.02, unit);
            }
            return this.temperature;
        }
    }

    State getStateFor(Pin io)
    {
        if (this.inputstate.containsKey(io))
        {
            return this.inputstate.get(io);
        }
        return this.outputstate.get(io);
    }

    private boolean heaterIsOn()
    {
        Map<String, IDigitalIO> map = Application.getIOMap();
        IDigitalOutput heater = null;
        for(IDigitalIO io : map.values())
        {
            Pin pin = io.getPin();
            if(pin.equals(RaspiPin.GPIO_01))
            {
                continue;
            }
            
            if(io instanceof IDigitalOutput)
            {
                p("new temp check: " + this.relays.get(pin) + " is " + io.getState());
                if(io.getPin().getAddress() == this.heaterpin.getAddress())
                {
                    heater = (IDigitalOutput)io;
                }
            }
        }
        return heater.getState().equals(IDigitalIO.State.On);
    }

    private synchronized void p(String string)
    {
        System.out.println("hot tub: " + string);
    }

    int readTemperatureSensor(byte[] buffer, int offset, int size)
    {
        Temperature t = this.getNewSimulatedTemperature();
        double tc = t.getIn(Temperature.Unit.Celcius);
        double dval = tc / 0.0625;
        int val = (int) dval;
        byte msb = (byte) (val >> 4);
        byte lsb = (byte) (val << 4);
        buffer[0] = msb;
        buffer[1] = lsb;
        return 2;
    }

    void simulateSetOutputOff(Pin io)
    {
        this.outputstate.put(io, IDigitalIO.State.Off);
        this.p("output for " + this.relays.get(io) + " turned off");
    }

    void simulateSetOutputOn(Pin io)
    {
        this.outputstate.put(io, IDigitalIO.State.On);
        this.p("output for " + this.relays.get(io) + " turned on");
    }

    void splashOutput()
    {
        System.out.println("*************************");
        System.out.println("*         SPLASH!       *");
        System.out.println("*************************");
    }

    void updateSetpoint(Temperature data)
    {
        this.p("display setpoint is at " + data);
    }

    void updateTemperature(Temperature data)
    {
        this.p("display temperature is at " + data);
    }

    void addInputMapping(Pin io, SimDigitalInput input)
    {
        this.siminputs.put(io, input);
    }
}
