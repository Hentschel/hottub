/*
 * Class: LCD
 * 
 * Created on Mar 27, 2013
 */
package net.hentschel.est.lcd;

public interface LCD
{
    public final static LCD INSTANCE = LCDFactory.instance();
    
    public enum Font 
    {
        FixedSys8x15(2),
        Lucida10x16(3),
        Terminal12x16(1),
        Terminal6x8(0);
        
        private int id;

        private Font(int id) 
        {
            this.id = id;
        }
        
        int getID() {
            return this.id;
        }
    }
    
    public enum Color 
    {
        Transparent(-1),
        White(0),
        Black(1);
        
        private int id;

        Color(int id)
        {
            this.id = id;
        }
        
        int getID() {
            return this.id;
        }
    }
    
    public enum Orientation {
        O0(0),
        O90(1),
        O180(2),
        O270(3);
        
        private int id;

        Orientation(int id) {
            this.id = id;
        }

        int getID() {
            return this.id;
        }
    }

    public void clear();
    public void drawCircle(Point center, int radius);
    public void drawCircle(int x, int y, int radius);
    
    public void drawEllipse(Point center, int a, int b);
    public void drawEllipse(int x, int y, int a, int b);
    
    public void drawLine(Point start, Point end);
    public void drawLine(int sx, int sy, int ex, int ey);
    
    public void drawRect(Box box);
    public void drawRect(Point origin, Dimension dim);
    public void drawRect(Point origin, Point end);
    public void drawRect(int sx, int sy, int ex, int ey);
    
    public void print(Point origin, String text);
    public void print(int x, int y, String text);
    
    public void putPixel(Point point, Color color);
    public void putPixel(int x, int y, Color color);
    
    public void flush();
    
    public void setFont(Font font);
    public void setContrast(int level);
    public void setFillColor(Color color);
    
    /**
     * sets new orientation
     * @param o
     */
    public void setOrientation(Orientation o);
    public void setPenColor(Color color);
}
