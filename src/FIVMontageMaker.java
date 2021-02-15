// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVMontageMaker.java

import ij.*;
import ij.process.ImageProcessor;

public class FIVMontageMaker
{

    public FIVMontageMaker()
    {
    }

    public void labelsOn(boolean on)
    {
        labels = on;
    }

    public void bordersOn(boolean on)
    {
        borders = on;
    }

    public ImagePlus makeMontage(ImagePlus imp, int columns, int rows, double scale, int first, int last, 
            int inc)
    {
        if(imp == null || imp.getStackSize() == 1)
        {
            IJ.error("Stack required");
            return null;
        }
        int stackWidth = imp.getWidth();
        int stackHeight = imp.getHeight();
        int nSlices = imp.getStackSize();
        ij.process.ImageStatistics is = imp.getStatistics();
        int width = (int)((double)stackWidth * scale);
        int height = (int)((double)stackHeight * scale);
        int montageWidth = width * columns;
        int montageHeight = height * rows;
        ImageProcessor montage = imp.getProcessor().createProcessor(montageWidth, montageHeight);
        is = imp.getStatistics();
        ImageStack stack = imp.getStack();
        int x = 0;
        int y = 0;
        for(int slice = first; slice <= Math.max(first, last) && slice >= Math.min(first, last); slice += inc)
        {
            ImageProcessor aSlice = stack.getProcessor(slice);
            aSlice.setInterpolate(false);
            if(scale != 1.0D)
                aSlice = aSlice.resize(width, height);
            montage.insert(aSlice, x, y);
            if(borders)
                drawBorder(montage, x, y, width, height);
            if(labels)
                drawLabel(montage, slice, x, y, width, height);
            x += width;
            if(x >= montageWidth)
            {
                x = 0;
                y += height;
                if(y >= montageHeight)
                    break;
            }
            IJ.showProgress((double)(slice - first) / (double)(last - first));
        }

        if(borders)
            drawBorder(montage, 0, 0, montageWidth - 1, montageHeight - 1);
        IJ.showProgress(1.0D);
        return new ImagePlus("Montage", montage);
    }

    void drawBorder(ImageProcessor montage, int x, int y, int width, int height)
    {
        montage.moveTo(x, y);
        montage.lineTo(x + width, y);
        montage.lineTo(x + width, y + height);
        montage.lineTo(x, y + height);
        montage.lineTo(x, y);
    }

    void drawLabel(ImageProcessor montage, int slice, int x, int y, int width, int height)
    {
        String s = "" + slice;
        int swidth = montage.getStringWidth(s);
        x += width / 2 - swidth / 2;
        y += height;
        montage.moveTo(x, y);
        montage.drawString(s);
    }

    private static int columns;
    private static int rows;
    private static int first;
    private static int last;
    private static int inc;
    private static double scale;
    private static boolean labels = false;
    private static boolean borders = false;
    private static int saveID;

}
