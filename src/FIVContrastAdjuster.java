// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVContrastAdjuster.java

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageProducer;
import java.net.URL;
import java.text.NumberFormat;
import javax.swing.*;

public class FIVContrastAdjuster extends JFrame
{

    public FIVContrastAdjuster(FIVManager manager)
    {
        this.manager = null;
        min = 0.0D;
        max = 0.0D;
        this.manager = manager;
        initComponents();
        setResizable(false);
        setIcon();
    }

    private void setIcon()
    {
        URL url = getClass().getResource("/images/contrastIcon.gif");
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

    public void setMinimum(double min)
    {
        this.min = min;
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setGroupingUsed(false);
        formatter.setMaximumFractionDigits(2);
        String smin = formatter.format(min);
        minField.setText(smin);
    }

    public void setMaximum(double max)
    {
        this.max = max;
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setGroupingUsed(false);
        formatter.setMaximumFractionDigits(2);
        String smax = formatter.format(max);
        maxField.setText(smax);
    }

    public void adjustMaximum()
    {
        try
        {
            max = (new Double(maxField.getText())).doubleValue();
        }
        catch(Exception e)
        {
            maxField.setText(Double.toString(max));
        }
        if(manager != null)
            manager.syncToIntensity(min, max);
    }

    public void adjustMinimum()
    {
        try
        {
            min = (new Double(minField.getText())).doubleValue();
        }
        catch(Exception e)
        {
            minField.setText(Double.toString(min));
        }
        if(manager != null)
            manager.syncToIntensity(min, max);
    }

    public void setLocationRelativeTo(Component c)
    {
        Point cloc = c.getLocation();
        Dimension thisDim = getSize();
        Dimension cdim = c.getSize();
        int x = (cloc.x + cdim.width) - thisDim.width;
        int y = cloc.y + cdim.height;
        setLocation(x, y);
    }

    private void initComponents()
    {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        minField = new JTextField();
        jLabel2 = new JLabel();
        maxField = new JTextField();
        jPanel3 = new JPanel();
        jLabel3 = new JLabel();
        jPanel5 = new JPanel();
        jPanel6 = new JPanel();
        jPanel7 = new JPanel();
        setBackground(Color.lightGray);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                exitForm(evt);
            }

        }
);
        jPanel1.setLayout(new GridLayout(2, 2, 2, 2));
        jLabel1.setText("minimum");
        jLabel1.setForeground(Color.black);
        jLabel1.setFont(new Font("Dialog", 0, 12));
        jPanel1.add(jLabel1);
        minField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                minFieldActionPerformed(evt);
            }

        }
);
        minField.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent evt)
            {
                minFieldFocusLost(evt);
            }

        }
);
        jPanel1.add(minField);
        jLabel2.setText("maximum");
        jLabel2.setForeground(Color.black);
        jLabel2.setFont(new Font("Dialog", 0, 12));
        jPanel1.add(jLabel2);
        maxField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                maxFieldActionPerformed(evt);
            }

        }
);
        maxField.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent evt)
            {
                maxFieldFocusLost(evt);
            }

        }
);
        jPanel1.add(maxField);
        getContentPane().add(jPanel1, "Center");
        jPanel3.setLayout(new FlowLayout(0));
        jLabel3.setText("Intensity range");
        jLabel3.setForeground(Color.black);
        jLabel3.setHorizontalAlignment(2);
        jLabel3.setFont(new Font("Dialog", 0, 12));
        jPanel3.add(jLabel3);
        getContentPane().add(jPanel3, "North");
        getContentPane().add(jPanel5, "South");
        getContentPane().add(jPanel6, "East");
        getContentPane().add(jPanel7, "West");
        pack();
    }

    private void maxFieldFocusLost(FocusEvent evt)
    {
        adjustMaximum();
    }

    private void minFieldFocusLost(FocusEvent evt)
    {
        adjustMinimum();
    }

    private void maxFieldActionPerformed(ActionEvent evt)
    {
        adjustMaximum();
    }

    private void minFieldActionPerformed(ActionEvent evt)
    {
        adjustMinimum();
    }

    private void exitForm(WindowEvent windowevent)
    {
    }

    public static void main(String args[])
    {
        (new FIVContrastAdjuster(null)).show();
    }

    FIVManager manager;
    double min;
    double max;
    private JPanel jPanel1;
    private JLabel jLabel1;
    private JTextField minField;
    private JLabel jLabel2;
    private JTextField maxField;
    private JPanel jPanel3;
    private JLabel jLabel3;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;





}
