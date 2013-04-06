/*
 * Class: Box
 * 
 * Created on Mar 27, 2013
 * 
 */
package net.hentschel.est.lcd;

public class Box
{
    public final Point origin;
    public final Dimension dimension;

    public Box(Point origin, Dimension dim)
    {
        this.origin = origin;
        this.dimension = dim;
    }
}
