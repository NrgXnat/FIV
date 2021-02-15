// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVSlicer.java

import ij.*;
import ij.measure.Calibration;
import ij.process.*;
import java.awt.image.ColorModel;

public class FIVSlicer
{

    public FIVSlicer()
    {
    }

    public ImagePlus slice(ImagePlus image, int x1, int y1, int x2, int y2, int w)
    {
        imp = image;
        if(imp == null)
        {
            IJ.noImage();
            return null;
        }
        if(imp.getStackSize() < 2)
        {
            IJ.error("Stack required");
            return null;
        }
        Calibration cal = imp.getCalibration();
        swidth = w;
        zscale = cal.pixelDepth / cal.pixelWidth;
        interpolate = false;
        setLineParams(x1, y1, x2, y2);
        long tstart = System.currentTimeMillis();
        if(!imp.lock())
        {
            return null;
        } else
        {
            ImagePlus oimg = sliceImage(imp);
            imp.unlock();
            long tstop = System.currentTimeMillis();
            double seconds = (double)(tstop - tstart) / 1000D;
            return oimg;
        }
    }

    public void setZScaling(double zscale)
    {
        this.zscale = zscale;
    }

    public void setSliceWidth(int swidth)
    {
        swidth = swidth;
    }

    public ImagePlus sliceImage(ImagePlus imp)
    {
        adjustParameters(imp);
        initParameters(imp);
        ImageStack stack = imp.getStack();
        ImageStack ostack = stackSlice(stack);
        if(zscale != 1.0D)
            ostack = applyZScaling(ostack);
        return new ImagePlus("Slice", ostack);
    }

    private ImageStack stackSlice(ImageStack stack)
    {
        double nrm = Math.sqrt(dx * dx + dy * dy);
        double sXInc = -dy / nrm;
        double sYInc = dx / nrm;
        ImageStack ostack = new ImageStack(number, stack.getSize(), cmod);
        for(int n = 0; n < swidth; n++)
        {
            ImageProcessor ip = processorSlice(stack);
            int type = imp.getType();
            switch(type)
            {
            case 0: // '\0'
                ip = ip.convertToByte(false);
                break;

            case 1: // '\001'
                ip = ip.convertToShort(false);
                break;
            }
            ostack.addSlice("", ip);
            xstart += sXInc;
            ystart += sYInc;
            xend += sXInc;
            yend += sYInc;
            if(n % 3 == 0)
                IJ.showProgress((double)n / (double)swidth);
        }

        IJ.showProgress(1.0D);
        return ostack;
    }

    private ImageProcessor processorSlice(ImageStack stack)
    {
        ImageProcessor sp = stack.getProcessor(1);
        int width = number;
        int height = stack.getSize();
        ImageProcessor oip;
        if(imp.getType() == 4)
            oip = sp.createProcessor(width, height);
        else
            oip = new FloatProcessor(width, height, new float[width * height], cmod);
        ImageProcessor sip = sp.createProcessor(sp.getWidth(), sp.getHeight());
        sip.setInterpolate(interpolate);
        int row = stack.getSize() - 1;
        for(int n = 1; n <= stack.getSize();)
        {
            sip.setPixels(stack.getPixels(n));
            line2Image(sip, oip, row);
            n++;
            row--;
        }

        sip = null;
        oip.resetMinAndMax();
        return oip;
    }

    private ImageStack applyZScaling(ImageStack stack)
    {
        if(zscale > 25D)
            zscale = 25D;
        if(zscale < 0.050000000000000003D)
            zscale = 0.050000000000000003D;
        ImageProcessor ip = stack.getProcessor(1);
        StackProcessor sp = new StackProcessor(stack, ip);
        ip.setInterpolate(interpolate);
        return sp.resize(imp.getWidth(), (int)(zscale * (double)stack.getHeight()));
    }

    private void line2Image(ImageProcessor ip, ImageProcessor oip, int row)
    {
        double rx = xstart;
        double ry = ystart;
        int ivalue = 0;
        boolean interpolateLine = interpolate && (xinc != 1.0D || yinc != 0.0D);
        if(oip instanceof FloatProcessor)
        {
            for(int n = 0; n < number; n++)
            {
                double value;
                if(interpolateLine)
                    value = ip.getInterpolatedPixel(rx, ry);
                else
                    ivalue = ip.getPixel((int)(rx + 0.5D), (int)(ry + 0.5D));
                rx += xinc;
                ry += yinc;
                oip.putPixel(n, row, ivalue);
            }

        }
    }

    private void setLineParams(int x1, int y1, int x2, int y2)
    {
        dx = x2 - x1;
        dy = y2 - y1;
        number = (int)Math.round(Math.sqrt(dx * dx + dy * dy)) + 1;
        xinc = dx / (double)number;
        yinc = dy / (double)number;
        xstart = x1;
        ystart = y1;
        xend = x2;
        yend = y2;
    }

    private void adjustParameters(ImagePlus imp)
    {
        if(zscale < 0.0D)
            zscale = 1.0D;
        int w = imp.getWidth();
        int h = imp.getHeight();
        int maxwidth = (int)Math.sqrt(w * w + h * h);
        if(swidth < 1 || swidth > maxwidth)
            swidth = 1;
    }

    private void initParameters(ImagePlus imp)
    {
        cmod = imp.getProcessor().getColorModel();
        width = imp.getWidth();
        height = imp.getHeight();
    }

    private double xinc;
    private double yinc;
    private double xstart;
    private double ystart;
    private double xend;
    private double yend;
    private double dx;
    private double dy;
    private int number;
    private double zscale;
    private static int swidth = 1;
    private static boolean interpolate = false;
    private ColorModel cmod;
    private int width;
    private int height;
    private ImagePlus imp;

}
