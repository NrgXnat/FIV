// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MapCanvas.java

import java.awt.*;
import java.awt.image.*;

public class MapCanvas extends Canvas
{

    public MapCanvas()
    {
        reds = new byte[256];
        greens = new byte[256];
        blues = new byte[256];
        setBackground(Color.white);
        orientation = 2;
        makeLUT();
        imgPos = null;
        imgNeg = null;
        rangeMin = 5D;
        rangeMax = 12D;
        sign = 3;
    }

    private void makeLUT()
    {
        int n = 32;
        int redPos[] = {
            150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 
            250, 255, 255, 255, 255, 255, 255, 255, 255, 255, 
            255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 
            255, 255
        };
        int grPos[] = {
            0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 
            80, 88, 96, 104, 112, 120, 128, 136, 144, 152, 
            160, 168, 176, 184, 192, 200, 208, 216, 224, 232, 
            240, 248, 255
        };
        int blPos[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 25, 50, 75, 100, 125, 150, 
            175, 200
        };
        for(int i = 0; i < redPos.length; i++)
        {
            reds[i] = (byte)redPos[i];
            greens[i] = (byte)grPos[i];
            blues[i] = (byte)blPos[i];
        }

        interpolate(n);
        cmPos = new IndexColorModel(8, 256, reds, greens, blues);
        int redNeg[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 25, 50, 75, 100, 125, 150, 
            175, 200
        };
        int grNeg[] = {
            0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 
            80, 88, 96, 104, 112, 120, 128, 136, 144, 152, 
            160, 175, 185, 195, 205, 215, 225, 235, 245, 255, 
            255, 255, 255
        };
        int blNeg[] = {
            150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 
            250, 255, 255, 255, 255, 255, 250, 240, 230, 220, 
            210, 200, 190, 180, 170, 160, 150, 140, 130, 120, 
            110, 100
        };
        for(int i = 0; i < redNeg.length; i++)
        {
            reds[i] = (byte)redNeg[i];
            greens[i] = (byte)grNeg[i];
            blues[i] = (byte)blNeg[i];
        }

        interpolate(n);
        cmNeg = new IndexColorModel(8, 256, reds, greens, blues);
    }

    void interpolate(int nColors)
    {
        byte r[] = new byte[nColors];
        byte g[] = new byte[nColors];
        byte b[] = new byte[nColors];
        System.arraycopy(reds, 0, r, 0, nColors);
        System.arraycopy(greens, 0, g, 0, nColors);
        System.arraycopy(blues, 0, b, 0, nColors);
        double scale = (double)nColors / 256D;
        for(int i = 0; i < 256; i++)
        {
            int i1 = (int)((double)i * scale);
            int i2 = i1 + 1;
            if(i2 == nColors)
                i2 = nColors - 1;
            double fraction = (double)i * scale - (double)i1;
            reds[i] = (byte)(int)((1.0D - fraction) * (double)(r[i1] & 0xff) + fraction * (double)(r[i2] & 0xff));
            greens[i] = (byte)(int)((1.0D - fraction) * (double)(g[i1] & 0xff) + fraction * (double)(g[i2] & 0xff));
            blues[i] = (byte)(int)((1.0D - fraction) * (double)(b[i1] & 0xff) + fraction * (double)(b[i2] & 0xff));
        }

    }

    public void setOrientation(int o)
    {
        if(o != 1 && o != 2)
        {
            return;
        } else
        {
            orientation = o;
            return;
        }
    }

    public void makeMap()
    {
        int w;
        if(sign == 3)
            w = getBounds().width / 2;
        else
            w = getBounds().width;
        int h = getBounds().height;
        if(h < 0 || w < 0)
        {
            FIVToolWindow.appendLog(1, "MapCanvas.makeMap: Bad dimensions: " + w + ", " + h);
            FIVToolWindow.appendLog(1, "MapCanvas.makeMap: Parent: " + getParent());
            FIVToolWindow.appendLog(1, "MapCanvas.makeMap: Parent Parent: " + getParent().getParent());
            FIVToolWindow.appendLog(1, "MapCanvas.makeMap: Parent Parent Parent: " + getParent().getParent().getParent());
            return;
        }
        byte pixels[] = new byte[w * h];
        if(pixels == null)
            return;
        for(int i = 0; i < h; i++)
        {
            for(int j = 0; j < w; j++)
                if(orientation == 2)
                    pixels[i * w + j] = (byte)(int)((double)(h - i - 1) * (256D / (double)h));
                else
                    pixels[i * w + j] = (byte)(int)((double)j * (256D / (double)w));

        }

        java.awt.image.ImageProducer p = new MemoryImageSource(w, h, cmPos, pixels, 0, w);
        imgPos = Toolkit.getDefaultToolkit().createImage(p);
        p = new MemoryImageSource(w, h, cmNeg, pixels, 0, w);
        imgNeg = Toolkit.getDefaultToolkit().createImage(p);
    }

    public void setRange(double min, double max, int sign)
    {
        rangeMin = min;
        rangeMax = max;
        if(sign != this.sign)
        {
            this.sign = sign;
            imgPos = null;
            imgNeg = null;
        }
        repaint();
    }

    public void print(Graphics g, double scale)
    {
        if(imgPos == null && imgNeg == null)
            makeMap();
        setBackground(Color.black);
        g.clearRect(0, 0, getBounds().width, getBounds().height);
        Rectangle bounds = g.getClipBounds();
        g.clearRect(0, 0, bounds.width, bounds.height);
        if(sign == 3 || sign == 1)
            g.drawImage(imgPos, 0, 0, bounds.width, bounds.height, 0, 0, imgPos.getWidth(null), imgPos.getHeight(null), null);
        if(sign == 3)
            g.drawImage(imgNeg, bounds.width / 2, 0, bounds.width, bounds.height, 0, 0, imgNeg.getWidth(null), imgNeg.getHeight(null), null);
        else
        if(sign == 2)
            g.drawImage(imgNeg, 0, 0, bounds.width, bounds.height, 0, 0, imgPos.getWidth(null), imgPos.getHeight(null), null);
        int x = bounds.x + bounds.width / 2;
        g.setFont(new Font("SansSerif", 0, 10));
        g.setColor(Color.black);
        double p = rangeMax;
        g.drawString(Double.toString(p), x - 10, 10);
        g.setColor(Color.white);
        p = rangeMin;
        g.drawString(Double.toString(p), x - 10, bounds.height - 2);
    }

    public void paint(Graphics g)
    {
        if(imgPos == null && imgNeg == null)
            makeMap();
        setBackground(Color.black);
        g.clearRect(0, 0, getBounds().width, getBounds().height);
        if((sign == 3 || sign == 1) && imgPos != null)
            g.drawImage(imgPos, 0, 0, null);
        if(sign == 3)
        {
            if(imgNeg != null)
                g.drawImage(imgNeg, getBounds().width / 2, 0, null);
        } else
        if(sign == 2 && imgNeg != null)
            g.drawImage(imgNeg, 0, 0, null);
        int x = getBounds().width / 2;
        g.setFont(new Font("SansSerif", 0, 10));
        g.setColor(Color.black);
        double p = rangeMax;
        g.drawString(Double.toString(p), x - 10, 10);
        g.setColor(Color.white);
        p = rangeMin;
        g.drawString(Double.toString(p), x - 10, getBounds().height - 2);
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    byte reds[];
    byte greens[];
    byte blues[];
    ColorModel cmPos;
    Image imgPos;
    ColorModel cmNeg;
    Image imgNeg;
    int orientation;
    int sign;
    double rangeMin;
    double rangeMax;

}
