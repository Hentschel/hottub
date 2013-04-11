/*
 * Class: Pump
 * 
 * Created on Mar 28, 2013
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
