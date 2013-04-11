/*
 * Class: Thermometer
 * 
 * Created on Mar 28, 2013
 * 
 */
package net.hentschel.hottub;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import net.hentschel.hottub.io.I2CSensor;

import com.pi4j.io.i2c.I2CBus;

/**
 * <tt>Thermometer</tt> ...
 */
class Thermometer
{

    private static final double TEMPERATURE_THRESHOLD = 0.2; //Fahrenheit

    private static final long UPDATE_PERIOD = 1000;

    private static double temperature(byte[] tempBuffer)
    {
        int msb = tempBuffer[0] < 0 ? 256 + tempBuffer[0] : tempBuffer[0];
        int lsb = tempBuffer[1] < 0 ? 256 + tempBuffer[1] : tempBuffer[1];

        msb <<= 4;
        lsb >>= 4;

        int result = msb | lsb;
        return result * 0.0625;
    }

    private Application app;

    private Temperature temperature;

    private I2CSensor sensor;

    private TimerTask updater;

    private Timer timer;

    /**
     * Constructs a new <tt>Thermometer</tt>.
     * @param app 
     */
    Thermometer(Application app)
    {
        this.app = app;
        this.temperature = new Temperature(0.0, Temperature.Unit.Fahrenheit);
        this.timer = new Timer();
    }

    Temperature getTemperature()
    {
        return new Temperature(this.temperature);
    }

    void init()
    {
        try
        {
            this.sensor = I2CSensor.Factory.getSensor(I2CBus.BUS_1, 0x48);
            this.sensorRead();
            this.updater = new TimerTask()
            {

                public void run()
                {
                    Thermometer.this.sensorRead();
                }
            };
            this.timer.scheduleAtFixedRate(this.updater, UPDATE_PERIOD, UPDATE_PERIOD);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void sensorRead()
    {
        try
        {
            byte[] buffer = new byte[2];
            int n = sensor.read(buffer, 0, 2);
            if (n > 1)
            {
                //check threshold
                double rval = Temperature.convertToFahrenheit(temperature(buffer), Temperature.Unit.Celcius);
                double cval = this.temperature.getIn(Temperature.Unit.Fahrenheit);

                if (Math.abs(rval - cval) >= TEMPERATURE_THRESHOLD)
                {
                    Temperature nt = new Temperature(rval, Temperature.Unit.Fahrenheit);
                    this.temperature.set(nt);
                    HTEvent evt = HTEvent.TemperatureUpdate;
                    evt.setData(nt);
                    this.app.broadcast(evt);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
