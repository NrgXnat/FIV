// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVCanvas.java

import ij.*;
import ij.gui.ImageCanvas;
import ij.process.ImageProcessor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.PrintStream;

public class FIVCanvas extends ImageCanvas
{

    public FIVCanvas(FIVImage image)
    {
        super(image.getBaseImagePlus());
        crosshairX = -1;
        crosshairY = -1;
        this.image = image;
        buildColorModel();
        removeKeyListener(IJ.getInstance());
        imageUpdated = true;
        setBackground(Color.black);
    }

    public void print(Graphics g, double s)
    {
        System.out.println("FIVCanvas: print");
    }

    private void buildColorModel()
    {
        fivColorModel = new FIVColorModel();
        fivColorModel.setAlpha(image.getAlpha());
        String mode = PropReader.getString("mode");
        String colormap = PropReader.getString("mode." + mode + ".func.colormap");
        if(colormap == null)
            fivColorModel.makeDefaultLUT();
        else
        if(colormap.equalsIgnoreCase("default"))
            fivColorModel.makeDefaultLUT();
        else
        if(colormap.equalsIgnoreCase("flip"))
            fivColorModel.makeFlipLUT();
        else
            fivColorModel.makeDefaultLUT();
    }

    public void setColorModel(int val, double min, double max)
    {
        ImagePlus fimp = image.getFunctionalImagePlus();
        ImagePlus bimp = image.getBaseImagePlus();
        java.awt.image.ColorModel cm;
        if(val == 1)
            cm = fivColorModel.getPos();
        else
            cm = fivColorModel.getNeg();
        if(fimp != null)
        {
            ImageProcessor ip = fimp.getProcessor();
            ip.setColorModel(cm);
            ip.setMinAndMax(min, max);
            if(fimp.getStackSize() > 1)
                fimp.getStack().setColorModel(cm);
            fimp.setSlice(bimp.getCurrentSlice());
            fimp.updateImage();
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        super.mouseMoved(e);
        Point mp = e.getPoint();
        if(image != null)
        {
            image.setCoordsFromWindow(offScreenX(mp.x), offScreenY(mp.y));
            image.getManager();
            if(FIVManager.isContinuousUpdate())
                image.getManager().syncToMouseMotion(image);
        }
    }

    public void mouseEntered(MouseEvent e)
    {
        super.mouseEntered(e);
    }

    public void mousePressed(MouseEvent e)
    {
        int mod = e.getModifiers();
        if((mod & 8) == 0)
            super.mousePressed(e);
        Point p = e.getPoint();
        startX = p.x;
        startY = p.y;
        xhandled = 0;
        yhandled = 0;
        if(FIVManager.isContrastAdjustable())
        {
            image.printIntensityRange();
            image.showPopup();
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        Point p = e.getPoint();
        super.mouseClicked(e);
        image.setCoordsFromWindow(offScreenX(p.x), offScreenY(p.y));
        image.getManager().syncToMouseClick(image);
    }

    public void mouseReleased(MouseEvent e)
    {
        if(FIVManager.isContrastAdjustable())
        {
            image.logIntensityRange();
            image.printIntensityRange();
            image.showPopup();
        }
        super.mouseReleased(e);
    }

    public void mouseDragged(MouseEvent e)
    {
        int flags = e.getModifiers();
        ImagePlus simp = image.getStructuralImagePlus();
        if(simp != null)
        {
            ImageProcessor ip = simp.getProcessor();
            Point p = e.getPoint();
            if(FIVManager.isContrastAdjustable())
            {
                int xdist = p.x - startX;
                int xincr = xdist - xhandled;
                xhandled = xdist;
                int ydist = p.y - startY;
                int yincr = ydist - yhandled;
                yhandled = ydist;
                image.adjustBrightness(0.0025000000000000001D * (double)xincr);
                image.adjustContrast(0.0050000000000000001D * (double)yincr);
                image.getManager().syncToIntensity(ip.getMin(), ip.getMax());
            } else
            {
                super.mouseDragged(e);
            }
        } else
        {
            super.mouseDragged(e);
        }
    }

    void setCrosshairPosition(int x, int y)
    {
        crosshairX = x;
        crosshairY = y;
    }

    public boolean isFocusTraversable()
    {
        return true;
    }

    public void save()
    {
    }

    FIVImage image;
    int startX;
    int startY;
    int xhandled;
    int yhandled;
    private boolean syncFlag;
    int crosshairX;
    int crosshairY;
    Image buffer;
    Graphics bufferGraphics;
    FIVColorModel fivColorModel;
}
