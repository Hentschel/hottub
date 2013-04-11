/*
 * Class: SimLCDScreen
 * 
 * Created on Mar 31, 2013
 * 
 */
package net.hentschel.hottub.io;

import net.hentschel.hottub.Application;
import net.hentschel.hottub.Temperature;

/**
 * <tt>SimLCDScreen</tt> ...
 */
public class SimLCDScreen extends AbstractLCDScreen
{

    /**
     * Constructs a new <tt>SimLCDScreen</tt>.
     * @param app 
     */
    SimLCDScreen(Application app)
    {
        super(app);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.io.ILCDScreen#splash()
     */
    public void splash()
    {
        Simulation.INSTANCE.splashOutput();
    }

    protected void updateSetpoint(Temperature data)
    {
        Simulation.INSTANCE.updateSetpoint(data);
    }

    protected void updateTemperature(Temperature data)
    {
        Simulation.INSTANCE.updateTemperature(data);
    }

    protected void updateHeater(boolean b)
    {
        // TODO Auto-generated method stub
        
    }

    protected void updatePump(boolean b)
    {
        // TODO Auto-generated method stub
        
    }

    protected void updateBlower(boolean b)
    {
        // TODO Auto-generated method stub
        
    }
}
