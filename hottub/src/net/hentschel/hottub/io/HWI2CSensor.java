/*
 * Class: HWI2CSensor
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

import com.pi4j.io.i2c.I2CDevice;

/**
 * <tt>HWI2CSensor</tt> ...
 */
public class HWI2CSensor implements I2CSensor
{

    private I2CDevice sensor;

    /**
     * Constructs a new <tt>HWI2CSensor</tt>.
     * @param sensor 
     */
    public HWI2CSensor(I2CDevice sensor)
    {
        this.sensor = sensor;
    }

    /* (non-Javadoc)
     * @see net.hentschel.hottub.io.I2CSensor#read(byte[], int, int)
     */
    public int read(byte[] buffer, int offset, int size) throws IOException
    {
        return this.sensor.read(buffer, offset, size);
    }
}
