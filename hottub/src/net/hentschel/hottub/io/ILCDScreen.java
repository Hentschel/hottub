/*
 * Class: ILCDScreen
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