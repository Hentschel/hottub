/*
 * Class: SimDigitalOutput
 * 
 * Created on Mar 31, 2013
 * 
 */
package net.hentschel.hottub.io;

import com.pi4j.io.gpio.Pin;

/**
 * <tt>SimDigitalOutput</tt> ...
 */
public class SimDigitalOutput extends SimDigitalIO implements IDigitalOutput
{

    /**
     * Constructs a new <tt>SimDigitalOutput</tt>.
     */
    public SimDigitalOutput(Pin gpio)
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
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalOutput#setOff()
     */
    public void setOff()
    {
        Simulation.INSTANCE.simulateSetOutputOff(this.io);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalOutput#setOn()
     */
    public void setOn()
    {
        Simulation.INSTANCE.simulateSetOutputOn(this.io);
    }
}
