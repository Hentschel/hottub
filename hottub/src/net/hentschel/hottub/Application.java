/*
 * Class: Application
 * 
 * Created on Mar 28, 2013
 * 
 * (c) Copyright Novellus Systems, Inc., unpublished work, created 2003
 * All use, disclosure, and/or reproduction of this material is prohibited
 * unless authorized in writing.  All Rights Reserved.
 * Rights in this program belong to:
 * Novellus Systems, Inc.
 * 4000 N. First Street
 * San Jose, CA
 */
package net.hentschel.hottub;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.hentschel.hottub.io.IDigitalIO;
import net.hentschel.hottub.io.IDigitalInput;
import net.hentschel.hottub.io.IDigitalOutput;
import net.hentschel.hottub.io.ILCDScreen;

import com.pi4j.io.gpio.RaspiPin;

/**
 * <tt>Application</tt> ...
 */
public class Application
{

    /**
         * <tt>State</tt> ...
         */
    private enum State {
        Init, Running, Ended
    }

    private static final Map<String, IDigitalIO> IOMAP = new HashMap<String, IDigitalIO>();

    // IO config
    static
    {
        // inputs (buttons)
        IOMAP.put(ButtonField.LEFT, IDigitalInput.Factory.create(RaspiPin.GPIO_05));    //schematic key 4
        IOMAP.put(ButtonField.BOTTOM, IDigitalInput.Factory.create(RaspiPin.GPIO_04));  //schematic key 0
        IOMAP.put(ButtonField.CENTER, IDigitalInput.Factory.create(RaspiPin.GPIO_03));  //schematic key 3
        IOMAP.put(ButtonField.TOP, IDigitalInput.Factory.create(RaspiPin.GPIO_02));     //schematic key 2
        IOMAP.put(ButtonField.RIGHT, IDigitalInput.Factory.create(RaspiPin.GPIO_00));   //schematic key 1

        // outputs (relays, screen backlite)
        IOMAP.put(ILCDScreen.BACKLITE, IDigitalOutput.Factory.create(RaspiPin.GPIO_01, false));
        IOMAP.put(Pump.ID, IDigitalOutput.Factory.create(RaspiPin.GPIO_07, true));
        IOMAP.put(Heater.ID, IDigitalOutput.Factory.create(RaspiPin.GPIO_15, true));
        IOMAP.put(Blower.ID, IDigitalOutput.Factory.create(RaspiPin.GPIO_16, true));
    }

    public static final Map<String, IDigitalIO> getIOMap()
    {
        return Collections.unmodifiableMap(IOMAP);
    }
    
    public static final boolean isSimulation(String type)
    {
        if (type.equalsIgnoreCase("ILCDScreen"))
        {
            return false;
        }
        if (type.equalsIgnoreCase("I2CSensor"))
        {
            return false;
        }
        if (type.equalsIgnoreCase("IDigitalIO"))
        {
            return false;
        }
        return false;
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Application app = new Application();
        app.init();
        app.run();
        app.shutdown();
    }

    private Map<HTEvent, Set<HTSubscriber>> subscriptions;

    private ILCDScreen lcd;

    private ButtonField buttons;

    private Hottub hottub;

    private BlockingQueue<HTEvent> eventqueue;

    private State state;

    /**
     * Constructs a new <tt>Application</tt>.
     */
    Application()
    {
        this.subscriptions = new HashMap<HTEvent, Set<HTSubscriber>>();
        this.eventqueue = new LinkedBlockingQueue<HTEvent>();
        this.state = State.Init;
    }

    synchronized void broadcast(HTEvent event)
    {
        if (!this.eventqueue.offer(event))
        {
            throw new RuntimeException("queue full!");
        }
    }

    public IDigitalIO getIOFor(String id)
    {
        return Application.IOMAP.get(id);
    }

    private void init()
    {
        this.lcd = ILCDScreen.Factory.create(this);

        for (IDigitalIO io : IOMAP.values())
        {
            io.init();
        }

        this.buttons = new ButtonField(this);
        this.buttons.init();

        this.lcd.splash();

        this.hottub = new Hottub(this);
        Heater heater = new Heater(this);
        final Pump pump = new Pump(this);
        Blower blower = new Blower(this);
        Thermometer thermo = new Thermometer(this);

        heater.setInterlock(new Interlock()
        {

            public boolean eval()
            {
                return pump.isOn();
            }
        });

        heater.init();
        pump.init();
        blower.init();
        thermo.init();
        this.lcd.init();

        this.hottub.set(pump);
        this.hottub.set(heater);
        this.hottub.set(blower);
        this.hottub.set(thermo);

        this.hottub.init();
    }

    public void run()
    {
        this.broadcast(HTEvent.Start);

        try
        {
            while (true)
            {
                HTEvent event = this.eventqueue.take();
                // wait for start event, if not yet started throw events away
                if (event.equals(HTEvent.Start))
                {
                    this.state = State.Running;
                }
                // call handlers while running
                if (this.state.equals(State.Running))
                {
                    Set<HTSubscriber> subs = this.subscriptions.get(event);
                    if (subs != null)
                    {
                        for (HTSubscriber sub : subs)
                        {
                            sub.eventReceived(event);
                        }
                    }
                }
                // if stop received, we're done
                if (event.equals(HTEvent.Stop))
                {
                    return;
                }
            }
        }
        catch (InterruptedException e)
        {
            // signal to end queue handling
        }
        finally
        {
            // always set state to ended when leaving this method
            this.state = State.Ended;
        }
    }

    private void shutdown()
    {
        // TODO Auto-generated method stub

    }

    public synchronized void subscribe(HTEvent event, HTSubscriber subscriber)
    {
        Set<HTSubscriber> subs = this.subscriptions.get(event);
        if (subs == null)
        {
            subs = new HashSet<HTSubscriber>();
            this.subscriptions.put(event, subs);
        }
        subs.add(subscriber);

    }

    synchronized void unsubscribe(HTEvent event, HTSubscriber subscriber)
    {
        Set<HTSubscriber> subs = this.subscriptions.get(event);
        if (subs == null)
        {
            return;
        }
        subs.remove(subscriber);
        if (subs.isEmpty())
        {
            this.subscriptions.remove(event);
        }
    }

    void debug(String string)
    {
        System.out.println("HT: " + string);
    }
}
