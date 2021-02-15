// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SurfaceConnectFrame.java

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SurfaceConnectFrame extends JFrame
    implements ActionListener
{

    public SurfaceConnectFrame(SurfaceConnectMaintainer manager)
    {
        startButton = new JButton("Start");
        hideButton = new JButton("Hide");
        waitingText = new JTextArea();
        connectedText = new JTextArea();
        listeningText = new JTextArea();
        hostLabel = new JLabel("host: ");
        portLabel = new JLabel("port: ");
        messageBox = new JTextArea();
        buttonPanel = new JPanel();
        messagePanel = new JPanel();
        startPanel = new JPanel();
        connectedPanel = new JPanel();
        waitingPanel = new JPanel();
        this.manager = manager;
        try
        {
            init();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void init()
        throws Exception
    {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.lightGray);
        setSize(new Dimension(360, 240));
        setTitle("FIV to Caret Connection");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

        }
);
        getContentPane().add(buttonPanel, "South");
        getContentPane().add(messagePanel, "Center");
        messagePanel.setBackground(Color.blue);
        messagePanel.setLayout(new BorderLayout());
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setLayout(new FlowLayout(2));
        GridBagLayout bag = new GridBagLayout();
        startPanel.setLayout(bag);
        startPanel.setBackground(Color.lightGray);
        listeningText.setText("FIV is now listening for connections from Caret. To complete the connection, select 'Connect to Tailarach' from Caret's 'Comm' menu and enter the following values in the dialogue box:\n");
        listeningText.setBackground(Color.lightGray);
        listeningText.setColumns(90);
        listeningText.setLineWrap(true);
        listeningText.setWrapStyleWord(true);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = 18;
        c.fill = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1.0D;
        startPanel.add(listeningText, c);
        hostLabel.setText("Host: " + manager.getSurfaceHost());
        hostLabel.setFont(new Font("Serif", 1, 12));
        hostLabel.setBackground(Color.lightGray);
        portLabel.setText("Port: " + manager.getSurfacePort());
        portLabel.setFont(new Font("Serif", 1, 12));
        portLabel.setBackground(Color.lightGray);
        JPanel holder = new JPanel(new GridLayout(2, 1));
        holder.setBackground(Color.lightGray);
        holder.add(hostLabel);
        holder.add(portLabel);
        c = new GridBagConstraints();
        c.anchor = 10;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        startPanel.add(holder, c);
        bag = new GridBagLayout();
        c = new GridBagConstraints();
        waitingText.setText("Press Start to begin.");
        waitingText.setBackground(Color.lightGray);
        waitingText.setColumns(12);
        waitingText.setLineWrap(true);
        waitingText.setWrapStyleWord(true);
        waitingText.setFont(new Font("Serif", 1, 12));
        c.anchor = 10;
        c.gridwidth = 0;
        c.weightx = 1.0D;
        waitingPanel.setLayout(bag);
        waitingPanel.setBackground(Color.lightGray);
        waitingPanel.add(waitingText, c);
        bag = new GridBagLayout();
        connectedPanel.setLayout(bag);
        connectedPanel.setBackground(Color.lightGray);
        connectedText.setText("Connection complete.  Receiving data from Caret:");
        connectedText.setColumns(35);
        connectedText.setFont(new Font("Serif", 1, 12));
        connectedText.setBackground(Color.lightGray);
        connectedText.setLineWrap(true);
        connectedText.setWrapStyleWord(true);
        c = new GridBagConstraints();
        c.anchor = 10;
        c.gridwidth = 0;
        c.gridheight = 1;
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        connectedPanel.add(connectedText, c);
        messageBox.setAutoscrolls(true);
        messageBox.setEditable(false);
        messageBox.setColumns(25);
        messageBox.setRows(6);
        JScrollPane areaScrollPane = new JScrollPane(messageBox);
        areaScrollPane.setVerticalScrollBarPolicy(22);
        areaScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        areaScrollPane.setBackground(Color.lightGray);
        c = new GridBagConstraints();
        c.anchor = 10;
        c.gridwidth = 0;
        c.gridheight = 3;
        c.weightx = 1.0D;
        c.weighty = 1.0D;
        connectedPanel.add(areaScrollPane, c);
        messagePanel.add(waitingPanel);
        buttonPanel.add(startButton);
        buttonPanel.add(hideButton);
        startButton.setFont(new Font("Serif", 0, 12));
        startButton.setBackground(Color.lightGray);
        startButton.addActionListener(this);
        hideButton.setFont(new Font("Serif", 0, 12));
        hideButton.setBackground(Color.lightGray);
        hideButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == startButton)
        {
            if(startButton.getText().equals("Start"))
                manager.startSurfaceConnection();
            else
                manager.stopSurfaceConnection();
        } else
        if(e.getSource() == hideButton)
            show(false);
    }

    public void connectionStarted()
    {
        messagePanel.removeAll();
        messagePanel.add(startPanel);
        startButton.setText("Stop");
        repaint();
    }

    public void connectionEstablished()
    {
        messagePanel.removeAll();
        messagePanel.add(connectedPanel);
        connectedPanel.repaint();
        repaint();
        show();
    }

    public void connectionClosed()
    {
        messagePanel.removeAll();
        messagePanel.add(waitingPanel);
        startButton.setText("Start");
        repaint();
    }

    public void appendMessage(String m)
    {
        messageBox.append(m);
        repaint();
    }

    private JButton startButton;
    private JButton hideButton;
    private JTextArea waitingText;
    private JTextArea connectedText;
    private JTextArea listeningText;
    private JLabel hostLabel;
    private JLabel portLabel;
    private JTextArea messageBox;
    private JPanel buttonPanel;
    private JPanel messagePanel;
    private JPanel startPanel;
    private JPanel connectedPanel;
    private JPanel waitingPanel;
    SurfaceConnectMaintainer manager;
}
