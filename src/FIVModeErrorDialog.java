// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVModeErrorDialog.java

import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;
import javax.swing.*;

public class FIVModeErrorDialog extends JDialog
{

    public FIVModeErrorDialog(Frame parent, boolean modal)
    {
        super(parent, modal);
        mode = null;
        initComponents();
        for(StringTokenizer tk = new StringTokenizer(PropReader.getString("modelist"), ","); tk.hasMoreTokens(); modeCombo.addItem(tk.nextToken()));
        setLocation(0, 0);
        setLocationRelativeTo(parent);
        pack();
    }

    public String getMode()
    {
        return mode;
    }

    private void initComponents()
    {
        modeCombo = new JComboBox();
        jPanel1 = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        jPanel2 = new JPanel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        getContentPane().setLayout(new GridBagLayout());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                closeDialog(evt);
            }

        }
);
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(modeCombo, gridBagConstraints2);
        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }

        }
);
        jPanel1.add(okButton);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }

        }
);
        jPanel1.add(cancelButton);
        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        getContentPane().add(jPanel1, gridBagConstraints2);
        jPanel2.setLayout(new GridBagLayout());
        jLabel4.setText("The current mode is invalid.");
        jLabel4.setForeground(Color.black);
        jLabel4.setFont(new Font("Dialog", 0, 12));
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new Insets(5, 5, 0, 5);
        jPanel2.add(jLabel4, gridBagConstraints1);
        jLabel5.setText("Select one from the list below.");
        jLabel5.setForeground(Color.black);
        jLabel5.setFont(new Font("Dialog", 0, 12));
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new Insets(0, 5, 5, 5);
        jPanel2.add(jLabel5, gridBagConstraints1);
        gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(jPanel2, gridBagConstraints2);
        pack();
    }

    private void cancelButtonActionPerformed(ActionEvent evt)
    {
        mode = null;
        dispose();
    }

    private void okButtonActionPerformed(ActionEvent evt)
    {
        mode = modeCombo.getSelectedItem().toString();
        dispose();
    }

    private void closeDialog(WindowEvent evt)
    {
        setVisible(false);
        dispose();
    }

    public static void main(String args[])
    {
        (new FIVModeErrorDialog(new JFrame(), true)).show();
    }

    String mode;
    private JComboBox modeCombo;
    private JPanel jPanel1;
    private JButton okButton;
    private JButton cancelButton;
    private JPanel jPanel2;
    private JLabel jLabel4;
    private JLabel jLabel5;



}
