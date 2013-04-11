/*
 * Class: ILCDScreen
 * 
 * Created on Mar 31, 2013
 * 
 */
package net.hentschel.hottub.io;

import net.hentschel.hottub.Application;

public interface ILCDScreen
{
    public class Factory
    {
        public static ILCDScreen create(Application app)
        {
            if (Application.isSimulation(ILCDScreen.class.getSimpleName()))
            {
                return new SimLCDScreen(app);
            }
            return new HWLCDScreen(app);
        }
    }

    public static final String BACKLITE = "backlite";

    public void init();

    public void splash();

}