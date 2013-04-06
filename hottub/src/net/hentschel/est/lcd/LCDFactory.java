/*
 * Class: LCDFactory
 * 
 * Created on Mar 27, 2013
 * 
 * (c) Copyright Novellus Systems, Inc., unpublished work, created 2003
 * All use, disclosure, and/or reproduction of this material is prohibited
 * unless authorized in writing.  All Rights Reserved.
 * Rights in this program belong to:
 * Novellus Systems, Inc.
 * 4000 N. First Street
 * San Jose, CA
 */
package net.hentschel.est.lcd;

import net.hentschel.est.lcd.jni.NativeLCDLibrary;

/**
 * <tt>LCDFactory</tt> ...
 */
class LCDFactory
{
    private static LCD INSTANCE = null;

    static synchronized LCD instance()
    {
        if(INSTANCE == null) {
            INSTANCE = new LCDImp();
        }
        return INSTANCE;
    }

    /**
     * <tt>LCDImp</tt> ...
     */
    public static class LCDImp implements LCD
    {

        private static final int MAX_CONTRAST = 64;
        private byte line;

        /**
         * Constructs a new <tt>LCDImp</tt>.
         */
        private LCDImp()
        {
            NativeLCDLibrary.INSTANCE.LCD_Init();
            this.setContrast(80);
            this.setFillColor(Color.Black);
            this.setPenColor(Color.Black);
            this.setLineWidth((byte)1);
            this.setOrientation(Orientation.O0);
            this.setFont(Font.Terminal6x8);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#clear()
         */
        public void clear()
        {
            NativeLCDLibrary.INSTANCE.LCD_ClearScreen();
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawCircle(net.hentschel.est.lcd.Point, int)
         */
        public void drawCircle(Point center, int radius)
        {
            this.drawCircle(center.x, center.y, radius);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawCircle(int, int, int)
         */
        public void drawCircle(int x, int y, int radius)
        {
            NativeLCDLibrary.INSTANCE.LCD_DrawCircle((byte)x, (byte)y, (byte)radius);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawEllipse(net.hentschel.est.lcd.Point, int, int)
         */
        public void drawEllipse(Point center, int a, int b)
        {
            this.drawEllipse(center.x, center.y, a, b);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawEllipse(int, int, int, int)
         */
        public void drawEllipse(int x, int y, int a, int b)
        {
            NativeLCDLibrary.INSTANCE.LCD_DrawEllipse(x, y, a, b);        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawLine(net.hentschel.est.lcd.Point, net.hentschel.est.lcd.Point)
         */
        public void drawLine(Point start, Point end)
        {
            this.drawLine(start.x, start.y, end.x, end.y);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawLine(int, int, int, int)
         */
        public void drawLine(int sx, int sy, int ex, int ey)
        {
            NativeLCDLibrary.INSTANCE.LCD_DrawLine((byte)sx, (byte)sy, (byte)ex, (byte)ey);        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawRect(net.hentschel.est.lcd.Box)
         */
        public void drawRect(Box box)
        {
            this.drawRect(box.origin, box.dimension);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawRect(net.hentschel.est.lcd.Point, net.hentschel.est.lcd.Dimension)
         */
        public void drawRect(Point origin, Dimension dim)
        {
            this.drawRect(origin.x, origin.y, origin.x + dim.x, origin.y + dim.y);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawRect(net.hentschel.est.lcd.Point, net.hentschel.est.lcd.Point)
         */
        public void drawRect(Point origin, Point end)
        {
            this.drawRect(origin.x, origin.y, end.x, end.y);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#drawRect(int, int, int, int)
         */
        public void drawRect(int sx, int sy, int ex, int ey)
        {
            NativeLCDLibrary.INSTANCE.LCD_DrawRect((byte)sx, (byte)sy, (byte)ex, (byte)ey, this.line);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#putPixel(net.hentschel.est.lcd.Point, net.hentschel.est.lcd.LCD.Color)
         */
        public void putPixel(Point point, Color color)
        {
            this.putPixel(point.x, point.y, color);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#putPixel(int, int, net.hentschel.est.lcd.LCD.Color)
         */
        public void putPixel(int x, int y, Color color)
        {
            NativeLCDLibrary.INSTANCE.LCD_PutPixel((byte)x, (byte)y, (byte) color.getID());
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#flush()
         */
        public void flush()
        {
            NativeLCDLibrary.INSTANCE.LCD_WriteFramebuffer();
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#setFont(net.hentschel.est.lcd.LCD.Font)
         */
        public void setFont(Font font)
        {
            NativeLCDLibrary.INSTANCE.LCD_SetFont((byte)font.getID());
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#setContrast(int)
         */
        public void setContrast(int level)
        {
            byte contrast = (byte) (level/100 * MAX_CONTRAST);
            NativeLCDLibrary.INSTANCE.LCD_SetContrast(contrast);
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#setFillColor(net.hentschel.est.lcd.LCD.Color)
         */
        public void setFillColor(Color color)
        {
            NativeLCDLibrary.INSTANCE.LCD_SetFillColor((byte)color.getID());
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#setFillColor(net.hentschel.est.lcd.LCD.Color)
         */
        public void setLineWidth(byte width)
        {
            this.line = width;
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#setOrientation(net.hentschel.est.lcd.LCD.Orientation)
         */
        public void setOrientation(Orientation o)
        {
            NativeLCDLibrary.INSTANCE.LCD_SetOrientation((byte)o.getID());
        }

        /* (non-Javadoc)
         * @see net.hentschel.est.lcd.LCD#setPenColor(net.hentschel.est.lcd.LCD.Color)
         */
        public void setPenColor(Color color)
        {
            NativeLCDLibrary.INSTANCE.LCD_SetPenColor((byte)color.getID());
        }

        public void print(Point origin, String text)
        {
            this.print(origin.x, origin.y, text);
        }

        public void print(int x, int y, String text)
        {
            NativeLCDLibrary.INSTANCE.LCD_PrintXY((byte)x, (byte)y, text);
        }
    }
}

