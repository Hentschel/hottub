/*
 * Class: Blower
 * 
 * Created on Mar 28, 2013
 * 
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
