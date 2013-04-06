/*
 * Class: Blower
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
 * <tt>Blower</tt> ...
 */
public class Blower extends SingleOutputDevice
{

    public static final String ID = "blower";

    private IDigitalOutput out;

    /**
     * Constructs a new <tt>Blower</tt>.
     */
    Blower(Application app)
    {
        super(app);
    }

    protected IDigitalOutput getIOPoint()
    {
        return out;
    }

    protected void init()
    {
        this.out = (IDigitalOutput) this.app.getIOFor(Blower.ID);
        this.setOnEvent(HTEvent.BlowerOn);
        this.setOffEvent(HTEvent.BlowerOff);
    }
}
