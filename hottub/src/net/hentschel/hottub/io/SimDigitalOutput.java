/*
 * Class: SimDigitalOutput
 * 
 * Created on Mar 31, 2013
 * 
 * (c) Copyright Novellus Systems, Inc., unpublished work, created 2003
 * All use, disclosure, and/or reproduction of this material is prohibited
 * unless authorized in writing.  All Rights Reserved.
 * Rights in this program belong to:
 * Novellus Systems, Inc.
 * 4000 N. First Street
 * San Jose, CA
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
