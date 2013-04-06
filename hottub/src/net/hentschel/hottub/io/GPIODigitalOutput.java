/*
 * Class: DigitalOutput
 * 
 * Created on Mar 28, 2013
 * 
 */
package net.hentschel.hottub.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

/**
 * <tt>DigitalOutput</tt> ...
 */
class GPIODigitalOutput extends GPIODigitalIO implements IDigitalOutput
{
    private GpioPinDigitalOutput pin;

    /**
     * Constructs a new <tt>DigitalOutput</tt>.
     * @param gpio 
     */
    public GPIODigitalOutput(Pin gpio)
    {
        super(gpio);
    }

    public State getState()
    {
        if (this.pin.isHigh())
        {
            return State.On;
        }
        else if (this.pin.isLow())
        {
            return State.Off;

        }
        return State.Unknown;
    }

    public void init()
    {
        final GpioController gpio = GpioFactory.getInstance();
        this.pin = gpio.provisionDigitalOutputPin(this.io, PinState.LOW);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalOutput#setOff()
     */
    public void setOff()
    {
        this.pin.low();
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalOutput#setOn()
     */
    public void setOn()
    {
        this.pin.high();
    }
}
