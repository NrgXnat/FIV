// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Separator.java

import java.awt.*;

public class Separator extends Canvas
{

    public Separator(int length, int thickness, int orient)
    {
        orientation = orient;
        if(orient == 0)
            resize(length, thickness);
        else
            resize(thickness, length);
    }

    public void paint(Graphics g)
    {
        Rectangle bbox = bounds();
        Color c = getBackground();
        Color brighter = c.brighter();
        Color darker = c.darker();
        int x1;
        int y1;
        int x2;
        int y2;
        if(orientation == 0)
        {
            x1 = 0;
            x2 = bbox.width - 1;
            y1 = y2 = bbox.height / 2 - 1;
        } else
        {
            x1 = x2 = bbox.width / 2 - 1;
            y1 = 0;
            y2 = bbox.height - 1;
        }
        g.setColor(darker);
        g.drawLine(x1, y2, x2, y2);
        g.setColor(brighter);
        if(orientation == 0)
        {
            g.setColor(darker);
            g.drawLine(x1, y2, x2, y2);
            g.setColor(brighter);
            g.drawLine(x1, y2 + 1, x2, y2 + 1);
        } else
        {
            g.setColor(darker);
            g.drawLine(x2, y1, x2, y2);
            g.setColor(brighter);
            g.drawLine(x2 + 1, y1, x2 + 1, y2);
        }
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    int orientation;

}
