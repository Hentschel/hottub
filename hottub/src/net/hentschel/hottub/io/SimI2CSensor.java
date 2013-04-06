/*
 * Class: SimI2CSensor
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

import java.io.IOException;

/**
 * <tt>SimI2CSensor</tt> ...
 */
public class SimI2CSensor implements I2CSensor
{

    /**
     * 
     * Constructs a new <tt>SimI2CSensor</tt>.
     */
    SimI2CSensor(int bus, int device)
    {
        Simulation.INSTANCE.createTemperatureSensor(bus, device);
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.io.I2CSensor#read(byte[], int, int)
     */
    public int read(byte[] buffer, int offset, int size) throws IOException
    {
        return Simulation.INSTANCE.readTemperatureSensor(buffer, offset, size);
    }

}
