/*
 * Class: DigitalInput
 * 
 * Created on Mar 28, 2013
 * 
 */
package net.hentschel.hottub.io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * <tt>DigitalInput</tt> ...
 */
class GPIODigitalInput extends GPIODigitalIO implements IDigitalInput
{
    private GpioPinDigitalInput pin;

    private GpioPinListenerDigital listener;

    private IOSubscriber subscriber;

    /**
     * Constructs a new <tt>DigitalOutput</tt>.
     * @param gpio 
     */
    public GPIODigitalInput(Pin gpio)
    {
        super(gpio);

        this.listener = new GpioPinListenerDigital()
        {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event)
            {
                synchronized (GPIODigitalInput.this)
                {
                    if (GPIODigitalInput.this.subscriber != null)
                    {
                        if(event.getState().isHigh())
                        {
                            GPIODigitalInput.this.subscriber.update(IDigitalIO.State.On);
                        }
                        if(event.getState().isLow())
                        {
                            GPIODigitalInput.this.subscriber.update(IDigitalIO.State.Off);
                        }
                    }
                }
            }
        };
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
        this.pin = gpio.provisionDigitalInputPin(this.io, PinPullResistance.PULL_DOWN);
        this.pin.addListener(this.listener);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalInput#setIOSubscriber(net.hentschel.hottub.IOSubscriber)
     */
    public synchronized void setIOSubscriber(IOSubscriber sub)
    {
        this.subscriber = sub;
    }
}
