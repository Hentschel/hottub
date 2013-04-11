/*
 * Class: SimI2CSensor
 * 
 * Created on Mar 31, 2013
 * 
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
