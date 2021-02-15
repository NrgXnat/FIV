// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVStackCanvas.java

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import java.awt.*;
import java.io.PrintStream;

public class FIVStackCanvas extends FIVCanvas
{

    public FIVStackCanvas(FIVImage image)
    {
        super(image);
        Rectangle b = image.getBoundingBox();
        double scale = image.getScale();
        super.setDrawingSize((int)((double)b.width * scale), (int)((double)b.height * scale));
        setSize((int)((double)b.width * scale), (int)((double)b.height * scale));
        validate();
    }

    public void print(Graphics g, double scale)
    {
        System.out.println("FIVStackCanvas: print");
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
            Rectangle bounds = image.getBoundingBox();
            buffer = createImage((int)((double)bounds.width * scale), (int)((double)bounds.height * scale));
            bufferGraphics = buffer.getGraphics();
        }
        bufferGraphics.setColor(Color.black);
        bufferGraphics.fillRect(0, 0, 500, 500);
        try
        {
            drawStructural(bufferGraphics, scale);
            drawFunctional(bufferGraphics, scale);
            bufferGraphics.setColor(new Color(50, 200, 25));
            bufferGraphics.drawString(s, getBounds().width - 15, getBounds().height - 5);
            if(FIVManager.isCrosshairVisible())
            {
                Rectangle r = super.getSrcRect();
                bufferGraphics.setColor(new Color(100, 255, 50));
                double space = 0.0D;
                bufferGraphics.drawLine(0, crosshairY, crosshairX - (int)space, crosshairY);
                bufferGraphics.drawLine(crosshairX + (int)space, crosshairY, (int)((double)r.width * scale), crosshairY);
                bufferGraphics.drawLine(crosshairX, 0, crosshairX, crosshairY - (int)space);
                bufferGraphics.drawLine(crosshairX, crosshairY + (int)space, crosshairX, (int)(scale * (double)r.height));
            }
            Roi roi = image.getBaseImagePlus().getRoi();
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
        ImagePlus bimp = image.getBaseImagePlus();
        if(simp == null)
            return;
        Rectangle bounds = image.getBoundingBox();
        simp.updateImage();
        Image img = simp.getImage();
        if(img != null)
        {
            Point dst2 = new Point((int)((double)bounds.width * scale), (int)((double)bounds.height * scale));
            Point src1 = new Point(bounds.x, bounds.y);
            Point src2 = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
            g.drawImage(img, 0, 0, dst2.x, dst2.y, src1.x, src1.y, src2.x, src2.y, null);
        }
    }

    public void drawFunctional(Graphics g, double scale)
    {
        ImagePlus fimp = image.getFunctionalImagePlus();
        int sign = image.getActivationDisplay();
        if(fimp == null)
            return;
        imageUpdated = false;
        fimp.updateImage();
        Rectangle bounds = image.getBoundingBox();
        try
        {
            if(sign == 1 || sign == 3)
            {
                setColorModel(1, image.getMinThreshold(), image.getMaxThreshold());
                Image img = fimp.getImage();
                if(img != null)
                {
                    Point dst2 = new Point((int)((double)bounds.width * scale), (int)((double)bounds.height * scale));
                    Point src1 = new Point(bounds.x, bounds.y);
                    Point src2 = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
                    g.drawImage(img, 0, 0, dst2.x, dst2.y, src1.x, src1.y, src2.x, src2.y, null);
                } else
                {
                    System.out.println("Functional Image is null.");
                }
            }
            if(sign == 2 || sign == 3)
            {
                setColorModel(2, -image.getMaxThreshold(), -image.getMinThreshold());
                Image img = fimp.getImage();
                if(img != null)
                {
                    Point dst2 = new Point((int)((double)bounds.width * scale), (int)((double)bounds.height * scale));
                    Point src1 = new Point(bounds.x, bounds.y);
                    Point src2 = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
                    g.drawImage(img, 0, 0, dst2.x, dst2.y, src1.x, src1.y, src2.x, src2.y, null);
                } else
                {
                    System.out.println("Functional Image is null.");
                }
            }
        }
        catch(OutOfMemoryError e)
        {
            IJ.outOfMemory("Paint");
        }
    }
}
