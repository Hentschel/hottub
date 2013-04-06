/*
 * Class: Heater
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
 * <tt>Heater</tt> ...
 */
public class Heater extends SingleOutputDevice
{
    public final static String ID = "heater";

    private IDigitalOutput out;

    /**
     * Constructs a new <tt>Heater</tt>.
     * @param app 
     */
    Heater(Application app)
    {
        super(app);
    }

    protected IDigitalOutput getIOPoint()
    {
        return this.out;
    }

    protected void init()
    {
        this.out = (IDigitalOutput) this.app.getIOFor(Heater.ID);
        this.setDelay(1000);
        this.setOnEvent(HTEvent.HeaterOn);
        this.setOffEvent(HTEvent.HeaterOff);
    }
}
