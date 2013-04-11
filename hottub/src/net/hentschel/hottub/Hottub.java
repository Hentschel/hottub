/*
 * Class: Hottub
 * 
 * Created on Mar 28, 2013
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
public class Hottub implements IHottub
{

    /** default setpoint on first boot, w/o stored temp */
    private static final double DEFAULT_SETPOINT = 98;

    /** the filename for temp setpoint storage */
    private static final String SETPOINT_FILENAME = "/tmp/tmpsetpoint";

    /** selectable min temp */
    private static double MINTEMP = 75;

    /** selectable max temp */
    private static double MAXTEMP = 104;

    /** hysteresis for heater on/off */
    private static Temperature HYSTERESIS = new Temperature(.5, Temperature.Unit.Fahrenheit);

    /** delay before turning heater on */
    private static long HEATER_ON_DELAY = 15 * 1000; // 18 second

    /** delay before turning heater off */
    private static long HEATER_OFF_DELAY = 6 * 1000; // 6 second

    private Application app;

    private Blower blower;

    private Heater heater;

    private HTSubscriber pumpHandler;

    private HTSubscriber blowerHandler;

    private HTSubscriber tempHandler;

    private HTSubscriber startHandler;

    private HTSubscriber heaterHandler;

    private Pump pump;

    private Temperature setpoint;

    private Thermometer thermometer;

    /**
     * Constructs a new <tt>Hottub</tt>.
     * @param application 
     */
    Hottub(Application app)
    {
        this.app = app;

        /** parse overrides from command line/property def's */
        MINTEMP = Double.parseDouble(System.getProperty("net.hentschel.hottub.mintemp", "75"));
        MAXTEMP = Double.parseDouble(System.getProperty("net.hentschel.hottub.maxtemp", "105"));
        HYSTERESIS = new Temperature(
            Double.parseDouble(System.getProperty("net.hentschel.hottub.hysteresis", "0.5")),
            Temperature.Unit.Fahrenheit);
        HEATER_ON_DELAY = Long.parseLong(System.getProperty("net.hentschel.hottub.heater-on-delay", "15")) * 1000;
        HEATER_OFF_DELAY = Long.parseLong(System.getProperty("net.hentschel.hottub.heater-off-delay", "6")) * 1000;

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
                // set the initial set point/temp, which also updates the screen (and the rest of the world)
                Hottub.this.setTempSetpoint(Hottub.this.setpoint);
                Temperature temp = Hottub.this.thermometer.getTemperature();
                HTEvent evt = HTEvent.TemperatureUpdate;
                evt.setData(temp);
                Hottub.this.app.broadcast(evt);
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

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#getCurrentTemperature()
     */
    @Override
    public final Temperature getCurrentTemperature()
    {
        return this.thermometer.getTemperature();
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#getSetpointTemperature()
     */
    @Override
    public Temperature getSetpointTemperature()
    {
        return this.setpoint;
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

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#isBlowerOn()
     */
    @Override
    public boolean isBlowerOn()
    {
        return this.blower.isOn();
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#isHeaterOn()
     */
    @Override
    public boolean isHeaterOn()
    {
        return this.heater.isOn();
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#isPumpOn()
     */
    @Override
    public boolean isPumpOn()
    {
        return this.pump.isOn();
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
    
    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#setTempSetpoint(net.hentschel.hottub.Temperature)
     */
    @Override
    public void setTempSetpoint(Temperature t)
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
        if (!this.pump.isOn())
        {
            this.heater.off();
            return;
        }

        double current = this.thermometer.getTemperature().getIn(Temperature.Unit.Fahrenheit);
        double set = this.setpoint.getIn(Temperature.Unit.Fahrenheit);
        double hysteresis = HYSTERESIS.getIn(Temperature.Unit.Fahrenheit);

        if (current < (set - hysteresis) && !this.heater.isScheduledOn())
        {
            this.app.debug("current [" + current + "] < (set [" + set + "] - hysteresis [" + hysteresis
                    + "]) ==> ask heater on in " + HEATER_ON_DELAY + " - heater is [" + (this.heater.isOn() ? "ON" : "OFF") + "]");
            this.heater.scheduleOn(HEATER_ON_DELAY);
            this.app.broadcast(HTEvent.CallHeaterOn);
        }

        if (current > (set + hysteresis) && !this.heater.isScheduledOff())
        {
            this.app.debug("current [" + current + "] > (set [" + set + "] + hysteresis [" + hysteresis
                    + "]) ==> ask heater off in " + HEATER_OFF_DELAY + " - heater is [" + (this.heater.isOn() ? "ON" : "OFF") + "]");
            this.heater.scheduleOff(HEATER_OFF_DELAY);
            this.app.broadcast(HTEvent.CallHeaterOff);
        }
    }

    public String toString()
    {
        String result = "";
        result += "Pump  : [" + (this.pump.isOn() ? "ON" : "OFF") + "]\r\n";
        result += "Heater: [" + (this.heater.isOn() ? "ON" : "OFF") + "]\r\n";
        result += "Blower: [" + (this.blower.isOn() ? "ON" : "OFF") + "]\r\n";
        result += "Temp  : [" + (this.thermometer.getTemperature()) + "]\r\n";
        return result;
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#turnBlowerOff()
     */
    @Override
    public void turnBlowerOff()
    {
        if (this.blower.isOn())
        {
            this.blower.off();
        }
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#turnBlowerOn()
     */
    @Override
    public void turnBlowerOn()
    {
        if (!this.blower.isOn())
        {
            this.blower.on();
        }
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#turnPumpOff()
     */
    @Override
    public void turnPumpOff()
    {
        if (this.pump.isOn())
        {
            this.pump.off();
        }
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IHeater#turnPumpOn()
     */
    @Override
    public void turnPumpOn()
    {
        if (!this.pump.isOn())
        {
            this.pump.on();

            // retrigger temp eval later (after pump state is settled) 
            new Timer().schedule(new TimerTask()
            {

                public void run()
                {
                    HTEvent evt = HTEvent.SetTemperatureSetpoint;
                    evt.setData(Hottub.this.setpoint);
                    Hottub.this.app.broadcast(evt);
                }
            }, 2000L);
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
