/*
 * Class: TempSensor
 * 
 * Created on Mar 28, 2013
 * 
 * (c) Copyright Novellus Systems, Inc., unpublished work, created 2003
 * All use, disclosure, and/or reproduction of this material is prohibited
 * unless authorized in writing.  All Rights Reserved.
 * Rights in this program belong to:
 * Novellus Systems, Inc.
 * 4000 N. First Street
 * San Jose, CA
 */
package net.hentschel.est.lcd.test;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class TempSensor
{
    public static void main(String[] args) throws Exception
    {
        I2CBus i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);

        for (int i = 0; i < 1000; i++)
        {
            I2CDevice sensor = i2cBus.getDevice(0x48);
            byte[] bufferInside = new byte[2];

            int n = sensor.read(bufferInside, 0, 2);
            System.out.println("IN (" + n +"): " + temperature(bufferInside));
            Thread.sleep(10000);
            sensor = null;
        }
    }

    public static double temperature(byte[] tempBuffer)
    {
        int msb = tempBuffer[0] < 0 ? 256 + tempBuffer[0] : tempBuffer[0];
        int lsb = tempBuffer[1] < 0 ? 256 + tempBuffer[1] : tempBuffer[1];

        msb <<= 4;
        lsb >>= 4;

        int result = msb | lsb;
        return result * 0.0625;
    }
}