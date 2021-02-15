// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVMontageCanvas.java

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import java.awt.*;
import java.io.PrintStream;

public class FIVMontageCanvas extends FIVCanvas
{

    public FIVMontageCanvas(FIVImage image)
    {
        super(image);
        super.setDrawingSize(image.getBaseImagePlus().getWidth(), image.getBaseImagePlus().getHeight());
    }

    public void print(Graphics g, double scale)
    {
        System.out.println("FIVMontageCanvas: print");
        String s;
        if(image.getView() == 2)
            s = "";
        else
        if(image.getRadiologic())
            s = "L";
        else
            s = "R";
        Rectangle bounds = g.getClipBounds();
        Image printBuffer = createImage(bounds.width, bounds.height);
        Graphics printBufferGraphics = printBuffer.getGraphics();
        try
        {
            ImagePlus imp = image.getStructuralImagePlus();
            if(imp != null)
            {
                imp.updateImage();
                Image img = imp.getImage();
                if(img == null)
                {
                    System.out.println("Warning: the AWT image is null");
                    return;
                }
                if(img != null)
                    printBufferGraphics.drawImage(img, 0, 0, bounds.width, bounds.height, 0, 0, img.getWidth(null), img.getHeight(null), null);
            }
            imp = image.getFunctionalImagePlus();
            if(imp != null)
            {
                int sign = image.getActivationDisplay();
                imp.updateImage();
                if(sign == 1 || sign == 3)
                {
                    setColorModel(1, image.getMinThreshold(), image.getMaxThreshold());
                    Image img = imp.getImage();
                    if(img != null)
                        printBufferGraphics.drawImage(img, 0, 0, bounds.width, bounds.height, 0, 0, img.getWidth(null), img.getHeight(null), null);
                    else
                        System.out.println("Functional Image is null.");
                }
                if(sign == 2 || sign == 3)
                {
                    setColorModel(2, -image.getMaxThreshold(), -image.getMinThreshold());
                    Image img = imp.getImage();
                    if(img != null)
                        printBufferGraphics.drawImage(img, 0, 0, bounds.width, bounds.height, 0, 0, img.getWidth(null), img.getHeight(null), null);
                }
            }
            printBufferGraphics.setColor(new Color(50, 200, 25));
            printBufferGraphics.drawString(s, bounds.width - 20, bounds.height - 10);
            g.drawImage(printBuffer, 0, 0, this);
        }
        catch(OutOfMemoryError e)
        {
            System.out.println("FIVMontageCanvas: print: out of memory error.");
        }
    }

    public void paint(Graphics g)
    {
        double scale = image.getScale();
        String s;
        if(image.getView() == 2)
            s = "";
        else
        if(image.getRadiologic())
            s = "L";
        else
            s = "R";
        if(bufferGraphics == null)
        {
            buffer = createImage(image.getBaseImagePlus().getWidth(), image.getBaseImagePlus().getHeight());
            bufferGraphics = buffer.getGraphics();
        }
        try
        {
            drawStructural(bufferGraphics, scale);
            drawFunctional(bufferGraphics, scale);
            imageUpdated = false;
            bufferGraphics.setColor(new Color(50, 200, 25));
            bufferGraphics.drawString(s, getBounds().width - 15, getBounds().height - 5);
            if(FIVManager.isCrosshairVisible())
            {
                bufferGraphics.setColor(new Color(120, 120, 120));
                int increment = 10;
                for(int i = 0; i <= getBounds().width; i += increment * 2)
                {
                    int start = i;
                    int end = i + increment;
                    bufferGraphics.drawLine(start, crosshairY, end, crosshairY);
                }

                for(int i = 0; i <= getBounds().height; i += increment * 2)
                {
                    int start = i;
                    int end = i + increment;
                    bufferGraphics.drawLine(crosshairX, start, crosshairX, end);
                }

            }
            Roi roi = imp.getRoi();
            if(roi != null)
                roi.draw(bufferGraphics);
            g.drawImage(buffer, 0, 0, this);
        }
        catch(OutOfMemoryError e)
        {
            IJ.outOfMemory("Paint");
        }
    }

    public void drawStructural(Graphics g, double scale)
    {
        ImagePlus simp = image.getStructuralImagePlus();
        if(simp == null)
            return;
        if(imageUpdated)
            simp.updateImage();
        Image img = simp.getImage();
        if(img != null)
            g.drawImage(img, 0, 0, null);
    }

    public void drawFunctional(Graphics g, double scale)
    {
        ImagePlus fimp = image.getFunctionalImagePlus();
        if(fimp == null)
            return;
        int sign = image.getActivationDisplay();
        try
        {
            if(sign == 1 || sign == 3)
            {
                setColorModel(1, image.getMinThreshold(), image.getMaxThreshold());
                Image img = fimp.getImage();
                if(img != null)
                    g.drawImage(img, 0, 0, null);
            }
            if(sign == 2 || sign == 3)
            {
                setColorModel(2, -image.getMaxThreshold(), -image.getMinThreshold());
                Image img = fimp.getImage();
                if(img != null)
                    g.drawImage(img, 0, 0, null);
            }
        }
        catch(OutOfMemoryError e)
        {
            IJ.outOfMemory("Paint");
        }
    }
}
