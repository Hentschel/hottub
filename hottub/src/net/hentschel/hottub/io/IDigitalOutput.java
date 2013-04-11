/*
 * Class: IDigitalOutput
 * 
 * Created on Mar 30, 2013
 * 
 */
package net.hentschel.hottub.io;

import net.hentschel.hottub.Application;

import com.pi4j.io.gpio.Pin;

public interface IDigitalOutput extends IDigitalIO
{
    public class Factory
    {
        public static IDigitalOutput create(Pin pin, boolean activeLow)
        {
            if (Application.isSimulation(IDigitalIO.class.getSimpleName()))
            {
                return new SimDigitalOutput(pin);
            }
            return new GPIODigitalOutput(pin, activeLow);
        }
    }

    public void setOff();

    public void setOn();

}