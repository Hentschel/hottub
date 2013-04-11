/*
 * Class: GPIODigitalIO
 * 
 * Created on Mar 30, 2013
 * 
 */
package net.hentschel.hottub.io;

import com.pi4j.io.gpio.Pin;

/**
 * <tt>SimDigitalIO</tt> ...
 */
abstract public class SimDigitalIO implements IDigitalIO
{

    protected final Pin io;

    /**
     * Constructs a new <tt>GPIODigitalIO</tt>.
     */
    public SimDigitalIO(Pin gpio)
    {
        this.io = gpio;
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalIO#getState()
     */
    public abstract State getState();

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalIO#init()
     */
    public abstract void init();

    /* (non-Javadoc)
     * @see net.hentschel.hottub.IDigitalIO#isOn()
     */
    public final boolean isOn()
    {
        return State.On.equals(this.getState());
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.io.IDigitalIO#getPin()
     */
    public Pin getPin()
    {
        return this.io;
    }
}
