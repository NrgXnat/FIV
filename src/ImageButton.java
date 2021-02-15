// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImageButton.java

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

public class ImageButton extends Component
{

    public ImageButton(String up, String down)
    {
        m_mouseDown = false;
        m_mouseWasDown = false;
        m_mouseInside = false;
        m_actionCommand = null;
        m_actionListener = null;
        depressable = true;
        actualState = 0;
        drawState = 0;
        setBackground(Color.red);
        if((m_imageUp = loadImageResource(up)) != null && !waitForImage(m_imageUp))
        {
            System.out.println("Cant load image " + up);
            m_imageUp = null;
        }
        if(m_imageUp == null)
            System.out.println("m_image is NULL !!!");
        if((m_imageDown = loadImageResource(down)) != null && !waitForImage(m_imageDown))
        {
            System.out.println("Cant load image " + down);
            m_imageDown = null;
        }
        if(m_imageDown == null)
            System.out.println("m_image is NULL !!!");
        setUpImage(m_imageUp);
        setDownImage(m_imageDown);
        enableEvents(16L);
    }

    public ImageButton(String image)
    {
        m_mouseDown = false;
        m_mouseWasDown = false;
        m_mouseInside = false;
        m_actionCommand = null;
        m_actionListener = null;
        depressable = true;
        actualState = 0;
        drawState = 0;
        if((m_imageUp = loadImageResource(image)) != null && !waitForImage(m_imageUp))
        {
            System.out.println("Cant load image " + image);
            m_imageUp = null;
        }
        if(m_imageUp == null)
            System.out.println("m_image is NULL !!!");
        m_imageDown = m_imageUp;
        setUpImage(m_imageUp);
        setDownImage(m_imageDown);
        enableEvents(16L);
    }

    public ImageButton(Image image)
    {
        m_mouseDown = false;
        m_mouseWasDown = false;
        m_mouseInside = false;
        m_actionCommand = null;
        m_actionListener = null;
        depressable = true;
        actualState = 0;
        drawState = 0;
        setUpImage(image);
        setDownImage(image);
        enableEvents(16L);
    }

    public ImageButton(Image up, Image down)
    {
        m_mouseDown = false;
        m_mouseWasDown = false;
        m_mouseInside = false;
        m_actionCommand = null;
        m_actionListener = null;
        depressable = true;
        actualState = 0;
        drawState = 0;
        setUpImage(up);
        setDownImage(down);
        enableEvents(16L);
    }

    public ImageButton()
    {
        m_mouseDown = false;
        m_mouseWasDown = false;
        m_mouseInside = false;
        m_actionCommand = null;
        m_actionListener = null;
        depressable = true;
        actualState = 0;
        drawState = 0;
        setUpImage(null);
        setDownImage(null);
        enableEvents(16L);
    }

    public void setDepressable(boolean b)
    {
        depressable = b;
    }

    public boolean isDepressable()
    {
        return depressable;
    }

    private Image loadImageResource(String name)
    {
        InputStream is = getClass().getResourceAsStream(name);
        if(is == null)
            System.out.println("No resource found");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            int i;
            while((i = is.read()) >= 0) 
                baos.write(i);
            Image img1 = getToolkit().createImage(baos.toByteArray());
            return img1;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private boolean waitForImage(Image image)
    {
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(image, 0);
        try
        {
            tracker.waitForAll();
            if(tracker.isErrorAny())
                return false;
        }
        catch(InterruptedException e)
        {
            return false;
        }
        return true;
    }

    public void setUpImage(Image image)
    {
        m_imageUp = image;
        m_inactive = null;
        if(m_imageUp != null)
            m_sizes = new Dimension(m_imageUp.getWidth(this) + 6, m_imageUp.getHeight(this) + 6);
        else
            m_sizes = new Dimension(10, 10);
        repaint();
    }

    public void setDownImage(Image image)
    {
        m_imageDown = image;
        repaint();
    }

    private Image getInactiveImage()
    {
        if(m_inactive == null)
            m_inactive = emboss();
        return m_inactive;
    }

    public Image getUpImage()
    {
        return m_imageUp;
    }

    public Image getDownImage()
    {
        return m_imageDown;
    }

    public void setActionCommand(String actionCommand)
    {
        m_actionCommand = actionCommand;
    }

    public String getActionCommand()
    {
        return m_actionCommand;
    }

    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(m_sizes);
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        Image image;
        if(isEnabled())
        {
            if(drawState == 1)
            {
                if((image = getDownImage()) != null)
                {
                    g.setColor(getBackground());
                    g.drawImage(image, 3, 3, this);
                    g.draw3DRect(0, 0, getSize().width - 1, getSize().height - 1, false);
                } else
                {
                    System.out.println("Image is null");
                }
            } else
            if((image = getUpImage()) != null)
            {
                g.setColor(getBackground());
                g.drawImage(image, 4, 4, this);
                g.draw3DRect(0, 0, getSize().width - 1, getSize().height - 1, true);
            } else
            {
                System.out.println("Image is null");
            }
        } else
        if((image = getInactiveImage()) != null)
            g.drawImage(image, 3, 3, this);
    }

    public synchronized void addActionListener(ActionListener l)
    {
        m_actionListener = AWTEventMulticaster.add(m_actionListener, l);
    }

    public synchronized void removeActionListener(ActionListener l)
    {
        m_actionListener = AWTEventMulticaster.remove(m_actionListener, l);
    }

    protected void checkFireAction(MouseEvent e)
    {
        if(m_actionListener == null)
            return;
        if(!isEnabled())
            return;
        if(e.isConsumed())
            return;
        if(!contains(e.getPoint()))
        {
            return;
        } else
        {
            m_actionListener.actionPerformed(new ActionEvent(this, 1001, m_actionCommand, e.getModifiers()));
            return;
        }
    }

    public void setButtonDown(boolean down)
    {
        if(down)
            actualState = drawState = 1;
        else
            actualState = drawState = 0;
        repaint();
    }

    public Boolean isDown()
    {
        if(actualState == 1)
            return new Boolean(true);
        else
            return new Boolean(false);
    }

    public void processMouseEvent(MouseEvent e)
    {
        switch(e.getID())
        {
        case 503: 
        default:
            break;

        case 501: 
            if(actualState == 1)
                drawState = 0;
            else
                drawState = 1;
            repaint();
            break;

        case 502: 
            if(!getBounds().contains(e.getX() + getLocation().x, e.getY() + getLocation().y))
                return;
            if(depressable)
                actualState = drawState;
            else
                actualState = drawState = 0;
            checkFireAction(e);
            repaint();
            break;

        case 504: 
            repaint();
            break;

        case 505: 
            drawState = actualState;
            repaint();
            break;
        }
        super.processMouseEvent(e);
    }

    private Image emboss()
    {
        if(m_imageUp == null)
            return null;
        int wide = m_imageUp.getWidth(null);
        int high = m_imageUp.getHeight(null);
        int orig[] = new int[wide * high];
        int dest[] = new int[wide * high];
        try
        {
            PixelGrabber grab = new PixelGrabber(m_imageUp, 0, 0, wide, high, orig, 0, wide);
            grab.grabPixels();
        }
        catch(InterruptedException e) { }
        int back = getBackground().getRGB() & 0xffffff;
        int dark = getBackground().darker().getRGB() & 0xffffff;
        int lite = getBackground().brighter().getRGB() & 0xffffff;
        for(int y = 0; y < high; y++)
        {
            int next = (y + 1) * wide + 1;
            boolean tops = y < high - 1;
            for(int x = 0; x < wide;)
            {
                int indx = y * wide + x;
                int rgba = orig[indx];
                int alph = rgba & 0xff000000;
                boolean fill = (((rgba & 0xff0000) >> 16) + ((rgba & 0xff00) >> 8) + (rgba & 0xff)) / 3 < 180;
                if(tops && x < wide - 1)
                    if(fill)
                        dest[next] = alph | lite;
                    else
                        dest[next] = alph | back;
                if(fill && alph != 0)
                    dest[indx] = alph | dark;
                x++;
                next++;
            }

        }

        return createImage(new MemoryImageSource(wide, high, ColorModel.getRGBdefault(), dest, 0, wide));
    }

    private Image m_imageUp;
    private Image m_imageDown;
    private Image m_inactive;
    private Dimension m_sizes;
    private boolean m_mouseDown;
    private boolean m_mouseWasDown;
    private boolean m_mouseInside;
    private String m_actionCommand;
    private ActionListener m_actionListener;
    private boolean depressable;
    private static final int BUTTON_UP = 0;
    private static final int BUTTON_DOWN = 1;
    private int actualState;
    private int drawState;

}
