/*
 * Class: Heater
 * 
 * Created on Mar 28, 2013
 * 
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
