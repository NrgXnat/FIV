// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BorderPanel.java

import java.awt.*;
import java.io.PrintStream;

public class BorderPanel extends Panel
{

    public BorderPanel()
    {
        setInsets(4, 4, 4, 4);
    }

    public BorderPanel(int top, int left, int bottom, int right)
    {
        setInsets(top, left, bottom, right);
    }

    public void setInsets(int top, int left, int bottom, int right)
    {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public float getAlignmentX()
    {
        return 0.5F;
    }

    public float getAlignmentY()
    {
        System.out.println("returning center alignment");
        return 0.5F;
    }

    public Dimension getPreferredSize()
    {
        Dimension d = super.getPreferredSize();
        return d;
    }

    public Insets insets()
    {
        return new Insets(top, left, bottom, right);
    }

    public void paint(Graphics g)
    {
        Dimension size = size();
        g.setColor(Color.black);
        g.drawRect(left / 2, top / 2, size.width - left / 2 - right / 2, size.height - top / 2 - bottom / 2);
        g.setColor(Color.white);
        g.drawRect(left / 2 + 1, top / 2 + 1, size.width - left / 2 - right / 2, size.height - top / 2 - bottom / 2);
    }

    int top;
    int left;
    int bottom;
    int right;
}
