// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVDualLogger.java

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageProducer;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
//import sun.net.smtp.SmtpClient;

public class FIVDualLogger extends JFrame
    implements Runnable, KeyListener
{
    class WinStream extends PrintStream
    {

        public void print(boolean x)
        {
            if(echo)
                pecho.print(x);
            view.append(x + "");
        }

        public void print(char x)
        {
            if(echo)
                pecho.print(x);
            view.append(x + "");
        }

        public void print(int x)
        {
            if(echo)
                pecho.print(x);
            view.append(x + "");
        }

        public void print(long x)
        {
            if(echo)
                pecho.print(x);
            view.append(x + "");
        }

        public void print(float x)
        {
            if(echo)
                pecho.print(x);
            view.append(x + "");
        }

        public void print(double x)
        {
            if(echo)
                pecho.print(x);
            view.append(x + "");
        }

        public void print(char x[])
        {
            if(echo)
                pecho.print(x);
            view.append(new String(x));
        }

        public void print(String x)
        {
            if(echo)
                pecho.print(x);
            view.append(x);
        }

        public void print(Object x)
        {
            if(echo)
                pecho.print(x);
            view.append(x.toString());
        }

        private void printError()
        {
            view.append("*** STANDARD ERROR *** ");
        }

        public void println()
        {
            if(echo)
                pecho.println();
            view.append("\n");
        }

        public void println(boolean x)
        {
            printError();
            print(x);
            println();
        }

        public void println(char x)
        {
            printError();
            print(x);
            println();
        }

        public void println(int x)
        {
            printError();
            print(x);
            println();
        }

        public void println(long x)
        {
            printError();
            print(x);
            println();
        }

        public void println(float x)
        {
            printError();
            print(x);
            println();
        }

        public void println(double x)
        {
            printError();
            print(x);
            println();
        }

        public void println(char x[])
        {
            printError();
            print(x);
            println();
        }

        public void println(String x)
        {
            printError();
            print(x);
            println();
        }

        public void println(Object x)
        {
            printError();
            print(x);
            println();
        }

        PrintStream pecho;
        boolean echo;
        JTextArea view;

        public WinStream(JTextArea view, PrintStream pecho)
        {
            super(pecho);
            echo = true;
            this.view = view;
            this.pecho = pecho;
        }
    }


    public FIVDualLogger()
    {
        logStatus = 0;
        infoLogArea = new JTextArea();
        errorLogArea = new JTextArea();
        ImageIcon errorIcon = new ImageIcon(getClass().getResource("/images/littleExclaim.gif"));
        ImageIcon infoIcon = new ImageIcon(getClass().getResource("/images/info.gif"));
        ImageIcon helpIcon = new ImageIcon(getClass().getResource("/images/question.gif"));
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", 1, 12));
        tabbedPane.setForeground(Color.black);
        Component tab1 = makeInfoPanel();
        tabbedPane.addTab("info", infoIcon, tab1, "Information log");
        tabbedPane.setSelectedIndex(0);
        Component tab2 = makeErrorPanel();
        tabbedPane.addTab("error", errorIcon, tab2, "Error log");
        Component tab3 = makeHelpPanel();
        tabbedPane.addTab("help", helpIcon, tab3, "Help page");
        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(tabbedPane);
        setBounds(650, 200, 400, 600);
        setTitle("FIV Log");
        final Frame thisFrame = this;
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                thisFrame.setVisible(false);
                toolBarButton.setButtonDown(false);
            }

        }
);
        setIcon();
        loadCanvasImages();
        System.setErr(new WinStream(errorLogArea, System.err));
        Thread timerThread = new Thread(this);
        timerThread.start();
    }

    public void attachButton(ImageButton b)
    {
        toolBarButton = b;
        final JFrame thisFrame = this;
        toolBarButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                if(logStatus == 1)
                    tabbedPane.setSelectedIndex(1);
                else
                    tabbedPane.setSelectedIndex(0);
                thisFrame.setVisible(!thisFrame.isVisible());
            }

        }
);
    }

    public void attachCanvas(Canvas c)
    {
        toolBarCanvas = c;
        toolBarCanvas.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e)
            {
                if(logStatus == 1)
                    tabbedPane.setSelectedIndex(1);
                else
                    tabbedPane.setSelectedIndex(0);
                setVisible(true);
                toolBarButton.setButtonDown(true);
            }

        }
);
    }

    public Image getCanvasImage()
    {
        if(logStatus == 3)
            return infoImage;
        if(logStatus == 1)
            return errorImage;
        else
            return null;
    }

    private void loadCanvasImages()
    {
        if((infoImage = loadImageResource("/images/info.gif")) != null && !waitForImage(infoImage))
        {
            System.out.println("FIVDualLogger: Can't load info image");
            infoImage = null;
        }
        if((errorImage = loadImageResource("/images/littleExclaim.gif")) != null && !waitForImage(errorImage))
        {
            System.out.println("FIVDualLogger: Can't load error image");
            errorImage = null;
        }
    }

    private void setIcon()
    {
        URL url = getClass().getResource("/images/brain.gif");
        if(url == null)
            return;
        Image img = null;
        try
        {
            img = createImage((ImageProducer)url.getContent());
        }
        catch(Exception e) { }
        if(img != null)
            setIconImage(img);
    }

    protected Component makeInfoPanel()
    {
        JPanel panel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton clearButton = new JButton("Clear");
        JButton closeButton = new JButton("Close");
        JPanel buttonPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(infoLogArea);
        panel.setForeground(Color.black);
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae)
            {
                saveInfo();
            }

        }
);
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae)
            {
                clearInfo();
            }

        }
);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae)
            {
                setVisible(false);
                toolBarButton.setButtonDown(false);
            }

        }
);
        closeButton.setForeground(Color.black);
        closeButton.setFont(new Font("SansSerif", 1, 12));
        clearButton.setForeground(Color.black);
        clearButton.setFont(new Font("SansSerif", 1, 12));
        saveButton.setForeground(Color.black);
        saveButton.setFont(new Font("SansSerif", 1, 12));
        infoLogArea.setBackground(Color.white);
        infoLogArea.setForeground(Color.black);
        infoLogArea.addKeyListener(this);
        scrollPane.setAutoscrolls(true);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, "Center");
        panel.add(buttonPanel, "South");
        return panel;
    }

    protected Component makeErrorPanel()
    {
        JPanel panel = new JPanel();
        JLabel commentLabel = new JLabel("Add comment: ");
        final JTextField commentField = new JTextField();
        JButton submitButton = new JButton("Submit");
        JButton closeButton = new JButton("Close");
        JPanel toolPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(errorLogArea);
        panel.setForeground(Color.black);
        commentLabel.setForeground(Color.black);
        commentField.setColumns(25);
        commentField.setBackground(Color.white);
        commentField.setForeground(Color.black);
        closeButton.setForeground(Color.black);
        closeButton.setFont(new Font("SansSerif", 1, 12));
        submitButton.setForeground(Color.black);
        submitButton.setFont(new Font("SansSerif", 1, 12));
        commentField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae)
            {
                addComment(commentField.getText());
                commentField.setText("");
            }

        }
);
        submitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae)
            {
                submitErrors();
            }

        }
);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae)
            {
                setVisible(false);
                toolBarButton.setButtonDown(false);
            }

        }
);
        errorLogArea.setEditable(false);
        errorLogArea.setBackground(Color.white);
        errorLogArea.setForeground(Color.black);
        scrollPane.setAutoscrolls(true);
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
        String datenewformat = formatter.format(today);
        errorLogArea.append("FIV Version 1.0 " + datenewformat + "\n");
        try
        {
            errorLogArea.append("User: " + System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getHostName() + "\n");
        }
        catch(IOException e) { }
        errorLogArea.append("-----------------------------------------------------------------------\n");
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(submitButton);
        buttonPanel.add(closeButton);
        toolPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        toolPanel.add(commentLabel, c);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 4;
        c.gridheight = 1;
        toolPanel.add(commentField, c);
        c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        toolPanel.add(buttonPanel, c);
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, "Center");
        panel.add(toolPanel, "South");
        return panel;
    }

    protected Component makeHelpPanel()
    {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        String s = null;
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(22);
        return editorScrollPane;
    }

    protected Component makeTextPanel(String text)
    {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(0);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    public void appendLog(int s, String message)
    {
        if(logStatus != 1)
        {
            logStatus = s;
            if(logStatus == 1 || logStatus == 3)
            {
                toolBarCanvas.repaint();
                synchronized(this)
                {
                    notify();
                }
            }
        }
        if(!message.endsWith("\n"))
            message = message + "\n";
        if(s == 1)
        {
            message = "*** FIV ERROR *** " + message;
            errorLogArea.append(message);
            System.out.print(message);
        } else
        if(s == 3)
        {
            infoLogArea.append(message);
        } else
        {
            errorLogArea.append(message);
            System.out.print(message);
        }
    }

    protected void clearInfo()
    {
        infoLogArea.setText("");
    }

    protected void addComment(String s)
    {
        errorLogArea.append("*** COMMENT *** " + s + "\n");
    }

    protected void close()
    {
        setVisible(false);
    }

    protected void saveInfo()
    {
        System.out.println("Saving info");
        JFileChooser fc = new JFileChooser();
        fc.setForeground(Color.black);
        int returnVal = fc.showSaveDialog(this);
        if(returnVal == 0)
            try
            {
                FileWriter out = new FileWriter(fc.getSelectedFile());
                out.write(infoLogArea.getText());
                out.close();
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
    }

    protected void submitErrors()
    {
/*    	
        System.out.println("Submitting errors");
        String server = "npg.wustl.edu";
        String recipient = "nrgtech";
        try
        {
            SmtpClient c = new SmtpClient(server);
            c.from("\"FIV User\"<" + System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getHostName() + ">");
            c.to(recipient);
            PrintStream p = c.startMessage();
            p.println("Subject: FIV Error Log");
            p.print(errorLogArea.getText());
            c.closeServer();
            errorLogArea.append("Log report submitted successfully to FIV administrator.\n");
        }
        catch(IOException e)
        {
            e.printStackTrace(System.err);
            errorLogArea.append("Log report failed to submit. Please email the FIV administrator (cnltech@iacmail.wustl.edu).\n");
        }
*/        
    }

    public void run()
    {
        while(true) 
            try
            {
                synchronized(this)
                {
                    wait();
                }
                Thread.sleep(1200L);
                logStatus = 0;
                toolBarCanvas.repaint();
            }
            catch(InterruptedException e) { }
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
            Image img1 = Toolkit.getDefaultToolkit().createImage(baos.toByteArray());
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

    public void keyPressed(KeyEvent keyevent)
    {
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    public void keyTyped(KeyEvent keyEvent)
    {
        System.out.print(keyEvent.getKeyChar());
    }

    public static final int GOOD = 0;
    public static final int ERROR = 1;
    public static final int ALERT = 2;
    public static final int INFO = 3;
    public static final int WARNING_DURATION = 1200;
    private int logStatus;
    private Image infoImage;
    private Image errorImage;
    ImageButton toolBarButton;
    Canvas toolBarCanvas;
    JTabbedPane tabbedPane;
    JTextArea infoLogArea;
    JTextArea errorLogArea;


}
