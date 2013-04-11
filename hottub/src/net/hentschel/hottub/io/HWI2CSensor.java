/*
 * Class: HWI2CSensor
 * 
 * Created on Mar 31, 2013
 * 
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
