/*
 * Class: SimDigitalInput
 * 
 * Created on Mar 31, 2013
 * 
 */
package net.hentschel.hottub.io;

import com.pi4j.io.gpio.Pin;

/**
 * <tt>SimDigitalInput</tt> ...
 */
public class SimDigitalInput extends SimDigitalIO implements IDigitalInput
{

    private IOSubscriber subscriber = null;

    /**
     * Constructs a new <tt>SimDigitalInput</tt>.
     */
    public SimDigitalInput(Pin gpio)
    {
        super(gpio);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.SimDigitalIO#getState()
     */
    public State getState()
    {
        return Simulation.INSTANCE.getStateFor(this.io);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.SimDigitalIO#init()
     */
    public void init()
    {
        Simulation.INSTANCE.addInputMapping(this.io, this);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalInput#setIOSubscriber(net.hentschel.hottub.IOSubscriber)
     */
    public void setIOSubscriber(IOSubscriber sub)
    {
        this.subscriber = sub;
    }

    void simulateInputOff()
    {
        if (this.subscriber != null)
        {
            this.subscriber.update(this.getState());
        }
    }

    void simulateInputOn()
    {
        if (this.subscriber != null)
        {
            this.subscriber.update(this.getState());
        }
    }
}
