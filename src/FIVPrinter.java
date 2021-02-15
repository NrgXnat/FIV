// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVPrinter.java

import ij.ImagePlus;
import ij.gui.GenericDialog;
import java.awt.*;

public class FIVPrinter
{

    public FIVPrinter(FIVImage fim)
    {
        imp = fim.getBaseImagePlus();
        display = fim.getDisplay();
        ImagePlus tip = fim.getFunctionalImagePlus();
        if(tip != null)
        {
            showMap = true;
            title = tip.getTitle();
        } else
        {
            title = imp.getTitle();
        }
    }

    public void setup()
    {
        pageSetup();
    }

    void pageSetup()
    {
        GenericDialog gd = new GenericDialog("Page Setup");
        gd.addNumericField("Scaling (5-500%):", scaling, 100);
        gd.addCheckbox("Draw Activation Range", drawScale);
        gd.addCheckbox("Draw Border", drawBorder);
        gd.addCheckbox("Center on Page", center);
        gd.addCheckbox("Print Information", label);
        gd.showDialog();
        if(gd.wasCanceled())
            return;
        scaling = gd.getNextNumber();
        if(scaling < 5D)
            scaling = 5D;
        drawScale = gd.getNextBoolean();
        drawBorder = gd.getNextBoolean();
        center = gd.getNextBoolean();
        label = gd.getNextBoolean();
    }

    public void print()
    {
        FIVWindow win = (FIVWindow)imp.getWindow();
        if(win == null)
        {
            FIVToolWindow.appendLog(1, "FIVPrinter: print: no valid image window");
            return;
        }
        FIVCanvas canvas = (FIVCanvas)win.getCanvas();
        MapCanvas mc = win.getMapCanvas();
        Toolkit toolkit = win.getToolkit();
        PageAttributes pa = new PageAttributes();
        if(display == 1)
            pa.setOrientationRequested(java.awt.PageAttributes.OrientationRequestedType.LANDSCAPE);
        else
            pa.setOrientationRequested(java.awt.PageAttributes.OrientationRequestedType.PORTRAIT);
        pa.setColor(java.awt.PageAttributes.ColorType.COLOR);
        pa.setMedia(java.awt.PageAttributes.MediaType.NA_LETTER);
        PrintJob job = toolkit.getPrintJob(win, "FIV_" + title, null, pa);
        if(job == null)
        {
            FIVToolWindow.appendLog(1, "FIVPrinter: print: no valid job");
            return;
        }
        Graphics g = job.getGraphics();
        if(g == null)
        {
            FIVToolWindow.appendLog(1, "FIVPrinter: print: no valid grahpics");
            return;
        }
        Dimension pageSize = job.getPageDimension();
        double scale = scaling / 100D;
        int imageWidth = imp.getWidth();
        int imageHeight = imp.getHeight();
        int mapWidth = 35;
        int width = (int)((double)imageWidth * scale);
        int height = (int)((double)imageHeight * scale);
        int margin = 40;
        int labelHeight = 0;
        g.setColor(Color.black);
        if(label)
        {
            labelHeight = 15;
            g.setFont(new Font("SansSerif", 0, 12));
            g.drawString(title, margin + 5, (margin + labelHeight) - 3);
        }
        int maxWidth = pageSize.width - margin * 2 - mapWidth;
        int maxHeight = pageSize.height - (margin + labelHeight) * 2;
        if(width > maxWidth || height > maxHeight)
        {
            double hscale = (double)maxWidth / (double)imageWidth;
            double vscale = (double)maxHeight / (double)imageHeight;
            if(hscale <= vscale)
                scale = hscale;
            else
                scale = vscale;
            width = (int)((double)imageWidth * scale);
            height = (int)((double)imageHeight * scale);
        }
        g.translate(margin, margin + labelHeight);
        int borderWidth = width + 1;
        if(showMap)
            borderWidth += mapWidth;
        if(drawBorder)
            g.drawRect(-1, -1, borderWidth, height + 1);
        g.setClip(0, 0, width, height);
        canvas.print(g, scale);
        if(showMap)
        {
            g.translate(width, 0);
            g.setClip(0, 0, mapWidth, height);
            mc.print(g, scale);
        }
        g.dispose();
        job.end();
    }

    private ImagePlus imp;
    private static double scaling = 100D;
    private static int display = 1;
    private static boolean drawBorder = true;
    private static boolean drawScale = false;
    private static boolean center = false;
    private static boolean label = true;
    private static boolean showMap = false;
    private static String title = null;

}
