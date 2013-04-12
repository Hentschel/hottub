/*
 * Class: NativeLCDLibrary
 * 
 * Created on Mar 27, 2013
 */
package net.hentschel.est.lcd.jni;
/**
 * JNA Wrapper for library<br>
 * 
 * this is he JNI wrapper for the native LCD code provided by EST.<br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class NativeLCDLibrary {
    
    public static final String JNI_LIBRARY_NAME ="estlcd";
    
    private static class Lib {
        static boolean init() {
            System.loadLibrary(JNI_LIBRARY_NAME);
            return true;
        }
    }
    private static boolean init = Lib.init();
 
    public static NativeLCDLibrary INSTANCE = new NativeLCDLibrary();
    
    public static final int LCD_WIDTH = (int)128;
    public static final int LCD_X_OFFSET = (int)4;
    public static final int LCD_HEIGHT = (int)64;
    /**
     * Original signature : <code>void LCD_ClearScreen()</code><br>
     * <i>native declaration : line 125</i>
     */
    public native void LCD_ClearScreen();
    /**
     * Original signature : <code>void LCD_SetPenColor(uint8)</code><br>
     * <i>native declaration : line 127</i>
     */
    public native void LCD_SetPenColor(byte c);
    /**
     * Original signature : <code>void LCD_SetFillColor(int8)</code><br>
     * <i>native declaration : line 129</i>
     */
    public native void LCD_SetFillColor(byte c);
    /**
     * Original signature : <code>void LCD_SetFont(uint8)</code><br>
     * <i>native declaration : line 131</i>
     */
    public native void LCD_SetFont(byte f);

    public native void LCD_SetOrientation(byte o);
    /**
     * Original signature : <code>void LCD_SetContrast(uint8)</code><br>
     * <i>native declaration : line 133</i>
     */
    public native void LCD_SetContrast(byte contrast);
    /**
     * Original signature : <code>void LCD_PutPixel(uint8, uint8, uint8)</code><br>
     * <i>native declaration : line 137</i>
     */
    public native void LCD_PutPixel(byte x, byte y, byte color);
    /**
     * Original signature : <code>void LCD_DrawLine(uint8, uint8, uint8, uint8)</code><br>
     * <i>native declaration : line 139</i>
     */
    public native void LCD_DrawLine(byte x0, byte y0, byte x1, byte y1);
    /**
     * Original signature : <code>void LCD_DrawCircle(uint8, uint8, uint8)</code><br>
     * <i>native declaration : line 141</i>
     */
    public native void LCD_DrawCircle(byte x0, byte y0, byte radius);
    /**
     * Original signature : <code>void LCD_DrawEllipse(int, int, int, int)</code><br>
     * <i>native declaration : line 143</i>
     */
    public native void LCD_DrawEllipse(int xm, int ym, int a, int b);
    /**
     * Original signature : <code>void LCD_DrawRect(uint8, uint8, uint8, uint8, uint8)</code><br>
     * <i>native declaration : line 145</i>
     */
    public native void LCD_DrawRect(byte x0, byte y0, byte x1, byte y1, byte line);

    /**
     * Original signature : <code>void LCD_PrintXY(uint8, uint8, char*)</code><br>
     * <i>native declaration : line 147</i>
     */
    public native void LCD_PrintXY(byte x, byte y, String s);

    /**
     * Original signature : <code>void LCD_DrawBitmap(uint8, uint8, const uint8*)</code><br>
     * <i>native declaration : line 149</i>
     */
    public native void LCD_DrawBitmap(byte x0, byte y0, byte bmp[]);
    /**
     * Original signature : <code>void LCD_Init()</code><br>
     * <i>native declaration : line 153</i>
     */
    public native void LCD_Init();
    /**
     * Original signature : <code>void LCD_WriteFramebuffer()</code><br>
     * <i>native declaration : line 155</i>
     */
    public native void LCD_WriteFramebuffer();

}
