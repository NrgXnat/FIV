// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVPrefsDialog.java

import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.*;

public class FIVPrefsDialog extends JDialog
{
    private class NewModeDialog extends JDialog
    {

        public String getName()
        {
            return textName.getText();
        }

        public String getVoxelSize()
        {
            return (String)comboVoxelSize.getSelectedItem();
        }

        public boolean getState()
        {
            if(textName.getText().equals(""))
                return false;
            else
                return state;
        }

        private JTextField textName;
        private JComboBox comboVoxelSize;
        boolean state;

        NewModeDialog()
        {
            state = false;
            setModal(true);
            setBackground(Color.lightGray);
            setResizable(false);
            setLocationRelativeTo(getParent());
            setLocation(250, 250);
            setTitle("New mode parameters");
            textName = new JTextField();
            comboVoxelSize = new JComboBox();
            JPanel mainPanel = new JPanel();
            JPanel buttonPanel = new JPanel();
            GridLayout gl = new GridLayout(2, 2);
            JButton buttonOk = new JButton("   OK   ");
            JButton buttonCancel = new JButton(" Cancel ");
            BorderLayout bl = new BorderLayout();
            buttonOk.setBackground(Color.lightGray);
            buttonOk.setForeground(Color.black);
            buttonOk.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    state = true;
                    setVisible(false);
                }

            }
);
            buttonCancel.setBackground(Color.lightGray);
            buttonCancel.setForeground(Color.black);
            buttonCancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    state = false;
                    setVisible(false);
                }

            }
);
            comboVoxelSize.setForeground(Color.black);
            comboVoxelSize.addItem("1");
            comboVoxelSize.addItem("2");
            comboVoxelSize.addItem("3");
            comboVoxelSize.setSelectedIndex(1);
            bl.setHgap(8);
            bl.setVgap(8);
            getContentPane().setBackground(Color.lightGray);
            getContentPane().setLayout(bl);
            JPanel p = new JPanel();
            p.setBackground(Color.lightGray);
            getContentPane().add(p, "West");
            p = new JPanel();
            p.setBackground(Color.lightGray);
            getContentPane().add(p, "East");
            p = new JPanel();
            p.setBackground(Color.lightGray);
            getContentPane().add(p, "North");
            getContentPane().add(mainPanel, "Center");
            getContentPane().add(buttonPanel, "South");
            gl.setHgap(4);
            gl.setVgap(4);
            mainPanel.setLayout(gl);
            mainPanel.setBackground(Color.lightGray);
            mainPanel.setForeground(Color.black);
            mainPanel.add(new JLabel("Name of mode "));
            mainPanel.add(textName);
            mainPanel.add(new JLabel("Voxel size "));
            mainPanel.add(comboVoxelSize);
            FlowLayout flow = new FlowLayout();
            flow.setAlignment(1);
            flow.setHgap(4);
            flow.setVgap(4);
            buttonPanel.setLayout(flow);
            buttonPanel.setForeground(Color.black);
            buttonPanel.setBackground(Color.lightGray);
            buttonPanel.add(buttonOk);
            buttonPanel.add(buttonCancel);
            pack();
        }
    }

    private class RemoveModeDialog extends JDialog
    {

        private JList list;
        private DefaultListModel listModel;
        private JButton removeButton;
        private JButton okButton;
        private JButton cancelButton;




        public RemoveModeDialog()
        {
            setModal(true);
            setBackground(Color.lightGray);
            setResizable(false);
            setLocationRelativeTo(getParent());
            setLocation(250, 250);
            setTitle("Remove mode");
            listModel = new DefaultListModel();
            for(int i = 0; i < vModes.size(); i++)
            {
                ModePanel m = (ModePanel)vModes.elementAt(i);
                String name = m.getModeName();
                if(!name.equals("111") && !name.equals("222") && !name.equals("333"))
                    listModel.addElement(m);
            }

            list = new JList(listModel);
            list.setForeground(Color.black);
            list.setSelectionMode(0);
            list.setSelectedIndex(0);
            JScrollPane listScrollPane = new JScrollPane(list);
            JButton removeButton = new JButton("Remove");
            removeButton.setForeground(Color.black);
            removeButton.setActionCommand("remove");
            removeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    try
                    {
                        listModel.remove(list.getSelectedIndex());
                    }
                    catch(Exception ne) { }
                }

            }
);
            JButton okButton = new JButton("   OK   ");
            okButton.setForeground(Color.black);
            okButton.setActionCommand("ok");
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    int nModes = vModes.size();
                    int nRemoved = 0;
                    for(int i = 0; i < nModes; i++)
                    {
                        ModePanel m = (ModePanel)vModes.elementAt(i - nRemoved);
                        String name = m.getModeName();
                        if(!listModel.contains(m) && !name.equals("111") && !name.equals("222") && !name.equals("333"))
                        {
                            tabbedPane.remove(m);
                            vModes.remove(m);
                            nRemoved++;
                        }
                    }

                    setVisible(false);
                    dispose();
                }

            }
);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setForeground(Color.black);
            cancelButton.setActionCommand("cancel");
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setVisible(false);
                    dispose();
                }

            }
);
            JPanel buttonPane = new JPanel();
            buttonPane.add(removeButton);
            buttonPane.add(new JPanel());
            buttonPane.add(okButton);
            buttonPane.add(cancelButton);
            Container contentPane = getContentPane();
            contentPane.add(listScrollPane, "Center");
            contentPane.add(buttonPane, "South");
            pack();
        }
    }


    public FIVPrefsDialog(Frame parent, boolean modal)
    {
        super(parent, modal);
        String modelist = PropReader.getString("modelist");
        if(modelist != null)
        {
            StringTokenizer st = new StringTokenizer(modelist, ",");
            vModes = new Vector();
            for(; st.hasMoreTokens(); vModes.addElement(new ModePanel(st.nextToken())));
            initComponents();
        } else
        {
            FIVToolWindow.appendLog(1, "No valid modes were found.  Your preferences file is absent or corrupt");
        }
    }

    private void removeCustomMode()
    {
        RemoveModeDialog d = new RemoveModeDialog();
        d.show();
    }

    private void addCustomMode()
    {
        NewModeDialog d = new NewModeDialog();
        d.show();
        if(d.getState())
        {
            int vs;
            try
            {
                vs = (new Integer(d.getVoxelSize())).intValue();
            }
            catch(Exception e)
            {
                FIVToolWindow.appendLog(1, "No voxel size selected. Mode will not be added.");
                return;
            }
            ModePanel mp = new ModePanel(d.getName());
            mp.setVoxelSize(vs);
            String s = "" + vs + vs + vs;
            mp.setInitialParams(s);
            String tabTitle = "   " + mp.getModeName() + "   ";
            tabbedPane.addTab(tabTitle, mp);
            tabbedPane.setSelectedComponent(mp);
            vModes.addElement(mp);
        }
    }

    private void initComponents()
    {
        tabPanel = new JPanel();
        tabbedPane = new JTabbedPane();
        buttonAddMode = new JButton();
        buttonRemoveMode = new JButton();
        bottomPanel = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        setTitle("Preferences Dialog");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                closeDialog(evt);
            }

        }
);
        tabPanel.setLayout(new BorderLayout());
        tabPanel.setBackground(Color.lightGray);
        tabPanel.setForeground(Color.black);
        tabbedPane.setBackground(Color.lightGray);
        tabbedPane.setForeground(Color.black);
        for(int i = 0; i < vModes.size(); i++)
        {
            ((ModePanel)vModes.elementAt(i)).setInitialParams();
            String tabTitle = "   " + ((ModePanel)vModes.elementAt(i)).getModeName() + "   ";
            tabbedPane.addTab(tabTitle, (JPanel)vModes.elementAt(i));
        }

        tabPanel.add(tabbedPane, "Center");
        getContentPane().add(tabPanel, "Center");
        bottomPanel.setBackground(Color.lightGray);
        okButton.setText("     OK     ");
        okButton.setForeground(Color.black);
        okButton.setBackground(Color.lightGray);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }

        }
);
        bottomPanel.add(okButton);
        cancelButton.setText("  Cancel  ");
        cancelButton.setForeground(Color.black);
        cancelButton.setBackground(Color.lightGray);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }

        }
);
        bottomPanel.add(cancelButton);
        buttonAddMode.setText("Add mode");
        buttonAddMode.setForeground(Color.black);
        buttonAddMode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                addCustomMode();
            }

        }
);
        buttonAddMode.setBackground(Color.lightGray);
        buttonAddMode.setBounds(10, 130, 191, 27);
        bottomPanel.add(buttonAddMode);
        buttonRemoveMode.setText("Remove mode");
        buttonRemoveMode.setForeground(Color.black);
        buttonRemoveMode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                removeCustomMode();
            }

        }
);
        buttonRemoveMode.setBackground(Color.lightGray);
        buttonRemoveMode.setBounds(10, 130, 191, 27);
        bottomPanel.add(buttonRemoveMode);
        getContentPane().add(bottomPanel, "South");
        setLocation(200, 200);
        pack();
    }

    private void cancelButtonActionPerformed(ActionEvent evt)
    {
        setVisible(false);
        dispose();
    }

    private void okButtonActionPerformed(ActionEvent evt)
    {
        String modelist = "";
        boolean goodVals = true;
        for(int i = 0; i < vModes.size(); i++)
        {
            goodVals = ((ModePanel)vModes.elementAt(i)).writeValues();
            if(!goodVals)
                break;
            String name = ((ModePanel)vModes.elementAt(i)).getModeName();
            modelist = modelist + name + ",";
        }

        if(!goodVals)
        {
            FIVToolWindow.appendLog(1, "One or more of the property values was invalid.");
            FIVToolWindow.appendLog(1, "Your preferences were not altered.");
        } else
        {
            PropReader.setProperty("modelist", modelist);
            PropReader.writeProperties();
            PropReader.load();
        }
        setVisible(false);
        dispose();
    }

    private void closeDialog(WindowEvent evt)
    {
        setVisible(false);
        dispose();
    }

    private Vector vModes;
    private JPanel tabPanel;
    private JTabbedPane tabbedPane;
    private JButton buttonAddMode;
    private JButton buttonRemoveMode;
    private JPanel bottomPanel;
    private JButton okButton;
    private JButton cancelButton;







}
