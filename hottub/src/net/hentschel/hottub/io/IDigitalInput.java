/*
 * Class: IDigitalInput
 * 
 * Created on Mar 30, 2013
 * 
 */
package net.hentschel.hottub.io;

import net.hentschel.hottub.Application;

import com.pi4j.io.gpio.Pin;

public interface IDigitalInput extends IDigitalIO
{
    public class Factory
    {
        public static IDigitalInput create(Pin pin)
        {
            if (Application.isSimulation(IDigitalIO.class.getSimpleName()))
            {
                return new SimDigitalInput(pin);
            }
            return new GPIODigitalInput(pin);
        }
    }

    public void setIOSubscriber(IOSubscriber sub);

}