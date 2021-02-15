// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVWindow.java

import ij.WindowManager;
import ij.gui.ImageWindow;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageProducer;
import java.net.URL;

public class FIVWindow extends ImageWindow
    implements ActionListener
{

    public FIVWindow(FIVImage image, FIVCanvas canvas)
    {
        super(image.getBaseImagePlus(), canvas);
        popup = null;
        activationDisabled = false;
        tipText = "";
        loaded = false;
        this.canvas = canvas;
        this.image = image;
        imagePanel = new Panel();
        mapPanel = new BorderPanel();
        syncButton = new ImageButton("/images/lock.gif");
        popup = new PopupMessage(canvas);
        if(syncButton == null)
        {
            FIVToolWindow.appendLog(1, "There were problems creating the image window.");
            FIVToolWindow.appendLog(1, "(The syncButton was not created.)");
            FIVToolWindow.appendLog(1, "You might be out of memory -- try closing other windows.");
            return;
        } else
        {
            imagePanel.setLayout(new GridLayout(1, 1, 0, 0));
            imagePanel.add(canvas);
            mapPanel.setLayout(new GridLayout(1, 1));
            mapCanvas = new MapCanvas();
            mapCanvas.setSize(new Dimension(20, 100));
            mapPanel.add(mapCanvas);
            syncButton.setButtonDown(false);
            final FIVImage fimage = image;
            syncButton.addMouseListener(new MouseAdapter() {

                public void mouseReleased(MouseEvent evt)
                {
                    fimage.setSync(syncButton.isDown().booleanValue());
                }

            }
);
            eastPanel = new Panel();
            eastPanel.setLayout(new BorderLayout());
            addComponents();
            setLocation(FIVManager.getWindowPos(image));
            setIcon();
            setBackground(Color.lightGray);
            pack();
            loaded = true;
            setVisible(true);
            listenTo(this);
            return;
        }
    }

    public void show()
    {
        if(loaded)
            super.show();
    }

    public void setVisible(boolean v)
    {
        if(loaded)
            super.setVisible(v);
    }

    public void setLayout(LayoutManager l)
    {
        super.setLayout(new BorderLayout());
    }

    public void listenTo(Component c)
    {
        c.addKeyListener(image.getManager());
        if(c instanceof Container)
        {
            Component allComps[] = ((Container)c).getComponents();
            for(int i = 0; i < allComps.length; i++)
                listenTo(allComps[i]);

        }
    }

    public void addComponents()
    {
        if(image.getFunctionalImagePlus() != null)
            eastPanel.add(mapPanel, "Center");
        add(imagePanel, "Center");
        add(eastPanel, "East");
    }

    private void setIcon()
    {
        URL url = getClass().getResource("/images/brain.gif");
        if(url == null)
            return;
        java.awt.Image img = null;
        try
        {
            img = createImage((ImageProducer)url.getContent());
        }
        catch(Exception e) { }
        if(img != null)
            setIconImage(img);
    }

    public void setLock(boolean lock)
    {
        syncButton.setButtonDown(lock);
    }

    public void disableActivation(boolean b)
    {
        activationDisabled = b;
    }

    public void windowActivated(WindowEvent e)
    {
        if(!activationDisabled)
        {
            super.windowActivated(e);
            if(image != null)
                image.makeCurrent();
        } else
        {
            activationDisabled = false;
        }
    }

    public FIVWindow getWindow()
    {
        return this;
    }

    public MapCanvas getMapCanvas()
    {
        return mapCanvas;
    }

    public void setTipText(String t)
    {
        tipText = t;
        popup.setTip(tipText);
    }

    public void showPopup()
    {
        if(!popup.isVisible())
        {
            popup.setTip(tipText);
            popup.showTip();
        }
    }

    public void updateCrosshairs(Point p)
    {
        ((FIVCanvas)getCanvas()).setCrosshairPosition(p.x, p.y);
        ((FIVCanvas)getCanvas()).repaint();
    }

    public void setScaleBars(double min, double max, int sign)
    {
        if(sign == 4)
        {
            mapCanvas.hide();
            validate();
        } else
        {
            mapCanvas.show();
            validate();
            mapCanvas.setRange(min, max, sign);
        }
    }

    public void actionPerformed(ActionEvent actionevent)
    {
    }

    public String toString()
    {
        return new String(getName() + " bounds: " + getBounds());
    }

    public void windowClosing(WindowEvent e)
    {
        WindowManager.setCurrentWindow(this);
        super.windowClosing(e);
    }

    public boolean close()
    {
        super.close();
        image.windowClosing();
        return true;
    }

    public Insets getInsets()
    {
        Insets insets = super.getInsets();
        return new Insets(insets.top - 10, insets.left, insets.bottom, insets.right);
    }

    public void drawInfo(Graphics g1)
    {
    }

    protected Panel imagePanel;
    protected Panel eastPanel;
    protected Panel mapPanel;
    protected FIVImage image;
    protected FIVCanvas canvas;
    protected MapCanvas mapCanvas;
    protected ImageButton syncButton;
    protected PopupMessage popup;
    protected int slice;
    protected boolean activationDisabled;
    protected String tipText;
    protected boolean loaded;
}
