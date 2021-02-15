// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVFileChooser.java

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class FIVFileChooser extends JDialog
{
    private class ImageFilter extends FileFilter
    {

        public boolean accept(File f)
        {
            if(f.isDirectory())
                return true;
            String name = f.getName();
            if(name != null)
                return name.endsWith(".4dfp.img");
            else
                return false;
        }

        public String getDescription()
        {
            return "4dfp stacks";
        }

        private ImageFilter()
        {
        }

    }


    public FIVFileChooser(Frame parent, boolean modal)
    {
        super(parent, modal);
        chooser = null;
        state = 0;
        modeString = "default";
        noImage = false;
        initComponents();
        chooser = new JFileChooser();
        chooser.setDialogType(0);
        chooser.setFileFilter(new ImageFilter());
        chooser.setMultiSelectionEnabled(false);
        getContentPane().add(chooser, "Center");
        setLocation(250, 250);
        pack();
    }

    public void setCurrentDirectory(File dir)
    {
        chooser.setCurrentDirectory(dir);
    }

    public File getCurrentDirectory()
    {
        return chooser.getCurrentDirectory();
    }

    public int getExitState()
    {
        return state;
    }

    public String getMode()
    {
        return modeString;
    }

    public File getFile()
    {
        return chooser.getSelectedFile();
    }

    private void initComponents()
    {
        JPanel southPanel = new JPanel();
        JPanel modePanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JComboBox modeCombo = new JComboBox();
        JButton okButton = new JButton();
        JButton cancelButton = new JButton();
        JButton goButton = new JButton();
        JCheckBox noImageCheck = new JCheckBox();
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                closeDialog(evt);
            }

        }
);
        modeCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                modeString = ((JComboBox)e.getSource()).getSelectedItem().toString();
            }

        }
);
        modeCombo.addItem("current");
        modeCombo.addItem("default");
        try
        {
            for(StringTokenizer tk = new StringTokenizer(PropReader.getString("modelist"), ","); tk.hasMoreTokens(); modeCombo.addItem(tk.nextToken()));
        }
        catch(Exception e) { }
        noImageCheck.setSelected(false);
        noImageCheck.setText("use no image");
        noImageCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                noImage = !noImage;
            }

        }
);
        okButton.setText("Load only");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                if(noImage)
                    state = 3;
                else
                    state = 1;
                setVisible(false);
                dispose();
            }

        }
);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                state = 0;
                setVisible(false);
                dispose();
            }

        }
);
        goButton.setText("Load and go");
        goButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                if(noImage)
                    state = 3;
                else
                    state = 2;
                setVisible(false);
                dispose();
            }

        }
);
        FlowLayout l = new FlowLayout();
        l.setHgap(8);
        modePanel.setLayout(l);
        modePanel.add(new JLabel("Mode"));
        modePanel.add(modeCombo);
        modePanel.add(noImageCheck);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(goButton);
        buttonPanel.add(cancelButton);
        southPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 100D;
        gbc.insets = new Insets(0, 6, 0, 4);
        gbc.anchor = 17;
        southPanel.add(modePanel, gbc);
        gbc.gridx = 3;
        gbc.weightx = 100D;
        gbc.anchor = 13;
        gbc.insets = new Insets(0, 4, 0, 8);
        southPanel.add(buttonPanel, gbc);
        getContentPane().add(southPanel, "South");
        pack();
    }

    private void closeDialog(WindowEvent evt)
    {
        setVisible(false);
        dispose();
    }

    public static final int CANCEL = 0;
    public static final int LOAD = 1;
    public static final int GO = 2;
    public static final int BLANK = 3;
    JFileChooser chooser;
    int state;
    String modeString;
    boolean noImage;


}
