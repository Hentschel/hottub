/*
 * Class: Pump
 * 
 * Created on Mar 28, 2013
 * 
 * (c) Copyright Novellus Systems, Inc., unpublished work, created 2003
 * All use, disclosure, and/or reproduction of this material is prohibited
 * unless authorized in writing.  All Rights Reserved.
 * Rights in this program belong to:
 * Novellus Systems, Inc.
 * 4000 N. First Street
 * San Jose, CA
 */
package net.hentschel.hottub;

import net.hentschel.hottub.io.IDigitalOutput;

/**
 * <tt>Pump</tt> ...
 */
public class Pump extends SingleOutputDevice
{
    public final static String ID = "pump";

    private IDigitalOutput out;

    /**
     * Constructs a new <tt>Pump</tt>.
     */
    Pump(Application app)
    {
        super(app);
    }

    protected IDigitalOutput getIOPoint()
    {
        return this.out;
    }

    protected void init()
    {
        this.out = (IDigitalOutput) this.app.getIOFor(Pump.ID);
        this.setOnEvent(HTEvent.PumpOn);
        this.setOffEvent(HTEvent.PumpOff);
    }
}
