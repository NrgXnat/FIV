// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVMontageWindow.java

import java.awt.Panel;

public class FIVMontageWindow extends FIVWindow
{

    public FIVMontageWindow(FIVImage image)
    {
        super(image, new FIVMontageCanvas(image));
    }

    public void addComponents()
    {
        if(image.getFunctionalImagePlus() != null)
            eastPanel.add(mapPanel, "Center");
        eastPanel.add(syncButton, "South");
        add(imagePanel, "Center");
        add(eastPanel, "East");
    }
}
