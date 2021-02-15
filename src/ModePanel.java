// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ModePanel.java

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintStream;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ModePanel extends JPanel
{

    ModePanel(String mode)
    {
        modeName = mode;
        initComponents();
    }

    public String getModeName()
    {
        return modeName;
    }

    public void setVoxelSize(int v)
    {
        voxelSize = v;
    }

    public String modeString(String m, String name)
    {
        return "mode." + m + "." + name;
    }

    public String toString()
    {
        return getModeName();
    }

    public boolean writeValues()
    {
        try
        {
            PropReader.setProperty(modeString(modeName, "defaultimage"), textDefImage.getText());
            PropReader.setProperty(modeString(modeName, "box.x"), String.valueOf((new Integer(textCropX.getText())).intValue()));
            PropReader.setProperty(modeString(modeName, "box.y"), String.valueOf((new Integer(textCropY.getText())).intValue()));
            PropReader.setProperty(modeString(modeName, "box.z"), String.valueOf((new Integer(textCropZ.getText())).intValue()));
            PropReader.setProperty(modeString(modeName, "box.width"), String.valueOf((new Integer(textCropWidth.getText())).intValue()));
            PropReader.setProperty(modeString(modeName, "box.depth"), String.valueOf((new Integer(textCropDepth.getText())).intValue()));
            PropReader.setProperty(modeString(modeName, "box.height"), String.valueOf((new Integer(textCropHeight.getText())).intValue()));
        }
        catch(Exception e)
        {
            FIVToolWindow.appendLog(1, "FIVPrefsDialog:writeValues: " + e.toString());
            return false;
        }
        if(checkTransverse.isSelected())
        {
            switch(comboTransverse.getSelectedIndex())
            {
            case 0: // '\0'
                PropReader.setProperty(modeString(modeName, "transverse.stack"), "on");
                PropReader.setProperty(modeString(modeName, "transverse.montage"), "off");
                break;

            case 1: // '\001'
                PropReader.setProperty(modeString(modeName, "transverse.stack"), "off");
                PropReader.setProperty(modeString(modeName, "transverse.montage"), "on");
                break;

            case 2: // '\002'
                PropReader.setProperty(modeString(modeName, "transverse.stack"), "on");
                PropReader.setProperty(modeString(modeName, "transverse.montage"), "on");
                break;
            }
        } else
        {
            PropReader.setProperty(modeString(modeName, "transverse.stack"), "off");
            PropReader.setProperty(modeString(modeName, "transverse.montage"), "off");
        }
        if(checkCoronal.isSelected())
        {
            switch(comboCoronal.getSelectedIndex())
            {
            case 0: // '\0'
                PropReader.setProperty(modeString(modeName, "coronal.stack"), "on");
                PropReader.setProperty(modeString(modeName, "coronal.montage"), "off");
                break;

            case 1: // '\001'
                PropReader.setProperty(modeString(modeName, "coronal.stack"), "off");
                PropReader.setProperty(modeString(modeName, "coronal.montage"), "on");
                break;

            case 2: // '\002'
                PropReader.setProperty(modeString(modeName, "coronal.stack"), "on");
                PropReader.setProperty(modeString(modeName, "coronal.montage"), "on");
                break;
            }
        } else
        {
            PropReader.setProperty(modeString(modeName, "coronal.stack"), "off");
            PropReader.setProperty(modeString(modeName, "coronal.montage"), "off");
        }
        if(checkSagittal.isSelected())
        {
            switch(comboSagittal.getSelectedIndex())
            {
            case 0: // '\0'
                PropReader.setProperty(modeString(modeName, "sagittal.stack"), "on");
                PropReader.setProperty(modeString(modeName, "sagittal.montage"), "off");
                break;

            case 1: // '\001'
                PropReader.setProperty(modeString(modeName, "sagittal.stack"), "off");
                PropReader.setProperty(modeString(modeName, "sagittal.montage"), "on");
                break;

            case 2: // '\002'
                PropReader.setProperty(modeString(modeName, "sagittal.stack"), "on");
                PropReader.setProperty(modeString(modeName, "sagittal.montage"), "on");
                break;
            }
        } else
        {
            PropReader.setProperty(modeString(modeName, "sagittal.stack"), "off");
            PropReader.setProperty(modeString(modeName, "sagittal.montage"), "off");
        }
        if(checkToolbar.isSelected())
            PropReader.setProperty(modeString(modeName, "notool"), "false");
        else
            PropReader.setProperty(modeString(modeName, "notool"), "true");
        String anatLow = textAnatRangeLow.getText().trim();
        String anatHigh = textAnatRangeHigh.getText().trim();
        String funcLow = textFuncRangeLow.getText().trim();
        String funcHigh = textFuncRangeHigh.getText().trim();
        try
        {
            if(!anatLow.equalsIgnoreCase("auto"))
                anatLow = String.valueOf((new Float(anatLow)).floatValue());
            if(!anatHigh.equalsIgnoreCase("auto"))
                anatHigh = String.valueOf((new Float(anatHigh)).floatValue());
            PropReader.setProperty(modeString(modeName, "anat.low"), anatLow);
            PropReader.setProperty(modeString(modeName, "anat.high"), anatHigh);
            PropReader.setProperty(modeString(modeName, "func.low"), String.valueOf((new Float(funcLow)).floatValue()));
            PropReader.setProperty(modeString(modeName, "func.high"), String.valueOf((new Float(funcHigh)).floatValue()));
        }
        catch(Exception e)
        {
            FIVToolWindow.appendLog(1, "FIVPrefsDialog:writeValues: " + e.toString());
            return false;
        }
        PropReader.setProperty(modeString(modeName, "func.colormap"), comboColormap.getSelectedItem().toString());
        if(comboFuncSign.getSelectedIndex() == 0)
            PropReader.setProperty(modeString(modeName, "func.sign"), "both");
        else
        if(comboFuncSign.getSelectedIndex() == 1)
            PropReader.setProperty(modeString(modeName, "func.sign"), "pos");
        else
        if(comboFuncSign.getSelectedIndex() == 2)
            PropReader.setProperty(modeString(modeName, "func.sign"), "neg");
        else
            PropReader.setProperty(modeString(modeName, "func.sign"), "none");
        String funcDisplay = comboFuncDisplay.getSelectedItem().toString();
        PropReader.setProperty(modeString(modeName, "func.opacity"), funcDisplay);
        String maskFile = textMask.getText();
        if(maskFile != null)
            PropReader.setProperty(modeString(modeName, "func.mask"), maskFile);
        else
            PropReader.setProperty(modeString(modeName, "func.mask"), "none");
        if(comboScaleStack.getSelectedIndex() == 0)
            PropReader.setProperty(modeString(modeName, "scale.stack"), "1");
        else
        if(comboScaleStack.getSelectedIndex() == 1)
            PropReader.setProperty(modeString(modeName, "scale.stack"), "2");
        else
        if(comboScaleStack.getSelectedIndex() == 2)
            PropReader.setProperty(modeString(modeName, "scale.stack"), "3");
        else
        if(comboScaleStack.getSelectedIndex() == 3)
            PropReader.setProperty(modeString(modeName, "scale.stack"), "4");
        else
        if(comboScaleStack.getSelectedIndex() == 4)
            PropReader.setProperty(modeString(modeName, "scale.stack"), "5");
        else
        if(comboScaleStack.getSelectedIndex() == 5)
            PropReader.setProperty(modeString(modeName, "scale.stack"), "6");
        else
            PropReader.setProperty(modeString(modeName, "scale.stack"), "2");
        if(comboScaleMontage.getSelectedIndex() == 0)
            PropReader.setProperty(modeString(modeName, "scale.montage"), "1");
        else
        if(comboScaleMontage.getSelectedIndex() == 1)
            PropReader.setProperty(modeString(modeName, "scale.montage"), "2");
        else
        if(comboScaleMontage.getSelectedIndex() == 2)
            PropReader.setProperty(modeString(modeName, "scale.montage"), "3");
        else
        if(comboScaleMontage.getSelectedIndex() == 3)
            PropReader.setProperty(modeString(modeName, "scale.montage"), "4");
        else
        if(comboScaleMontage.getSelectedIndex() == 4)
            PropReader.setProperty(modeString(modeName, "scale.montage"), "5");
        else
        if(comboScaleMontage.getSelectedIndex() == 5)
            PropReader.setProperty(modeString(modeName, "scale.montage"), "6");
        else
            PropReader.setProperty(modeString(modeName, "scale.montage"), "2");
        return true;
    }

    public void setInitialParams()
    {
        setInitialParams(modeName);
    }

    public void setInitialParams(String m)
    {
        try
        {
            textDefImage.setText(PropReader.getString(modeString(m, "defaultimage")));
            textCropX.setText(PropReader.getString(modeString(m, "box.x")));
            textCropY.setText(PropReader.getString(modeString(m, "box.y")));
            textCropZ.setText(PropReader.getString(modeString(m, "box.z")));
            textCropWidth.setText(PropReader.getString(modeString(m, "box.width")));
            textCropDepth.setText(PropReader.getString(modeString(m, "box.depth")));
            textCropHeight.setText(PropReader.getString(modeString(m, "box.height")));
            String scale = PropReader.getString(modeString(m, "scale.stack"));
            if(scale == null)
                if(m.equalsIgnoreCase("111"))
                    scale = "2";
                else
                if(m.equalsIgnoreCase("222"))
                    scale = "2";
                else
                if(m.equalsIgnoreCase("333"))
                    scale = "4";
                else
                    scale = "2";
            if(scale.equalsIgnoreCase("1"))
                comboScaleStack.setSelectedIndex(0);
            else
            if(scale.equalsIgnoreCase("2"))
                comboScaleStack.setSelectedIndex(1);
            else
            if(scale.equalsIgnoreCase("3"))
                comboScaleStack.setSelectedIndex(2);
            else
            if(scale.equalsIgnoreCase("4"))
                comboScaleStack.setSelectedIndex(3);
            else
            if(scale.equalsIgnoreCase("5"))
                comboScaleStack.setSelectedIndex(4);
            else
            if(scale.equalsIgnoreCase("6"))
            {
                comboScaleStack.setSelectedIndex(5);
            } else
            {
                FIVToolWindow.appendLog(1, "FIVPrefsDialog: Unknown scale: " + scale);
                comboScaleStack.setSelectedIndex(1);
            }
            scale = PropReader.getString(modeString(m, "scale.montage"));
            if(scale == null)
            {
                System.out.println("m: " + m);
                if(m.equalsIgnoreCase("111"))
                    scale = "1";
                else
                if(m.equalsIgnoreCase("222"))
                    scale = "1";
                else
                if(m.equalsIgnoreCase("333"))
                    scale = "2";
                else
                    scale = "2";
            }
            if(scale.equalsIgnoreCase("1"))
                comboScaleMontage.setSelectedIndex(0);
            else
            if(scale.equalsIgnoreCase("2"))
                comboScaleMontage.setSelectedIndex(1);
            else
            if(scale.equalsIgnoreCase("3"))
                comboScaleMontage.setSelectedIndex(2);
            else
            if(scale.equalsIgnoreCase("4"))
                comboScaleMontage.setSelectedIndex(3);
            else
            if(scale.equalsIgnoreCase("5"))
                comboScaleMontage.setSelectedIndex(4);
            else
            if(scale.equalsIgnoreCase("6"))
            {
                comboScaleMontage.setSelectedIndex(5);
            } else
            {
                FIVToolWindow.appendLog(1, "FIVPrefsDialog: Unknown scale: " + scale);
                comboScaleMontage.setSelectedIndex(1);
            }
            if(PropReader.getString(modeString(m, "transverse.stack")).equalsIgnoreCase("on") || PropReader.getString(modeString(m, "transverse.montage")).equalsIgnoreCase("on"))
            {
                checkTransverse.setSelected(true);
                comboTransverse.enable(true);
                if(PropReader.getString(modeString(m, "transverse.stack")).equalsIgnoreCase("on") && PropReader.getString(modeString(m, "transverse.montage")).equalsIgnoreCase("on"))
                    comboTransverse.setSelectedIndex(2);
                else
                if(PropReader.getString(modeString(m, "transverse.stack")).equalsIgnoreCase("on"))
                    comboTransverse.setSelectedIndex(0);
                else
                if(PropReader.getString(modeString(m, "transverse.montage")).equalsIgnoreCase("on"))
                    comboTransverse.setSelectedIndex(1);
            } else
            {
                checkTransverse.setSelected(false);
                comboTransverse.enable(false);
            }
            if(PropReader.getString(modeString(m, "coronal.stack")).equalsIgnoreCase("on") || PropReader.getString(modeString(m, "coronal.montage")).equalsIgnoreCase("on"))
            {
                checkCoronal.setSelected(true);
                comboCoronal.enable(true);
                if(PropReader.getString(modeString(m, "coronal.stack")).equalsIgnoreCase("on") && PropReader.getString(modeString(m, "coronal.montage")).equalsIgnoreCase("on"))
                    comboCoronal.setSelectedIndex(2);
                else
                if(PropReader.getString(modeString(m, "coronal.stack")).equalsIgnoreCase("on"))
                    comboCoronal.setSelectedIndex(0);
                else
                if(PropReader.getString(modeString(m, "coronal.montage")).equalsIgnoreCase("on"))
                    comboCoronal.setSelectedIndex(1);
            } else
            {
                checkCoronal.setSelected(false);
                comboCoronal.enable(false);
            }
            if(PropReader.getString(modeString(m, "sagittal.stack")).equalsIgnoreCase("on") || PropReader.getString(modeString(m, "sagittal.montage")).equalsIgnoreCase("on"))
            {
                checkSagittal.setSelected(true);
                comboSagittal.enable(true);
                if(PropReader.getString(modeString(m, "sagittal.stack")).equalsIgnoreCase("on") && PropReader.getString(modeString(m, "sagittal.montage")).equalsIgnoreCase("on"))
                    comboSagittal.setSelectedIndex(2);
                else
                if(PropReader.getString(modeString(m, "sagittal.stack")).equalsIgnoreCase("on"))
                    comboSagittal.setSelectedIndex(0);
                else
                if(PropReader.getString(modeString(m, "sagittal.montage")).equalsIgnoreCase("on"))
                    comboSagittal.setSelectedIndex(1);
            } else
            {
                checkSagittal.setSelected(false);
                comboSagittal.enable(false);
            }
            if(!PropReader.getBoolean(modeString(m, "notool"), false))
                checkToolbar.setSelected(true);
            else
                checkToolbar.setSelected(false);
            String anatLow = PropReader.getString(modeString(m, "anat.low"));
            String anatHigh = PropReader.getString(modeString(m, "anat.high"));
            if(anatLow.equalsIgnoreCase("auto"))
            {
                checkAnatRange.setSelected(false);
                textAnatRangeLow.setText("auto");
                textAnatRangeHigh.setText("auto");
                textAnatRangeLow.setEnabled(false);
                textAnatRangeHigh.setEnabled(false);
            } else
            {
                checkAnatRange.setSelected(true);
                textAnatRangeLow.setText(anatLow);
                textAnatRangeHigh.setText(anatHigh);
                textAnatRangeLow.setEnabled(true);
                textAnatRangeHigh.setEnabled(true);
            }
            String funcLow = PropReader.getString(modeString(m, "func.low"));
            String funcHigh = PropReader.getString(modeString(m, "func.high"));
            textFuncRangeLow.setText(funcLow);
            textFuncRangeHigh.setText(funcHigh);
            String funcSign = PropReader.getString(modeString(m, "func.sign"));
            if(funcSign.equalsIgnoreCase("both"))
                comboFuncSign.setSelectedIndex(0);
            else
            if(funcSign.equalsIgnoreCase("pos"))
                comboFuncSign.setSelectedIndex(1);
            else
            if(funcSign.equalsIgnoreCase("neg"))
                comboFuncSign.setSelectedIndex(2);
            else
            if(funcSign.equalsIgnoreCase("none"))
            {
                comboFuncSign.setSelectedIndex(3);
            } else
            {
                FIVToolWindow.appendLog(1, "FIVPrefsDialog: Unknown activation sign: " + funcSign);
                comboFuncSign.setSelectedIndex(0);
            }
            String colormap = PropReader.getString(modeString(m, "func.colormap"));
            if(colormap.equalsIgnoreCase("default"))
                comboColormap.setSelectedIndex(0);
            else
            if(colormap.equalsIgnoreCase("flip"))
            {
                comboColormap.setSelectedIndex(1);
            } else
            {
                FIVToolWindow.appendLog(1, "FIVPrefsDialog: Unknown colormap: " + colormap);
                comboFuncSign.setSelectedIndex(0);
            }
            String funcOpac = PropReader.getString(modeString(m, "func.opacity"));
            if(funcOpac.equalsIgnoreCase("100"))
                comboFuncDisplay.setSelectedIndex(0);
            else
            if(funcOpac.equalsIgnoreCase("90"))
                comboFuncDisplay.setSelectedIndex(1);
            else
            if(funcOpac.equalsIgnoreCase("80"))
                comboFuncDisplay.setSelectedIndex(2);
            else
            if(funcOpac.equalsIgnoreCase("70"))
                comboFuncDisplay.setSelectedIndex(3);
            else
            if(funcOpac.equalsIgnoreCase("60"))
                comboFuncDisplay.setSelectedIndex(4);
            else
            if(funcOpac.equalsIgnoreCase("50"))
                comboFuncDisplay.setSelectedIndex(5);
            else
            if(funcOpac.equalsIgnoreCase("40"))
                comboFuncDisplay.setSelectedIndex(6);
            else
            if(funcOpac.equalsIgnoreCase("30"))
                comboFuncDisplay.setSelectedIndex(7);
            else
            if(funcOpac.equalsIgnoreCase("20"))
                comboFuncDisplay.setSelectedIndex(8);
            else
            if(funcOpac.equalsIgnoreCase("10"))
                comboFuncDisplay.setSelectedIndex(9);
            else
                comboFuncDisplay.setSelectedIndex(0);
            String mask = PropReader.getString(modeString(m, "func.mask"));
            if(mask == null || mask.equalsIgnoreCase("") || mask.equalsIgnoreCase("none"))
                textMask.setText("");
            else
                textMask.setText(mask);
            if(modeName.equals("111") || modeName.equals("222") || modeName.equals("333"))
            {
                textCropX.setEnabled(false);
                textCropY.setEnabled(false);
                textCropZ.setEnabled(false);
                textCropWidth.setEnabled(false);
                textCropDepth.setEnabled(false);
                textCropHeight.setEnabled(false);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            FIVToolWindow.appendLog(1, "The following mode has been corrupted: " + modeName);
            FIVToolWindow.appendLog(1, e.toString());
        }
    }

    private void initComponents()
    {
        panelAnatParameters = new JPanel();
        panelFuncParameters = new JPanel();
        panelStartupDisplays = new JPanel();
        comboFuncSign = new JComboBox();
        comboFuncDisplay = new JComboBox();
        comboScaleMontage = new JComboBox();
        comboScaleStack = new JComboBox();
        textFuncRangeLow = new JTextField();
        textFuncRangeHigh = new JTextField();
        checkAnatRange = new JCheckBox();
        textAnatRangeLow = new JTextField();
        textAnatRangeHigh = new JTextField();
        comboColormap = new JComboBox();
        checkTransverse = new JCheckBox();
        comboTransverse = new JComboBox(displayVals);
        checkCoronal = new JCheckBox();
        comboCoronal = new JComboBox(displayVals);
        checkSagittal = new JCheckBox();
        comboSagittal = new JComboBox(displayVals);
        checkToolbar = new JCheckBox();
        panelDimensions = new JPanel();
        panelCrop = new JPanel();
        labelCropX = new JLabel();
        textCropX = new JTextField();
        labelCropWidth = new JLabel();
        textCropWidth = new JTextField();
        labelCropY = new JLabel();
        textCropY = new JTextField();
        labelCropDepth = new JLabel();
        textCropDepth = new JTextField();
        labelCropZ = new JLabel();
        textCropZ = new JTextField();
        labelCropHeight = new JLabel();
        textCropHeight = new JTextField();
        panelDefImage = new JPanel();
        textDefImage = new JTextField();
        buttonDefImage = new JButton() {

            public Dimension getPreferredSize()
            {
                return new Dimension(24, 24);
            }

            public Dimension getMinimumSize()
            {
                return new Dimension(24, 24);
            }

            public Dimension getMaximumSize()
            {
                return new Dimension(24, 24);
            }

        }
;
        panelMask = new JPanel();
        textMask = new JTextField();
        buttonMask = new JButton() {

            public Dimension getPreferredSize()
            {
                return new Dimension(24, 24);
            }

            public Dimension getMinimumSize()
            {
                return new Dimension(24, 24);
            }

            public Dimension getMaximumSize()
            {
                return new Dimension(24, 24);
            }

        }
;
        setLayout(new GridBagLayout());
        setBackground(Color.lightGray);
        panelStartupDisplays.setLayout(new GridLayout(4, 2));
        panelStartupDisplays.setBorder(new TitledBorder(null, "Startup displays", 0, 0, new Font("Dialog", 0, 12), Color.black));
        panelStartupDisplays.setBackground(Color.lightGray);
        checkTransverse.setText("transverse");
        checkTransverse.setBackground(Color.lightGray);
        checkTransverse.setForeground(Color.black);
        checkTransverse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                checkTransverseActionPerformed(evt);
            }

        }
);
        panelStartupDisplays.add(checkTransverse);
        comboTransverse.setBackground(Color.lightGray);
        comboTransverse.setForeground(Color.black);
        comboTransverse.setPreferredSize(new Dimension(80, 26));
        comboTransverse.setMinimumSize(new Dimension(80, 26));
        comboTransverse.setMaximumSize(new Dimension(80, 26));
        comboTransverse.setEnabled(false);
        panelStartupDisplays.add(comboTransverse);
        checkCoronal.setText("coronal");
        checkCoronal.setForeground(Color.black);
        checkCoronal.setBackground(Color.lightGray);
        checkCoronal.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                checkCoronalActionPerformed(evt);
            }

        }
);
        panelStartupDisplays.add(checkCoronal);
        comboCoronal.setBackground(Color.lightGray);
        comboCoronal.setForeground(Color.black);
        comboCoronal.setPreferredSize(new Dimension(80, 26));
        comboCoronal.setMinimumSize(new Dimension(80, 26));
        comboCoronal.setMaximumSize(new Dimension(80, 26));
        comboCoronal.setEnabled(false);
        panelStartupDisplays.add(comboCoronal);
        checkSagittal.setText("sagittal");
        checkSagittal.setForeground(Color.black);
        checkSagittal.setBackground(Color.lightGray);
        checkSagittal.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                checkSagittalActionPerformed(evt);
            }

        }
);
        panelStartupDisplays.add(checkSagittal);
        comboSagittal.setBackground(Color.lightGray);
        comboSagittal.setForeground(Color.black);
        comboSagittal.setPreferredSize(new Dimension(80, 26));
        comboSagittal.setMinimumSize(new Dimension(80, 26));
        comboSagittal.setMaximumSize(new Dimension(80, 26));
        comboSagittal.setEnabled(false);
        panelStartupDisplays.add(comboSagittal);
        checkToolbar.setBackground(Color.lightGray);
        checkToolbar.setForeground(Color.black);
        checkToolbar.setText("Show toolbar");
        panelStartupDisplays.add(checkToolbar);
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.anchor = 11;
        gridBagConstraints1.insets = new Insets(10, 8, 10, 5);
        add(panelStartupDisplays, gridBagConstraints1);
        panelCrop.setLayout(new GridLayout(3, 4, 2, 0));
        panelCrop.setBackground(Color.lightGray);
        labelCropX.setText("x   ");
        labelCropX.setForeground(Color.black);
        labelCropX.setBackground(Color.lightGray);
        labelCropX.setHorizontalAlignment(0);
        panelCrop.add(labelCropX);
        panelCrop.add(textCropX);
        labelCropWidth.setText("width   ");
        labelCropWidth.setForeground(Color.black);
        labelCropWidth.setHorizontalAlignment(0);
        panelCrop.add(labelCropWidth);
        panelCrop.add(textCropWidth);
        labelCropY.setText("y   ");
        labelCropY.setForeground(Color.black);
        labelCropY.setHorizontalAlignment(0);
        panelCrop.add(labelCropY);
        panelCrop.add(textCropY);
        labelCropDepth.setText("depth   ");
        labelCropDepth.setForeground(Color.black);
        labelCropDepth.setHorizontalAlignment(0);
        panelCrop.add(labelCropDepth);
        panelCrop.add(textCropDepth);
        labelCropZ.setText("z   ");
        labelCropZ.setForeground(Color.black);
        labelCropZ.setHorizontalAlignment(0);
        panelCrop.add(labelCropZ);
        panelCrop.add(textCropZ);
        labelCropHeight.setText("height   ");
        labelCropHeight.setForeground(Color.black);
        labelCropHeight.setHorizontalAlignment(0);
        panelCrop.add(labelCropHeight);
        panelCrop.add(textCropHeight);
        panelDimensions.setBackground(Color.lightGray);
        panelDimensions.setLayout(new GridBagLayout());
        panelDimensions.setBorder(new TitledBorder(null, "Dimensions", 0, 0, new Font("Dialog", 0, 12), Color.black));
        JLabel label = new JLabel("Scaling");
        label.setForeground(Color.black);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 1;
        gridBagConstraints1.gridheight = 1;
        gridBagConstraints1.anchor = 17;
        gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
        panelDimensions.add(label, gridBagConstraints1);
        label = new JLabel("stack");
        label.setForeground(Color.black);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = 1;
        gridBagConstraints1.gridheight = 1;
        gridBagConstraints1.anchor = 17;
        gridBagConstraints1.insets = new Insets(0, 8, 0, 0);
        panelDimensions.add(label, gridBagConstraints1);
        comboScaleStack.setBackground(Color.lightGray);
        comboScaleStack.setForeground(Color.black);
        comboScaleStack.addItem(new String(" 1x "));
        comboScaleStack.addItem(new String(" 2x "));
        comboScaleStack.addItem(new String(" 3x "));
        comboScaleStack.addItem(new String(" 4x "));
        comboScaleStack.addItem(new String(" 5x "));
        comboScaleStack.addItem(new String(" 6x "));
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = 1;
        gridBagConstraints1.gridheight = 1;
        gridBagConstraints1.anchor = 17;
        gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
        panelDimensions.add(comboScaleStack, gridBagConstraints1);
        label = new JLabel("montage");
        label.setForeground(Color.black);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = 1;
        gridBagConstraints1.gridheight = 1;
        gridBagConstraints1.anchor = 17;
        gridBagConstraints1.insets = new Insets(0, 4, 0, 0);
        panelDimensions.add(label, gridBagConstraints1);
        comboScaleMontage.setBackground(Color.lightGray);
        comboScaleMontage.setForeground(Color.black);
        comboScaleMontage.addItem(new String(" 1x "));
        comboScaleMontage.addItem(new String(" 2x "));
        comboScaleMontage.addItem(new String(" 3x "));
        comboScaleMontage.addItem(new String(" 4x "));
        comboScaleMontage.addItem(new String(" 5x "));
        comboScaleMontage.addItem(new String(" 6x "));
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 3;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = 1;
        gridBagConstraints1.gridheight = 1;
        gridBagConstraints1.anchor = 17;
        gridBagConstraints1.insets = new Insets(0, 0, 0, 5);
        panelDimensions.add(comboScaleMontage, gridBagConstraints1);
        label = new JLabel("Cropping");
        label.setForeground(Color.black);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = 1;
        gridBagConstraints1.gridheight = 1;
        gridBagConstraints1.anchor = 17;
        gridBagConstraints1.insets = new Insets(10, 0, 0, 0);
        panelDimensions.add(label, gridBagConstraints1);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 4;
        gridBagConstraints1.gridheight = 1;
        gridBagConstraints1.anchor = 11;
        gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
        panelDimensions.add(panelCrop, gridBagConstraints1);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.anchor = 11;
        gridBagConstraints1.insets = new Insets(10, 5, 10, 8);
        add(panelDimensions, gridBagConstraints1);
        panelDefImage.setLayout(new GridBagLayout());
        panelDefImage.setBorder(new TitledBorder(null, "Default anatomic image", 0, 0, new Font("Dialog", 0, 12), Color.black));
        panelDefImage.setBackground(Color.lightGray);
        textDefImage.setEditable(true);
        textDefImage.setForeground(Color.black);
        textDefImage.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e)
            {
                int k = e.getKeyCode();
                if(k == 37 || k == 39 || k == 36 || k == 35)
                {
                    return;
                } else
                {
                    e.consume();
                    return;
                }
            }

            public void keyReleased(KeyEvent e)
            {
                int k = e.getKeyCode();
                if(k == 37 || k == 39 || k == 36 || k == 35)
                {
                    return;
                } else
                {
                    e.consume();
                    return;
                }
            }

            public void keyPressed(KeyEvent e)
            {
                int k = e.getKeyCode();
                if(k == 37 || k == 39 || k == 36 || k == 35)
                {
                    return;
                } else
                {
                    e.consume();
                    return;
                }
            }

        }
);
        textDefImage.setColumns(1);
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = 2;
        gridBagConstraints2.insets = new Insets(0, 2, 0, 2);
        gridBagConstraints2.weightx = 0.10000000000000001D;
        panelDefImage.add(textDefImage, gridBagConstraints2);
        buttonDefImage.setIcon(new ImageIcon(getClass().getResource("/images/folderAnat.gif")));
        buttonDefImage.setToolTipText("browse for image");
        buttonDefImage.setBackground(Color.lightGray);
        buttonDefImage.setForeground(Color.black);
        buttonDefImage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                buttonDefImageActionPerformed(evt);
            }

        }
);
        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.insets = new Insets(0, 2, 0, 4);
        gridBagConstraints2.anchor = 17;
        panelDefImage.add(buttonDefImage, gridBagConstraints2);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 4;
        gridBagConstraints1.fill = 2;
        gridBagConstraints1.insets = new Insets(10, 5, 10, 8);
        gridBagConstraints1.anchor = 11;
        gridBagConstraints1.weightx = 100D;
        add(panelDefImage, gridBagConstraints1);
        panelAnatParameters.setBorder(new TitledBorder(null, "Anatomic parameters", 0, 0, new Font("Dialog", 0, 12), Color.black));
        panelAnatParameters.setBackground(Color.lightGray);
        panelAnatParameters.setLayout(new GridBagLayout());
        panelAnatParameters.setAlignmentX(0.0F);
        panelAnatParameters.setAlignmentY(0.0F);
        checkAnatRange.setText("specify intensity range");
        checkAnatRange.setBackground(Color.lightGray);
        checkAnatRange.setForeground(Color.black);
        checkAnatRange.setSelected(false);
        checkAnatRange.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                if(checkAnatRange.isSelected())
                {
                    textAnatRangeLow.setEnabled(true);
                    textAnatRangeHigh.setEnabled(true);
                    textAnatRangeLow.setText("");
                    textAnatRangeHigh.setText("");
                    textAnatRangeLow.requestFocus();
                } else
                {
                    textAnatRangeLow.setEnabled(false);
                    textAnatRangeHigh.setEnabled(false);
                    textAnatRangeLow.setText("auto");
                    textAnatRangeHigh.setText("auto");
                }
            }

        }
);
        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.gridwidth = 4;
        gridBagConstraints2.gridheight = 1;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints2.weightx = 100D;
        gridBagConstraints2.weighty = 100D;
        gridBagConstraints2.anchor = 17;
        panelAnatParameters.add(checkAnatRange, gridBagConstraints2);
        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = 1;
        gridBagConstraints2.gridheight = 1;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints2.weightx = 100D;
        gridBagConstraints2.weighty = 100D;
        gridBagConstraints2.anchor = 10;
        label = new JLabel("low");
        label.setForeground(Color.black);
        panelAnatParameters.add(label, gridBagConstraints2);
        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = 1;
        gridBagConstraints2.gridheight = 1;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints2.weightx = 100D;
        gridBagConstraints2.weighty = 100D;
        gridBagConstraints2.anchor = 10;
        label = new JLabel("high");
        label.setForeground(Color.black);
        panelAnatParameters.add(label, gridBagConstraints2);
        textAnatRangeLow.setColumns(5);
        textAnatRangeLow.setForeground(Color.black);
        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.gridwidth = 1;
        gridBagConstraints2.gridheight = 1;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints2.weightx = 100D;
        gridBagConstraints2.weighty = 100D;
        gridBagConstraints2.anchor = 17;
        panelAnatParameters.add(textAnatRangeLow, gridBagConstraints2);
        textAnatRangeHigh.setColumns(5);
        textAnatRangeHigh.setForeground(Color.black);
        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.gridwidth = 1;
        gridBagConstraints2.gridheight = 1;
        gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints2.weightx = 0.0D;
        gridBagConstraints2.weighty = 0.0D;
        gridBagConstraints2.anchor = 17;
        panelAnatParameters.add(textAnatRangeHigh, gridBagConstraints2);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.fill = 1;
        gridBagConstraints1.insets = new Insets(10, 8, 10, 5);
        gridBagConstraints1.anchor = 11;
        gridBagConstraints1.weightx = 100D;
        add(panelAnatParameters, gridBagConstraints1);
        panelFuncParameters.setBorder(new TitledBorder(null, "Functional parameters", 0, 0, new Font("Dialog", 0, 12), Color.black));
        panelFuncParameters.setBackground(Color.lightGray);
        GridLayout gl = new GridLayout(6, 2);
        gl.setHgap(2);
        panelFuncParameters.setLayout(gl);
        panelFuncParameters.setAlignmentX(0.0F);
        panelFuncParameters.setAlignmentY(0.0F);
        label = new JLabel("threshold low");
        label.setForeground(Color.black);
        panelFuncParameters.add(label, gridBagConstraints2);
        textFuncRangeLow.setColumns(5);
        textFuncRangeLow.setForeground(Color.black);
        panelFuncParameters.add(textFuncRangeLow);
        label = new JLabel("threshold high");
        label.setForeground(Color.black);
        panelFuncParameters.add(label, gridBagConstraints2);
        textFuncRangeHigh.setColumns(5);
        textFuncRangeHigh.setForeground(Color.black);
        panelFuncParameters.add(textFuncRangeHigh);
        label = new JLabel("activation sign");
        label.setForeground(Color.black);
        panelFuncParameters.add(label, gridBagConstraints2);
        comboFuncSign.addItem(new String("+/-"));
        comboFuncSign.addItem(new String(" + "));
        comboFuncSign.addItem(new String(" - "));
        comboFuncSign.addItem(new String("none"));
        panelFuncParameters.add(comboFuncSign);
        label = new JLabel("colormap");
        label.setForeground(Color.black);
        panelFuncParameters.add(label);
        comboColormap.addItem(new String("default"));
        comboColormap.addItem(new String("flip"));
        panelFuncParameters.add(comboColormap);
        label = new JLabel("opacity %");
        label.setForeground(Color.black);
        panelFuncParameters.add(label);
        comboFuncDisplay.addItem(new String("100"));
        comboFuncDisplay.addItem(new String("90"));
        comboFuncDisplay.addItem(new String("80"));
        comboFuncDisplay.addItem(new String("70"));
        comboFuncDisplay.addItem(new String("60"));
        comboFuncDisplay.addItem(new String("50"));
        comboFuncDisplay.addItem(new String("40"));
        comboFuncDisplay.addItem(new String("30"));
        comboFuncDisplay.addItem(new String("20"));
        comboFuncDisplay.addItem(new String("10"));
        panelFuncParameters.add(comboFuncDisplay);
        label = new JLabel("mask");
        label.setForeground(Color.black);
        panelFuncParameters.add(label);
        panelFuncParameters.add(panelMask);
        panelMask.setLayout(new BorderLayout());
        panelMask.add(textMask, "Center");
        panelMask.add(buttonMask, "East");
        buttonMask.setIcon(new ImageIcon(getClass().getResource("/images/folderAnat.gif")));
        buttonMask.setToolTipText("browse for image");
        buttonMask.setBackground(Color.lightGray);
        buttonMask.setForeground(Color.black);
        buttonMask.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                buttonMaskActionPerformed(evt);
            }

        }
);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.fill = 1;
        gridBagConstraints1.insets = new Insets(10, 5, 10, 8);
        gridBagConstraints1.anchor = 11;
        gridBagConstraints1.weightx = 100D;
        add(panelFuncParameters, gridBagConstraints1);
    }

    private void buttonDefImageActionPerformed(ActionEvent evt)
    {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == 0)
        {
            File file = chooser.getSelectedFile();
            textDefImage.setText(file.getAbsolutePath());
        }
    }

    private void buttonMaskActionPerformed(ActionEvent evt)
    {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == 0)
        {
            File file = chooser.getSelectedFile();
            textMask.setText(file.getAbsolutePath());
        }
    }

    private void checkSagittalActionPerformed(ActionEvent evt)
    {
        comboSagittal.enable(!comboSagittal.isEnabled());
        comboSagittal.repaint();
    }

    private void checkCoronalActionPerformed(ActionEvent evt)
    {
        comboCoronal.enable(!comboCoronal.isEnabled());
        comboCoronal.repaint();
    }

    private void checkTransverseActionPerformed(ActionEvent evt)
    {
        comboTransverse.enable(!comboTransverse.isEnabled());
        comboTransverse.repaint();
    }

    private JPanel panelDefImage;
    private JPanel panelAnatParameters;
    private JPanel panelFuncParameters;
    private JPanel panelDimensions;
    private JPanel panelCrop;
    private JPanel panelStartupDisplays;
    private JComboBox comboFuncSign;
    private JComboBox comboFuncDisplay;
    private JTextField textMask;
    private JPanel panelMask;
    private JButton buttonMask;
    private JTextField textFuncRangeLow;
    private JTextField textFuncRangeHigh;
    private JCheckBox checkAnatRange;
    private JTextField textAnatRangeLow;
    private JTextField textAnatRangeHigh;
    private JComboBox comboScaleStack;
    private JComboBox comboScaleMontage;
    private JComboBox comboColormap;
    private JCheckBox checkTransverse;
    private JComboBox comboTransverse;
    private JCheckBox checkCoronal;
    private JComboBox comboCoronal;
    private JCheckBox checkSagittal;
    private JComboBox comboSagittal;
    private JCheckBox checkToolbar;
    private JLabel labelCropX;
    private JTextField textCropX;
    private JLabel labelCropWidth;
    private JTextField textCropWidth;
    private JLabel labelCropY;
    private JTextField textCropY;
    private JLabel labelCropDepth;
    private JTextField textCropDepth;
    private JLabel labelCropZ;
    private JTextField textCropZ;
    private JLabel labelCropHeight;
    private JTextField textCropHeight;
    private JTextField textDefImage;
    private JButton buttonDefImage;
    String displayVals[] = {
        "stack", "montage", "both"
    };
    String modeName;
    int voxelSize;








}
