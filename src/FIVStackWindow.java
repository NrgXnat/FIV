// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVStackWindow.java

import ij.ImagePlus;
import ij.ImageStack;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.PrintStream;

public class FIVStackWindow extends FIVWindow
    implements Runnable, AdjustmentListener
{

    public FIVStackWindow(FIVImage image)
    {
        super(image, new FIVStackCanvas(image));
        thread = new Thread(this, "SliceSelector");
        thread.start();
    }

    public void addComponents()
    {
        bottomPanel = new Panel();
        bottomPanel.setLayout(new BorderLayout());
        ImagePlus ip = image.getBaseImagePlus();
        if(ip != null)
        {
            ImageStack s = ip.getStack();
            int stackSize = s.getSize();
            sliceSelector = new Scrollbar(0, 1, 1, 1, stackSize + 1);
            bottomPanel.add(sliceSelector, "Center");
            sliceSelector.addAdjustmentListener(this);
            int blockIncrement = stackSize / 10;
            if(blockIncrement < 1)
                blockIncrement = 1;
            sliceSelector.setUnitIncrement(1);
            sliceSelector.setBlockIncrement(blockIncrement);
            sliceSelector.setValue(ip.getCurrentSlice());
        }
        if(image.getFunctionalImagePlus() != null)
            eastPanel.add(mapPanel, "Center");
        bottomPanel.add(syncButton, "East");
        add(imagePanel, "Center");
        add(eastPanel, "East");
        add(bottomPanel, "South");
    }

    public synchronized void adjustmentValueChanged(AdjustmentEvent e)
    {
        if(image == null)
        {
            System.out.println("FIVStackWindow: adjustmentValueChanged: image null!");
            return;
        }
        if(!running)
        {
            if(image.getView() != 3)
            {
                slice = sliceSelector.getValue();
            } else
            {
                int selected = sliceSelector.getValue();
                slice = sliceSelector.getMaximum() - selected;
            }
            notify();
            image.setCoordBySlice(slice);
            image.getManager().syncToScroll();
            ImagePlus ip = image.getBaseImagePlus();
            if(ip != null)
                ip.updateAndDraw();
        }
    }

    public void showSlice(int index)
    {
        ImagePlus ip = image.getBaseImagePlus();
        if(ip != null && index >= 1 && index <= ip.getStackSize() && ip.getCurrentSlice() != index)
        {
            ip.setSlice(index);
            if(image.getView() != 3)
                sliceSelector.setValue(index);
            else
                sliceSelector.setValue(sliceSelector.getMaximum() - index);
            ip.updateAndDraw();
        }
    }

    public void run()
    {
        while(!done) 
        {
            synchronized(this)
            {
                try
                {
                    wait();
                }
                catch(InterruptedException e) { }
            }
            if(done)
                return;
            if(slice > 0)
            {
                int s = slice;
                slice = 0;
                ImagePlus ip = image.getBaseImagePlus();
                if(ip != null && s != ip.getCurrentSlice())
                    ip.setSlice(s);
            }
        }
    }

    protected Panel bottomPanel;
    protected Scrollbar sliceSelector;
    protected Thread thread;
    protected boolean done;
    protected int slice;
}
