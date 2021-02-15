// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVImage.java

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.ImageCanvas;
import ij.io.FileSaver;
import ij.measure.Calibration;
import ij.process.*;
import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.text.NumberFormat;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FIVImage
{

    public FIVImage(ImagePlus functional, ImagePlus structural, FIVManager man)
    {
        selfClose = false;
        locked = false;
        serialString = "";
        serialEntry = 0;
        view = 3;
        display = 2;
        radiologic = true;
        minThreshold = 5D;
        maxThreshold = 12D;
        alpha = 255;
        actDisplay = 3;
        fimp = functional;
        simp = structural;
        manager = man;
        if(simp == null && fimp == null)
            FIVToolWindow.appendLog(1, "Your structural and funtional images were invalid.");
        if(fimp != null)
            title = fimp.getTitle();
        else
            title = simp.getTitle();
    }

    public boolean show()
    {
        return show(view, display, radiologic);
    }

    public boolean show(int view, int display, boolean radiologic)
    {
        this.view = view;
        this.display = display;
        this.radiologic = radiologic;
        if(simp != null && fimp != null && (fimp.getWidth() != simp.getWidth() || fimp.getHeight() != simp.getHeight() || fimp.getStackSize() != simp.getStackSize()))
        {
            FIVImage _tmp = this;
            FIVToolWindow.appendLog(1, "Structural and functional images must have the same dimensions. ");
            FIVImage _tmp1 = this;
            FIVToolWindow.appendLog(1, new String("\tStructural image: " + simp.getWidth() + ", " + simp.getHeight()));
            FIVImage _tmp2 = this;
            FIVToolWindow.appendLog(1, new String("\tFuntional image: " + fimp.getWidth() + ", " + fimp.getHeight()));
            toolWindow.requestFocus();
            return false;
        }
        coords = new FIVCoords();
        coords.setView(view);
        coords.setDisplay(display);
        coords.setRadiologic(radiologic);
        coords.setImageSize(getBaseImagePlus().getWidth(), getBaseImagePlus().getHeight(), getBaseImagePlus().getStackSize());
        coords.setVoxelSize((int)getBaseImagePlus().getCalibration().pixelWidth);
        coords.loadBoundingBox();
        double min = 0.0D;
        double max = 0.0D;
        if(simp != null)
        {
            String sMin = PropReader.getString("mode." + PropReader.getString("mode") + ".anat.low");
            String sMax = PropReader.getString("mode." + PropReader.getString("mode") + ".anat.high");
            if(!sMin.equalsIgnoreCase("auto"))
                try
                {
                    min = (new Double(sMin)).doubleValue();
                    max = (new Double(sMax)).doubleValue();
                }
                catch(Exception e)
                {
                    FIVToolWindow.appendLog(1, new String("The anatomic intensity values are not valid: " + sMin + " and " + sMax));
                    min = 0.0D;
                    max = 0.0D;
                }
            if(max == 0.0D)
            {
                min = simp.getProcessor().getMin();
                max = simp.getProcessor().getMax();
            }
            minIntensityDefault = min;
            maxIntensityDefault = max;
        }
        switch(view)
        {
        case 3: // '\003'
            if(simp != null)
            {
                simp = new ImagePlus(simp.getTitle(), copyStack(simp.getStack()));
                simp.getProcessor().setMinAndMax(min, max);
                if(!radiologic)
                {
                    StackProcessor sp = new StackProcessor(simp.getStack(), simp.getProcessor());
                    sp.flipHorizontal();
                }
                simp = cropSlices(simp);
            }
            if(fimp != null)
            {
                fimp = new ImagePlus(fimp.getTitle(), copyStack(fimp.getStack()));
                if(!radiologic)
                {
                    StackProcessor sp = new StackProcessor(fimp.getStack(), fimp.getProcessor());
                    sp.flipHorizontal();
                }
                fimp = cropSlices(fimp);
            }
            break;

        case 1: // '\001'
            if(simp != null)
            {
                simp = slice(simp, 1);
                simp.getProcessor().setMinAndMax(min, max);
                if(!radiologic)
                {
                    StackProcessor sp = new StackProcessor(simp.getStack(), simp.getProcessor());
                    sp.flipHorizontal();
                }
                simp = cropSlices(simp);
            }
            if(fimp != null)
            {
                fimp = slice(fimp, 1);
                if(!radiologic)
                {
                    StackProcessor sp = new StackProcessor(fimp.getStack(), fimp.getProcessor());
                    sp.flipHorizontal();
                }
                fimp = cropSlices(fimp);
            }
            break;

        case 2: // '\002'
            if(simp != null)
            {
                simp = slice(simp, 2);
                simp.getProcessor().setMinAndMax(min, max);
                simp = cropSlices(simp);
            }
            if(fimp != null)
            {
                fimp = slice(fimp, 2);
                fimp = cropSlices(fimp);
            }
            break;
        }
        if(display == 2)
            showStack();
        else
        if(display == 1)
            showMontage();
        return true;
    }

    public ImagePlus slice(ImagePlus ip, int orient)
    {
        ImageStack oldStack = ip.getStack();
        int oldStackSize = ip.getStackSize();
        int newWidth;
        int newHeight;
        int newStackSize;
        if(orient == 2)
        {
            newWidth = ip.getHeight();
            newHeight = oldStackSize;
            newStackSize = ip.getWidth();
        } else
        {
            newWidth = ip.getWidth();
            newHeight = oldStackSize;
            newStackSize = ip.getHeight();
        }
        int data[] = new int[newWidth];
        ImageStack newStack = new ImageStack(newWidth, newHeight, ip.getProcessor().getColorModel());
        ImageProcessor baseProc = oldStack.getProcessor(1);
        ImageProcessor procOld = baseProc.createProcessor(baseProc.getWidth(), baseProc.getHeight());
        for(int i = 0; i < newStackSize; i++)
        {
            ImageProcessor procNew = new FloatProcessor(newWidth, newHeight);
            for(int j = oldStackSize; j > 0; j--)
            {
                procOld.setPixels(oldStack.getPixels(j));
                if(orient == 2)
                    procOld.getColumn(newStackSize - i - 1, 0, data, newWidth);
                else
                    procOld.getRow(0, i, data, newWidth);
                procNew.putRow(0, oldStackSize - j, data, newWidth);
            }

            newStack.addSlice("", procNew);
        }

        return new ImagePlus(ip.getTitle(), newStack);
    }

    public ImagePlus cropSlices(ImagePlus ip)
    {
        int first = coords.getFirstSliceInBox();
        int last = coords.getLastSliceInBox();
        int stackSize = ip.getStackSize();
        for(int i = 1; i < first; i++)
            ip.getStack().deleteSlice(1);

        for(int i = stackSize; i > last; i--)
            ip.getStack().deleteLastSlice();

        return ip;
    }

    private ImageStack copyStack(ImageStack stack)
    {
        int n = stack.getSize();
        ImageStack ns = new ImageStack(stack.getWidth(), stack.getHeight());
        for(int i = 1; i <= n; i++)
            ns.addSlice("", stack.getProcessor(i).getPixelsCopy());

        return ns;
    }

    private void showStack()
    {
        coords.setTalFromTal(0, 0, 0);
        getBaseImagePlus().setSlice(coords.getSlice());
        window = new FIVStackWindow(this);
        window.setTitle(title);
    }

    private void showMontage()
    {
        makeMontage();
        window = new FIVMontageWindow(this);
        window.setTitle(title);
    }

    void makeMontage()
    {
        FIVMontageMaker mm = new FIVMontageMaker();
        int startSlice = getMontageStartSlice();
        int endSlice = getMontageEndSlice();
        int increment = getMontageIncrement();
        int nRows = getMontageRowCount();
        int nColumns = getMontageColumnCount();
        int scale = coords.getScale();
        System.out.println("makeMontage: startSlice" + startSlice + " endSlice: " + endSlice + " increment: " + increment + " rows: " + nRows + " columns: " + nColumns);
        coords.setMontageIncrement(increment);
        coords.setMontageStartSlice(startSlice - 1);
        coords.setMontageColumnCount(nColumns);
        Rectangle rect = coords.getBoundingBox();
        if(simp != null)
        {
            ImageStack stack = simp.getStack();
            ImageStack newStack = new ImageStack(rect.width, rect.height, simp.getProcessor().getColorModel());
            for(int i = 1; i <= stack.getSize(); i++)
            {
                ImageProcessor proc = stack.getProcessor(i);
                proc.setRoi(rect);
                proc = proc.crop();
                newStack.addSlice("", proc);
            }

            simp = mm.makeMontage(new ImagePlus("", newStack), nColumns, nRows, scale, startSlice, endSlice, increment);
            simp.setTitle(title);
        }
        if(fimp != null)
        {
            ImageStack stack = fimp.getStack();
            ImageStack newStack = new ImageStack(rect.width, rect.height, fimp.getProcessor().getColorModel());
            for(int i = 1; i <= stack.getSize(); i++)
            {
                ImageProcessor proc = stack.getProcessor(i);
                proc.setRoi(rect);
                proc = proc.crop();
                newStack.addSlice("", proc);
            }

            fimp = mm.makeMontage(new ImagePlus("", newStack), nColumns, nRows, scale, startSlice, endSlice, increment);
            fimp.setTitle(title);
        }
    }

    public int getMontageRowCount()
    {
        double aspectRatio;
        if(coords.getVoxelSize() == 1)
        {
            if(view == 3)
                aspectRatio = 2D;
            else
            if(view == 1)
                aspectRatio = 1.5D;
            else
                aspectRatio = 1.0D;
        } else
        if(view == 2)
            aspectRatio = 1.75D;
        else
            aspectRatio = 2.5D;
        int sliceCount = (int)Math.abs(Math.ceil((double)((getMontageEndSlice() - getMontageStartSlice()) + 1) / (double)getMontageIncrement()));
        int nRows = (int)Math.ceil(Math.sqrt((double)sliceCount / aspectRatio));
        return nRows;
    }

    public int getMontageColumnCount()
    {
        int sliceCount = (int)Math.abs(Math.ceil((double)((getMontageEndSlice() - getMontageStartSlice()) + 1) / (double)getMontageIncrement()));
        int nRows = getMontageRowCount();
        int nColumns = (int)Math.ceil((double)sliceCount / (double)nRows);
        return nColumns;
    }

    public int getMontageIncrement()
    {
        int voxelSize = coords.getVoxelSize();
        int inc;
        switch(voxelSize)
        {
        case 1: // '\001'
            inc = 4;
            break;

        case 2: // '\002'
            inc = 2;
            break;

        case 3: // '\003'
            inc = 1;
            break;

        default:
            FIVToolWindow.appendLog(1, new String("FIVImage: makeMontage: invalid voxel size: " + voxelSize));
            inc = 2;
            break;
        }
        if(view == 3)
            inc = -inc;
        return inc;
    }

    public int getMontageStartSlice()
    {
        switch(coords.getVoxelSize())
        {
        case 1: // '\001'
            switch(view)
            {
            case 3: // '\003'
                return 153;

            case 1: // '\001'
                return 20;

            case 2: // '\002'
                return 20;
            }
            // fall through

        case 2: // '\002'
            switch(view)
            {
            case 3: // '\003'
                return getBaseImagePlus().getStackSize();

            case 1: // '\001'
                return 1;

            case 2: // '\002'
                return 1;
            }
            // fall through

        case 3: // '\003'
            switch(view)
            {
            case 3: // '\003'
                return getBaseImagePlus().getStackSize();

            case 1: // '\001'
                return 1;

            case 2: // '\002'
                return 1;
            }
            // fall through

        default:
            return 0;
        }
    }

    public int getMontageEndSlice()
    {
        switch(coords.getVoxelSize())
        {
        case 1: // '\001'
            switch(view)
            {
            case 3: // '\003'
                return 29;

            case 1: // '\001'
                return 180;

            case 2: // '\002'
                return 136;
            }
            // fall through

        case 2: // '\002'
            switch(view)
            {
            case 3: // '\003'
                return 1;

            case 1: // '\001'
                return getBaseImagePlus().getStackSize();

            case 2: // '\002'
                return getBaseImagePlus().getStackSize();
            }
            // fall through

        case 3: // '\003'
            switch(view)
            {
            case 3: // '\003'
                return 1;

            case 1: // '\001'
                return getBaseImagePlus().getStackSize();

            case 2: // '\002'
                return getBaseImagePlus().getStackSize();
            }
            // fall through

        default:
            return 0;
        }
    }

    public int getView()
    {
        return view;
    }

    public FIVWindow getWindow()
    {
        return window;
    }

    public FIVManager getManager()
    {
        return manager;
    }

    public int getDisplay()
    {
        return display;
    }

    public int getCurrentSlice()
    {
        return getBaseImagePlus().getCurrentSlice();
    }

    public boolean getRadiologic()
    {
        return radiologic;
    }

    public double getMinIntensity()
    {
        return simp.getProcessor().getMin();
    }

    public double getMaxIntensity()
    {
        return simp.getProcessor().getMax();
    }

    public double getMinThreshold()
    {
        return minThreshold;
    }

    public double getMaxThreshold()
    {
        return maxThreshold;
    }

    public FIVCoords getCoords()
    {
        return coords;
    }

    public ImagePlus getFunctionalImagePlus()
    {
        return fimp;
    }

    public ImagePlus getStructuralImagePlus()
    {
        return simp;
    }

    public ImagePlus getBaseImagePlus()
    {
        if(simp != null)
            return simp;
        else
            return fimp;
    }

    public int getActivationDisplay()
    {
        return actDisplay;
    }

    public double getActivationLevel()
    {
        Point p = coords.getPosImagePlus();
        double val = 0.0D;
        if(fimp != null)
        {
            return (double)fimp.getProcessor().getPixelValue(p.x, p.y);
        } else
        {
            val = simp.getProcessor().getPixelValue(p.x, p.y);
            return val;
        }
    }

    public Rectangle getBoundingBox()
    {
        return coords.getBoundingBox();
    }

    public double getScale()
    {
        return (double)coords.getScale();
    }

    public void scale(int s)
    {
        if((double)s != getScale())
        {
            coords.setScale(s);
            selfClose = true;
            if(display == 2)
                showStack();
            else
            if(display != 1);
        }
    }

    public boolean isSynced()
    {
        return locked;
    }

    public String getTitle()
    {
        return title;
    }

    public void setSync(boolean lock)
    {
        locked = lock;
        window.setLock(locked);
    }

    public void setToolWindow(FIVToolWindow tw)
    {
        toolWindow = tw;
    }

    public void makeCurrent()
    {
        FIVManager.setCurrentImage(this);
    }

    public void windowClosing()
    {
        if(selfClose)
            selfClose = false;
        else
            FIVManager.removeImage(this);
    }

    public static int strToView(String s)
    {
        if(s.trim().equalsIgnoreCase("TRANSVERSE"))
            return 3;
        if(s.trim().equalsIgnoreCase("CORONAL"))
            return 1;
        if(s.trim().equalsIgnoreCase("SAGITTAL"))
        {
            return 2;
        } else
        {
            FIVToolWindow.appendLog(1, new String("FIVImage: strToView: Bad parameter value: " + s));
            return 3;
        }
    }

    public static int strToDisplay(String s)
    {
        if(s.trim().equalsIgnoreCase("NONE"))
            return 0;
        if(s.trim().equalsIgnoreCase("STACK"))
            return 2;
        if(s.trim().equalsIgnoreCase("MONTAGE"))
        {
            return 1;
        } else
        {
            FIVToolWindow.appendLog(1, new String("FIVImage: strToDisplay: Bad parameter value: " + s));
            return 1;
        }
    }

    public void setCoordsFromWindow(int x, int y)
    {
        if(display == 2)
            coords.setTalFromStack(x, y, getBaseImagePlus().getCurrentSlice());
        else
            coords.setTalFromMontage(x, y);
        Point3d pt = coords.getPosTal();
        double v = getActivationLevel();
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(3);
        String sx = formatter.format(pt.x);
        String sy = formatter.format(pt.y);
        String sz = formatter.format(pt.z);
        String sv = formatter.format(v);
        if(window != null)
            window.setTipText(sx + ", " + sy + ", " + sz + "   " + sv);
    }

    public void printCoords()
    {
        Point3d pt = coords.getPosTal();
        double v = getActivationLevel();
        toolWindow.setCoords(pt.x, pt.y, pt.z, v);
    }

    public void logCoords()
    {
        Point3d pt = coords.getPosTal();
        double v = getActivationLevel();
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(3);
        String sx = formatter.format(pt.x);
        String sy = formatter.format(pt.y);
        String sz = formatter.format(pt.z);
        String sv = formatter.format(v);
        FIVImage _tmp = this;
        FIVToolWindow.appendLog(3, new String("Coordinate: " + sx + ", " + sy + ", " + sz + " Value: " + sv));
    }

    public void printIntensityRange()
    {
        double min = simp.getProcessor().getMin();
        double max = simp.getProcessor().getMax();
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(2);
        String smin = formatter.format(min);
        String smax = formatter.format(max);
        window.setTipText("Intensity range: " + smin + " to " + smax);
    }

    public void logIntensityRange()
    {
        double min = simp.getProcessor().getMin();
        double max = simp.getProcessor().getMax();
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(2);
        String smin = formatter.format(min);
        String smax = formatter.format(max);
        FIVImage _tmp = this;
        FIVToolWindow.appendLog(3, new String("Intensity range: " + smin + " to " + smax));
    }

    public void setCoordBySlice(int s)
    {
        coords.setTalFromSlice(s);
    }

    public void setCoords(FIVCoords newCoords)
    {
        coords.setTalFromTal(newCoords.getPosTal());
    }

    public void updateCrosshairs()
    {
        if(display == 2)
            window.updateCrosshairs(coords.getPosWindow());
    }

    public void updateSlice()
    {
        if(display == 2)
            ((FIVStackWindow)window).showSlice(coords.getSlice());
    }

    public void setCoords(int x, int y, int z)
    {
        coords.setTalFromTal(x, y, z);
        if(display == 2 && locked)
        {
            ((FIVStackWindow)window).showSlice(coords.getSlice());
            if(FIVManager.isCrosshairVisible())
                window.updateCrosshairs(coords.getPosWindow());
        }
    }

    public void setAlpha(int a)
    {
        alpha = a;
    }

    public int getAlpha()
    {
        return alpha;
    }

    public void setMinThreshold(double d)
    {
        minThreshold = d;
        ImageProcessor ip = fimp.getProcessor();
        window.getCanvas().repaint();
    }

    public void setMaxThreshold(double d)
    {
        maxThreshold = d;
        window.getCanvas().repaint();
    }

    public void setThreshold(double min, double max, int sign)
    {
        minThreshold = min;
        maxThreshold = max;
        actDisplay = sign;
        if(window != null)
        {
            window.setScaleBars(min, max, sign);
            window.getCanvas().repaint();
        }
    }

    public void setSlice(int s)
    {
        if(display != 2)
        {
            return;
        } else
        {
            setCoordBySlice(s);
            ((FIVStackWindow)window).showSlice(coords.getSlice());
            return;
        }
    }

    public void sliceIncrement(int inc)
    {
        if(display != 2)
            return;
        if(view == 3)
            setCoordBySlice(getBaseImagePlus().getCurrentSlice() - inc);
        else
            setCoordBySlice(getBaseImagePlus().getCurrentSlice() + inc);
        ((FIVStackWindow)window).showSlice(coords.getSlice());
    }

    public void sliceDecrement(int inc)
    {
        if(display != 2)
            return;
        if(view == 3)
            setCoordBySlice(getBaseImagePlus().getCurrentSlice() + inc);
        else
            setCoordBySlice(getBaseImagePlus().getCurrentSlice() - inc);
        ((FIVStackWindow)window).showSlice(coords.getSlice());
    }

    public void sliceTop()
    {
        if(display != 2)
            return;
        if(view == 3)
            setCoordBySlice(getBaseImagePlus().getStackSize());
        else
            setCoordBySlice(1);
    }

    public void sliceBottom()
    {
        if(display != 2)
            return;
        if(view == 3)
            setCoordBySlice(1);
        else
            setCoordBySlice(getBaseImagePlus().getStackSize());
    }

    public void showPopup()
    {
        window.showPopup();
    }

    public void setIntensity(double min, double max)
    {
        if(simp != null)
        {
            simp.getProcessor().setMinAndMax(min, max);
            simp.updateAndDraw();
        }
    }

    public void setIntensityToDefault()
    {
        if(simp != null)
        {
            simp.getProcessor().setMinAndMax(minIntensityDefault, maxIntensityDefault);
            simp.updateAndDraw();
        }
    }

    void adjustBrightness(double adjustVal)
    {
        if(simp == null)
        {
            return;
        } else
        {
            double min = simp.getProcessor().getMin();
            double max = simp.getProcessor().getMax();
            double increment = (max - min) * adjustVal;
            min -= increment;
            max -= increment;
            simp.getProcessor().setMinAndMax(min, max);
            simp.updateAndDraw();
            return;
        }
    }

    public void adjustContrast(double adjustVal)
    {
        if(simp == null)
        {
            return;
        } else
        {
            double min = simp.getProcessor().getMin();
            double max = simp.getProcessor().getMax();
            double center = (min + max) / 2D;
            double increment = (max - min) * adjustVal;
            double range = (max - min) + increment;
            min = center - range / 2D;
            max = center + range / 2D;
            simp.getProcessor().setMinAndMax(min, max);
            simp.updateAndDraw();
            return;
        }
    }

    public void print()
    {
        FIVPrinter p = new FIVPrinter(this);
        p.print();
    }

    public void save(String filename)
    {
        ImageCanvas canvas = window.getCanvas();
        boolean showMap = false;
        if(fimp != null)
            showMap = true;
        int w;
        int h;
        if(display == 1)
        {
            w = getBaseImagePlus().getWidth();
            h = getBaseImagePlus().getHeight();
        } else
        {
            int scale = coords.getScale();
            Rectangle box = coords.getBoundingBox();
            System.out.println("Box: " + box);
            w = box.width * scale;
            h = box.height * scale;
        }
        int mapWidth;
        if(showMap)
            mapWidth = 30;
        else
            mapWidth = 0;
        Image img = canvas.createImage(w + mapWidth, h);
        try
        {
            Graphics g = img.getGraphics();
            g.setClip(0, 0, w, h);
            canvas.print(g);
            g.translate(w, 0);
            if(showMap)
            {
                g.setClip(0, 0, mapWidth, h);
                window.getMapCanvas().print(g, 1.0D);
            }
            ImagePlus imp = new ImagePlus(title, img);
            if(imp.getType() == 4)
            {
                ImageConverter ic = new ImageConverter(imp);
                ic.convertRGBtoIndexedColor(256);
            }
            if(!filename.endsWith(".gif"))
                filename = filename + ".gif";
            FileSaver fs = new FileSaver(imp);
            fs.saveAsGif(filename);
        }
        catch(Exception e)
        {
            FIVToolWindow.appendLog(1, "Unable to save gif image\n" + e);
        }
    }

    public void save()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(0);
        chooser.setSelectedFile(new File(title + ".gif"));
        chooser.setFileFilter(new FileFilter() {

            public boolean accept(File f)
            {
                if(f.isDirectory())
                    return true;
                String name = f.getName();
                if(name != null)
                    return name.endsWith(".gif");
                else
                    return false;
            }

            public String getDescription()
            {
                return "GIF files";
            }

        }
);
        chooser.showSaveDialog(toolWindow);
        File f = chooser.getSelectedFile();
        if(f == null)
        {
            return;
        } else
        {
            String fname = f.getAbsolutePath();
            save(fname);
            return;
        }
    }

    public static final int CORONAL = 1;
    public static final int SAGITTAL = 2;
    public static final int TRANSVERSE = 3;
    public static final int NO_DISPLAY = 0;
    public static final int MONTAGE = 1;
    public static final int STACK = 2;
    public static final int TOGGLE = 0;
    public static final int POSITIVE = 1;
    public static final int NEGATIVE = 2;
    public static final int BOTH_SIGNS = 3;
    public static final int NO_SIGNS = 4;
    private static final int SERIAL_NONE = 0;
    private static final int SERIAL_MIN = 1;
    private static final int SERIAL_MAX = 2;
    public static final boolean RADIOLOGIC = true;
    private ImagePlus fimp;
    private ImagePlus simp;
    private int view;
    private int display;
    private boolean radiologic;
    private FIVWindow window;
    private FIVToolWindow toolWindow;
    private String title;
    private int alpha;
    private double minThreshold;
    private double maxThreshold;
    private double minIntensityDefault;
    private double maxIntensityDefault;
    private int actDisplay;
    private boolean selfClose;
    private FIVCoords coords;
    private boolean locked;
    private String serialString;
    private int serialEntry;
    private FIVManager manager;

}
