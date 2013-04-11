/*
 * Class: I2CSensor
 * 
 * Created on Mar 31, 2013
 * 
 */
package net.hentschel.hottub.io;

import java.io.IOException;

import net.hentschel.hottub.Application;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

public interface I2CSensor
{

    public class Factory
    {
        public static I2CSensor getSensor(int bus, int device) throws IOException
        {
            if (Application.isSimulation(I2CSensor.class.getSimpleName()))
            {
                return new SimI2CSensor(bus, device);
            }
            I2CBus i2cBus = I2CFactory.getInstance(bus);
            return new HWI2CSensor(i2cBus.getDevice(device));
        }

    }

    /**
     * This method reads bytes directly from the i2c sensor to given buffer at asked offset. 
     * 
     * @param buffer buffer of data to be read from the i2c device in one go
     * @param offset offset in buffer 
     * @param size number of bytes to be read 
     * 
     * @return number of bytes read
     * 
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    int read(byte[] buffer, int offset, int size) throws IOException;
}
