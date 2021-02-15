// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVImageJ.java

import ij.ImageJ;
import java.applet.Applet;

public class FIVImageJ extends ImageJ
{

    public FIVImageJ()
    {
        showIt = false;
    }

    public FIVImageJ(Applet a)
    {
        super(a);
        showIt = false;
    }

    public void setVisible(boolean b)
    {
        showIt = b;
        super.setVisible(b);
    }

    public void show()
    {
        if(showIt)
            super.show();
    }

    boolean showIt;
}
