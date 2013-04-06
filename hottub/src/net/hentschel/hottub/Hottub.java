/*
 * Class: Hottub
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <tt>Hottub</tt> ...
 */
class Hottub
{

    private static final double DEFAULT_SETPOINT = 98;

    private static final String SETPOINT_FILENAME = "/tmp/tmpsetpoint";

    private static final double MINTEMP = 90;

    private static final double MAXTEMP = 104;

    private static final Temperature THRESHOLD = new Temperature(.75, Temperature.Unit.Fahrenheit);

    private static final long HEATER_ON_DELAY = 1 * 1000; // 5 second

    private static final long HEATER_OFF_DELAY = 1 * 1000; // 2 second

    private Application app;

    private Pump pump;

    private Heater heater;

    private Blower blower;

    private HTSubscriber pumpHandler;

    private HTSubscriber blowerHandler;

    private HTSubscriber tempHandler;

    private Temperature setpoint;

    private Thermometer thermometer;

    private HTSubscriber startHandler;

    private HTSubscriber heaterHandler;

    /**
     * Constructs a new <tt>Hottub</tt>.
     * @param application 
     */
    Hottub(Application app)
    {
        this.app = app;
        this.setpoint = new Temperature(readSetpointStorage(), Temperature.Unit.Fahrenheit);
        this.pumpHandler = new HTSubscriber()
        {
            public void eventReceived(HTEvent event)
            {
                switch (event)
                {
                case CallPumpOn:
                    Hottub.this.turnPumpOn();
                    break;
                case CallPumpOff:
                    Hottub.this.turnPumpOff();
                    break;
                case CallTogglePump:
                    Hottub.this.pumpToggle();
                    break;
                }
            }
        };

        this.blowerHandler = new HTSubscriber()
        {
            public void eventReceived(HTEvent event)
            {
                switch (event)
                {
                case CallBlowerOn:
                    Hottub.this.turnBlowerOn();
                    break;
                case CallBlowerOff:
                    Hottub.this.turnBlowerOff();
                    break;
                case CallToggleBlower:
                    Hottub.this.blowerToggle();
                    break;
                }
            }
        };

        this.tempHandler = new HTSubscriber()
        {
            public void eventReceived(HTEvent event)
            {
                switch (event)
                {
                case CallSetTemperatureSetpointUp:
                    Hottub.this.setTempSetpointUp();
                    break;
                case CallSetTemperatureSetpointDown:
                    Hottub.this.setTempSetpointDown();
                    break;
                case SetTemperatureSetpoint:
                    Hottub.this.setTempSetpoint(event);
                    break;
                case TemperatureUpdate:
                    Hottub.this.temperatureEval();
                    break;
                }
            }
        };

        this.startHandler = new HTSubscriber()
        {
            public void eventReceived(HTEvent event)
            {
                // set the initial set point, which also updates the screen (and the rest of the world)
                Hottub.this.setTempSetpoint(Hottub.this.setpoint);
            }
        };
        
        this.heaterHandler = new HTSubscriber()
        {
            
            public void eventReceived(HTEvent event)
            {
                switch (event)
                {
                case PumpOff:
                    Hottub.this.heater.scheduleOff(1);
                    break;
                }
            }
        };
    }

    private void blowerToggle()
    {
        if (this.blower.isOn())
        {
            this.blower.off();
        }
        else
        {
            this.blower.on();
        }
    }

    void init()
    {
        this.app.subscribe(HTEvent.CallPumpOn, this.pumpHandler);
        this.app.subscribe(HTEvent.CallPumpOff, this.pumpHandler);
        this.app.subscribe(HTEvent.CallTogglePump, this.pumpHandler);

        this.app.subscribe(HTEvent.CallBlowerOn, this.blowerHandler);
        this.app.subscribe(HTEvent.CallBlowerOff, this.blowerHandler);
        this.app.subscribe(HTEvent.CallToggleBlower, this.blowerHandler);

        this.app.subscribe(HTEvent.CallSetTemperatureSetpointUp, this.tempHandler);
        this.app.subscribe(HTEvent.CallSetTemperatureSetpointDown, this.tempHandler);
        this.app.subscribe(HTEvent.SetTemperatureSetpoint, this.tempHandler);
        this.app.subscribe(HTEvent.TemperatureUpdate, this.tempHandler);

        /** safety to turn off heater whenever the pump is off */
        this.app.subscribe(HTEvent.PumpOff, this.heaterHandler);
        
        this.app.subscribe(HTEvent.Start, this.startHandler);
    }

    private void pumpToggle()
    {
        if (this.pump.isOn())
        {
            this.turnPumpOff();
        }
        else
        {
            this.turnPumpOn();
        }
    }

    private double readSetpointStorage()
    {
        File file = new File(SETPOINT_FILENAME);
        if (!file.exists())
        {
            return DEFAULT_SETPOINT;
        }
        if (!file.canRead())
        {
            System.err.println("unable to open '" + SETPOINT_FILENAME + "' for read");
            return DEFAULT_SETPOINT;
        }

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            reader.close();
            return Double.parseDouble(line);
        }
        catch (Exception e)
        {
            System.err.println("unable to read '" + SETPOINT_FILENAME + "'");
            System.err.println(e.getMessage());
            return DEFAULT_SETPOINT;
        }
    }

    void set(Blower blower)
    {
        this.blower = blower;
    }

    void set(Heater heater)
    {
        this.heater = heater;
    }

    void set(Pump pump)
    {
        this.pump = pump;
    }

    void set(Thermometer thermo)
    {
        this.thermometer = thermo;
    }

    private void setTempSetpoint(HTEvent event)
    {
        this.setTempSetpoint((Temperature) event.getData());
    }

    private void setTempSetpoint(Temperature t)
    {
        double tval = t.getIn(Temperature.Unit.Fahrenheit);
        if (tval > MINTEMP && tval < MAXTEMP)
        {
            this.setpoint.set(t);
            this.writeSetpointStorage(t);
            HTEvent evt = HTEvent.SetpointUpdate;
            evt.setData(t);
            this.app.broadcast(evt);
            this.temperatureEval();
        }
    }

    private void setTempSetpointDown()
    {
        this.setTempSetpoint(Temperature.decrement(this.setpoint, Temperature.Unit.Fahrenheit));
    }

    private void setTempSetpointUp()
    {
        this.setTempSetpoint(Temperature.increment(this.setpoint, Temperature.Unit.Fahrenheit));
    }

    private void temperatureEval()
    {
        if(!this.pump.isOn())
        {
            this.heater.off();
            return;
        }
        
        double current = this.thermometer.getTemperature().getIn(Temperature.Unit.Fahrenheit);
        double set = this.setpoint.getIn(Temperature.Unit.Fahrenheit);
        double threshold = THRESHOLD.getIn(Temperature.Unit.Fahrenheit);

        if (current < (set - threshold))
        {
            this.heater.scheduleOn(HEATER_ON_DELAY);
            this.app.broadcast(HTEvent.CallHeaterOn);
        }

        if (current > (set + threshold))
        {
            this.heater.scheduleOff(HEATER_OFF_DELAY);
            this.app.broadcast(HTEvent.CallHeaterOff);
        }
    }

    private void turnBlowerOff()
    {
        if (this.blower.isOn())
        {
            this.blower.off();
        }
    }

    private void turnBlowerOn()
    {
        if (!this.blower.isOn())
        {
            this.blower.on();
        }
    }

    private void turnPumpOff()
    {
        if (this.pump.isOn())
        {
            this.pump.off();
        }
    }

    private void turnPumpOn()
    {
        if (!this.pump.isOn())
        {
            this.pump.on();
            
            // retrigger temp eval later (after pump state is settled) 
            new Timer().schedule(new TimerTask(){

                public void run()
                {
                    HTEvent evt = HTEvent.SetTemperatureSetpoint;
                    evt.setData(Hottub.this.setpoint);
                    Hottub.this.app.broadcast(evt);
                }}, 2000L);
        }
    }

    private void writeSetpointStorage(Temperature temp)
    {
        File file = new File(SETPOINT_FILENAME);
        if (file.exists())
        {
            file.delete();
        }

        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println(temp.getIn(Temperature.Unit.Fahrenheit));
            writer.close();
        }
        catch (Exception e)
        {
            System.err.println("unable to write '" + SETPOINT_FILENAME + "'");
            System.err.println(e.getMessage());
        }
    }
}
