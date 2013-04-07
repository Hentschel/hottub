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
    private boolean activeLow;

    /**
     * Constructs a new <tt>DigitalOutput</tt>.
     * @param gpio 
     */
    public GPIODigitalOutput(Pin gpio, boolean activeLow)
    {
        super(gpio);
        this.activeLow = activeLow;
    }

    public State getState()
    {
        if (this.pin.isHigh())
        {
            if(this.activeLow)
            {
                return State.Off;
            }
            else
            {
                return State.On;
            }
        }
        else if (this.pin.isLow())
        {
            if(this.activeLow)
            {
                return State.On;
            }
            else
            {
                return State.Off;
            }
        }
        return State.Unknown;
    }

    public void init()
    {
        final GpioController gpio = GpioFactory.getInstance();
        PinState state = this.activeLow ? PinState.HIGH : PinState.LOW;
        this.pin = gpio.provisionDigitalOutputPin(this.io, state);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalOutput#setOff()
     */
    public void setOff()
    {
        if(this.activeLow)
        {
            this.pin.high();
        }
        else
        {
            this.pin.low();
        }
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalOutput#setOn()
     */
    public void setOn()
    {
        if(this.activeLow)
        {
            this.pin.low();
        }
        else
        {
            this.pin.high();
        }
    }
}
