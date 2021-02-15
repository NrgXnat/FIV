// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SmallLabel.java

import java.awt.*;

class SmallLabel extends Label
{

    SmallLabel(int w, int h)
    {
        d = new Dimension(w, h);
        setText("                                                       ");
    }

    SmallLabel(String s, int w, int h)
    {
        super(s);
        d = new Dimension(w, h);
    }

    public void setSize(int w, int h)
    {
        super.setSize(w, h);
        d.setSize(w, h);
    }

    public Dimension getPreferredSize()
    {
        Dimension s = super.getPreferredSize();
        return s;
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    Dimension d;
}
