// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PopupMessage.java

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;

public class PopupMessage extends Window
    implements MouseListener, MouseMotionListener
{

    public PopupMessage(Component c)
    {
        super(new Frame());
        tipPos = new Point(0, 0);
        label = new Label();
        tip = "";
        component = c;
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
        label.setForeground(Color.black);
        label.setFont(new Font("Serif", 0, 12));
        setBackground(BACKGROUND_COLOR);
        add(label, "Center");
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.black);
        g.drawRect(0, 0, getBounds().width - 1, getBounds().height - 1);
    }

    public Insets getInsets()
    {
        return new Insets(2, 2, 2, 2);
    }

    public void setTip(String t)
    {
        tip = t;
    }

    public String getTip()
    {
        return tip;
    }

    public void showTipNow()
    {
        mouseIn = true;
        showTip();
    }

    public void showTip()
    {
        if(mouseIn)
        {
            label.setText(tip);
            pack();
            Point location = component.getLocationOnScreen();
            if(tipPos == null)
                System.out.println("the tipPos is null");
            setLocation(location.x + tipPos.x + 10, location.y + tipPos.y + 10);
            setVisible(true);
        }
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
        mouseIn = true;
    }

    public void mouseExited(MouseEvent e)
    {
        mouseIn = false;
        if(isVisible())
            hide();
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent e)
    {
        hide();
    }

    public void mouseMoved(MouseEvent e)
    {
        tipPos = e.getPoint();
        if(isVisible())
            hide();
    }

    private static final Color BACKGROUND_COLOR = new Color(255, 255, 180);
    private Point tipPos;
    private Label label;
    private String tip;
    private Component component;
    private boolean mouseIn;

}
