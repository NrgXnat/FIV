// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVToolWindow.java

import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageProducer;
import java.io.PrintStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Vector;

public class FIVToolWindow extends Frame
    implements MouseListener, KeyListener, ActionListener, FocusListener, ItemListener
{

    public FIVToolWindow(FIVManager m)
    {
        minThreshold = 0.0D;
        maxThreshold = 100D;
        xCoord = 0;
        yCoord = 0;
        zCoord = 0;
        manager = m;
        manager.setToolWindow(this);
        dualLogger = new FIVDualLogger();
        initComponentsMod();
        setBackground(Color.lightGray);
        setLocation(100, 100);
        setResizable(false);
        setIcon();
        setTitle("FuncIView");
        pack();
        structuralPopup = new PopupMessage(structuralFile);
        functionalPopup = new PopupMessage(functionalFile);
    }

    private void initComponentsMod()
    {
        topPanel = new Panel();
        bottomPanel = new Panel();
        goPanel = new Panel();
        buttonPanel = new Panel();
        messagePanel = new Panel();
        filePanel = new Panel();
        bCluster1 = new Panel();
        bCluster2 = new Panel();
        bCluster3 = new Panel();
        bCluster4 = new Panel();
        displayChoice = new Choice();
        viewChoice = new Choice();
        goButton = new Button();
        fileStructButton = new ImageButton("/images/folderAnat.gif");
        fileFuncButton = new ImageButton("/images/folderFunc.gif");
        printButton = new ImageButton("/images/print.gif");
        saveButton = new ImageButton("/images/save.gif");
        logButton = new ImageButton("/images/book.gif");
        syncButton = new ImageButton("/images/lock.gif");
        ijButton = new ImageButton("/images/microscope.gif");
        radButton = new ImageButton("/images/nonRad.gif", "/images/rad.gif");
        crosshairButton = new ImageButton("/images/crosshair.gif", "/images/noCrosshair.gif");
        arrowButton = new ImageButton("/images/arrow.gif");
        contrastButton = new ImageButton("/images/contrast.gif");
        scaleButton = new ImageButton("/images/magnify.gif");
        prefsButton = new ImageButton("/images/prefs.gif");
        surfaceButton = new ImageButton("/images/carrot.gif");
        registryButton = new ImageButton("/images/rocket.gif");
        activationPanel = new Panel();
        mapCanvas = new MapCanvas();
        minThresholdField = new TextField();
        maxThresholdField = new TextField();
        actChoice = new Choice();
        xfield = new TextField();
        yfield = new TextField();
        zfield = new TextField();
        intValueLabel = new SmallLabel(150, 20);
        structuralFile = new SmallLabel(180, 20);
        functionalFile = new SmallLabel(180, 20);
        setLayout(new GridBagLayout());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                exitForm(evt);
            }

        }
);
        goPanel.setLayout(new FlowLayout(0, 6, 0));
        goPanel.setFont(new Font("Serif", 1, 12));
        goPanel.setName("goPanel");
        goPanel.setForeground(Color.black);
        goButton.setFont(new Font("Serif", 0, 12));
        goButton.setLabel("      Go      ");
        goButton.setName("goButton");
        goButton.setBackground(Color.lightGray);
        goButton.setForeground(Color.black);
        goButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                goButtonClicked(evt);
            }

        }
);
        goPanel.add(goButton);
        viewChoice.setFont(new Font("Serif", 0, 12));
        viewChoice.setName("choiceView");
        viewChoice.setForeground(Color.black);
        viewChoice.addItem("transverse");
        viewChoice.addItem("coronal");
        viewChoice.addItem("sagittal");
        Panel holder = new Panel(new FlowLayout(1, 0, 0));
        holder.add(viewChoice);
        goPanel.add(holder);
        displayChoice.setFont(new Font("Serif", 0, 12));
        displayChoice.setName("choiceDisplay");
        displayChoice.setForeground(Color.black);
        displayChoice.addItem("montage");
        displayChoice.addItem("stack");
        goPanel.add(displayChoice);
        activationPanel.setLayout(new FlowLayout(0, 2, 0));
        activationPanel.setFont(new Font("Serif", 0, 12));
        activationPanel.setName("activationPanel");
        activationPanel.setBackground(Color.lightGray);
        activationPanel.setForeground(Color.black);
        minThresholdField.setBackground(Color.white);
        minThresholdField.setName("minThresholdField");
        minThresholdField.setFont(new Font("Serif", 0, 10));
        minThresholdField.setForeground(Color.black);
        minThresholdField.setText("       ");
        minThresholdField.addFocusListener(this);
        minThresholdField.addActionListener(this);
        maxThresholdField.setBackground(Color.white);
        maxThresholdField.setName("maxThresholdField");
        maxThresholdField.setFont(new Font("Serif", 0, 10));
        maxThresholdField.setForeground(Color.black);
        maxThresholdField.setText("       ");
        maxThresholdField.addFocusListener(this);
        maxThresholdField.addActionListener(this);
        actChoice.setFont(new Font("Serif", 0, 12));
        actChoice.setForeground(Color.black);
        actChoice.addItem("+ / -");
        actChoice.addItem("   +  ");
        actChoice.addItem("   -  ");
        actChoice.addItem("none");
        actChoice.addItemListener(this);
        activationPanel.add(new SmallLabel("  range:", 40, 20));
        holder = new Panel(new FlowLayout(1, 3, 0));
        holder.add(actChoice);
        activationPanel.add(holder);
        activationPanel.add(minThresholdField);
        activationPanel.add(new SmallLabel("  to", 30, 20));
        activationPanel.add(maxThresholdField);
        messagePanel.setLayout(new FlowLayout(0, 1, 1));
        messagePanel.setFont(new Font("Serif", 0, 12));
        messagePanel.setName("messagePanel");
        messagePanel.setBackground(Color.lightGray);
        messagePanel.setForeground(Color.black);
        xfield.setBackground(Color.white);
        xfield.setName("maxThresholdField");
        xfield.setFont(new Font("Serif", 0, 10));
        xfield.setForeground(Color.black);
        xfield.setColumns(5);
        xfield.addFocusListener(this);
        xfield.addActionListener(this);
        yfield.setBackground(Color.white);
        yfield.setName("maxThresholdField");
        yfield.setFont(new Font("Serif", 0, 10));
        yfield.setForeground(Color.black);
        yfield.setColumns(5);
        yfield.addFocusListener(this);
        yfield.addActionListener(this);
        zfield.setBackground(Color.white);
        zfield.setName("maxThresholdField");
        zfield.setFont(new Font("Serif", 0, 10));
        zfield.setForeground(Color.black);
        zfield.setColumns(5);
        zfield.addFocusListener(this);
        zfield.addActionListener(this);
        intValueLabel.setText(" =              ");
        messagePanel.add(new SmallLabel("   x", 20, 20));
        messagePanel.add(xfield);
        messagePanel.add(new SmallLabel("   y", 20, 20));
        messagePanel.add(yfield);
        messagePanel.add(new SmallLabel("   z", 20, 20));
        messagePanel.add(zfield);
        messagePanel.add(intValueLabel);
        bCluster1.setLayout(new FlowLayout(0, 1, 1));
        bCluster1.setFont(new Font("Serif", 0, 12));
        bCluster1.setName("bCluster1");
        bCluster1.setBackground(Color.lightGray);
        bCluster1.setForeground(Color.black);
        bCluster2.setLayout(new FlowLayout(0, 1, 1));
        bCluster2.setFont(new Font("Serif", 0, 12));
        bCluster2.setName("bCluster2");
        bCluster2.setBackground(Color.lightGray);
        bCluster2.setForeground(Color.black);
        bCluster3.setLayout(new FlowLayout(0, 1, 1));
        bCluster3.setFont(new Font("Serif", 0, 12));
        bCluster3.setName("bCluster3");
        bCluster3.setBackground(Color.lightGray);
        bCluster4.setForeground(Color.black);
        bCluster4.setLayout(new FlowLayout(0, 1, 1));
        bCluster4.setFont(new Font("Serif", 0, 12));
        bCluster4.setName("bCluster4");
        bCluster4.setBackground(Color.lightGray);
        bCluster4.setForeground(Color.black);
        saveButton.setFont(new Font("Serif", 0, 12));
        saveButton.setName("saveButton");
        saveButton.setBackground(new Color(131, 153, 177));
        saveButton.setForeground(Color.black);
        saveButton.setDepressable(false);
        saveButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                Vector v = new Vector();
                v.addElement(syncButton.isDown());
                manager.doCommand("Save", v);
            }

        }
);
        printButton.setFont(new Font("Serif", 0, 12));
        printButton.setName("printButton");
        printButton.setBackground(new Color(131, 153, 177));
        printButton.setForeground(Color.black);
        printButton.setDepressable(false);
        printButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                Vector v = new Vector();
                v.addElement(syncButton.isDown());
                manager.doCommand("Print", v);
            }

        }
);
        prefsButton.setFont(new Font("Serif", 0, 12));
        prefsButton.setName("prefsButton");
        prefsButton.setBackground(new Color(131, 153, 177));
        prefsButton.setForeground(Color.black);
        prefsButton.setDepressable(false);
        prefsButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                FIVPrefsDialog pd = new FIVPrefsDialog(new Frame(), true);
                pd.show();
            }

        }
);
        logButton.setName("logButton");
        logButton.setBackground(new Color(131, 153, 177));
        logButton.setForeground(Color.black);
        logButton.setDepressable(true);
        dualLogger.attachButton(logButton);
        syncButton.setFont(new Font("Serif", 0, 12));
        syncButton.setName("syncButton");
        syncButton.setBackground(new Color(131, 153, 177));
        syncButton.setForeground(Color.black);
        syncButton.setButtonDown(true);
        syncButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                Vector v = new Vector();
                v.addElement(syncButton.isDown());
                manager.doCommand("Sync", v);
            }

        }
);
        ijButton.setFont(new Font("Serif", 0, 12));
        ijButton.setName("ijButton");
        ijButton.setBackground(new Color(131, 153, 177));
        ijButton.setDepressable(false);
        ijButton.setForeground(Color.black);
        ijButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                manager.toggleImagejVisibility();
            }

        }
);
        surfaceButton.setFont(new Font("Serif", 0, 12));
        surfaceButton.setName("surfaceButton");
        surfaceButton.setBackground(new Color(131, 153, 177));
        surfaceButton.setDepressable(false);
        surfaceButton.setForeground(Color.black);
        surfaceButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                manager.runSurface();
            }

        }
);
        registryButton.setFont(new Font("Serif", 0, 12));
        registryButton.setName("registryButton");
        registryButton.setBackground(new Color(131, 153, 177));
        registryButton.setDepressable(false);
        registryButton.setForeground(Color.black);
        registryButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent mouseevent)
            {
            }

        }
);
        crosshairButton.setFont(new Font("Serif", 0, 12));
        crosshairButton.setName("crosshairButton");
        crosshairButton.setBackground(new Color(131, 153, 177));
        crosshairButton.setForeground(Color.black);
        crosshairButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                manager.doCommand("Crosshair", null);
            }

        }
);
        radButton.setFont(new Font("Serif", 0, 12));
        radButton.setName("radButton");
        radButton.setBackground(new Color(131, 153, 177));
        radButton.setForeground(Color.black);
        radButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                Vector v = new Vector();
                v.addElement(syncButton.isDown());
                manager.doCommand("Radiologic", null);
            }

        }
);
        contrastButton.setFont(new Font("Serif", 0, 12));
        contrastButton.setName("contrastButton");
        contrastButton.setBackground(new Color(131, 153, 177));
        contrastButton.setForeground(Color.black);
        contrastButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                manager.doCommand("Contrast", null);
            }

        }
);
        scaleButton.setFont(new Font("Serif", 0, 12));
        scaleButton.setName("scaleButton");
        scaleButton.setBackground(new Color(131, 153, 177));
        scaleButton.setForeground(Color.black);
        scaleButton.setDepressable(false);
        scaleButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                manager.doCommand("Scale", null);
            }

        }
);
        bCluster1.add(saveButton);
        bCluster1.add(printButton);
        bCluster1.add(prefsButton);
        bCluster1.add(logButton);
        bCluster2.add(syncButton);
        bCluster2.add(ijButton);
        bCluster2.add(surfaceButton);
        bCluster2.add(crosshairButton);
        bCluster2.add(radButton);
        bCluster3.add(contrastButton);
        bCluster4.add(scaleButton);
        buttonPanel.setLayout(new FlowLayout(0, 4, 0));
        buttonPanel.add(bCluster1);
        buttonPanel.add(new Separator(24, 10, 1));
        buttonPanel.add(bCluster2);
        buttonPanel.add(new Separator(24, 10, 1));
        buttonPanel.add(bCluster3);
        fileStructButton.setFont(new Font("Serif", 0, 12));
        fileStructButton.setName("fileStructButton");
        fileStructButton.setBackground(new Color(131, 153, 177));
        fileStructButton.setForeground(Color.black);
        fileStructButton.setDepressable(false);
        fileStructButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                Vector v = new Vector();
                v.addElement(new String("structural"));
                manager.doCommand("File", v);
            }

        }
);
        fileFuncButton.setFont(new Font("Serif", 0, 12));
        fileFuncButton.setName("fileFuncButton");
        fileFuncButton.setBackground(new Color(131, 153, 177));
        fileFuncButton.setForeground(Color.black);
        fileFuncButton.setDepressable(false);
        fileFuncButton.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent evt)
            {
                Vector v = new Vector();
                v.addElement(new String("functional"));
                manager.doCommand("File", v);
            }

        }
);
        functionalFile.setFont(new Font("SansSerif", 0, 12));
        functionalFile.addMouseListener(this);
        structuralFile.setFont(new Font("SansSerif", 0, 12));
        structuralFile.addMouseListener(this);
        filePanel.setLayout(new FlowLayout(0, 4, 0));
        filePanel.add(fileFuncButton);
        filePanel.add(functionalFile);
        filePanel.add(fileStructButton);
        filePanel.add(structuralFile);
        logCanvas = new Canvas() {

            public Dimension getSize()
            {
                return new Dimension(28, 26);
            }

            public void paint(Graphics g)
            {
                java.awt.Image img = FIVToolWindow.dualLogger.getCanvasImage();
                g.clearRect(0, 0, logCanvas.getBounds().width, logCanvas.getBounds().height);
                if(img != null)
                    g.drawImage(img, 0, 0, null);
            }

        }
;
        logCanvas.setBackground(Color.lightGray);
        dualLogger.attachCanvas(logCanvas);
        topPanel.setLayout(new FlowLayout(0, 0, 2));
        topPanel.add(goPanel);
        topPanel.add(new Separator(24, 10, 1));
        topPanel.add(filePanel);
        Panel tph = new Panel(new GridBagLayout());
        tph.setForeground(Color.black);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 9;
        c.anchor = 17;
        c.fill = 2;
        c.weightx = 1.0D;
        c.weighty = 0.5D;
        tph.add(topPanel, c);
        c = new GridBagConstraints();
        c.gridx = 10;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 2);
        c.gridwidth = 1;
        c.anchor = 13;
        tph.add(logCanvas, c);
        bottomPanel.setLayout(new FlowLayout(0, 0, 2));
        bottomPanel.add(buttonPanel);
        bottomPanel.add(new Separator(24, 10, 1));
        bottomPanel.add(activationPanel);
        bottomPanel.add(new Separator(24, 10, 1));
        bottomPanel.add(messagePanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = 17;
        gbc.weightx = 0.29999999999999999D;
        gbc.fill = 2;
        add(tph, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = 10;
        gbc.weightx = 0.29999999999999999D;
        gbc.fill = 2;
        add(new Separator(840, 2, 0), gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = 17;
        gbc.weightx = 0.29999999999999999D;
        add(bottomPanel, gbc);
        listenTo(this);
    }

    public void listenTo(Component c)
    {
        c.addKeyListener(this);
        if(c instanceof Container)
        {
            Component allComps[] = ((Container)c).getComponents();
            for(int i = 0; i < allComps.length; i++)
                listenTo(allComps[i]);

        }
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

    private void exitForm(WindowEvent evt)
    {
        System.exit(0);
    }

    public static void message(String s1)
    {
    }

    private void goButtonClicked(MouseEvent e)
    {
        java.awt.Cursor c = getCursor();
        setCursor(3);
        manager.show(getView(), getDisplay(), getRad());
        setCursor(c);
    }

    public void setCoords(int x, int y, int z, double val)
    {
        xfield.setText(Integer.toString(x));
        yfield.setText(Integer.toString(y));
        zfield.setText(Integer.toString(z));
        xCoord = x;
        yCoord = y;
        zCoord = z;
        NumberFormat formatter = NumberFormat.getNumberInstance();
        if(val > 100D)
            formatter.setMaximumFractionDigits(0);
        else
            formatter.setMaximumFractionDigits(3);
        intValueLabel.setText(" =  " + formatter.format(val));
    }

    public void updateSettings()
    {
        String mode = PropReader.getString("mode");
        if(mode == null)
            mode = manager.getDefaultMode();
        if(mode == null)
            return;
        double low = PropReader.getDouble("mode." + mode + ".func.low", 0.0D);
        double high = PropReader.getDouble("mode." + mode + ".func.high", 0.0D);
        low = PropReader.getDouble("func.low", low);
        high = PropReader.getDouble("func.high", high);
        setThresholdRange(low, high);
        String sign = PropReader.getString("mode." + mode + ".func.sign");
        if(sign != null)
            if(sign.equalsIgnoreCase("both"))
                setThresholdSign(3);
            else
            if(sign.equalsIgnoreCase("pos"))
                setThresholdSign(1);
            else
            if(sign.equalsIgnoreCase("neg"))
                setThresholdSign(2);
            else
            if(sign.equalsIgnoreCase("none"))
                setThresholdSign(4);
    }

    public void setThresholdValue(double val)
    {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(3);
        intValueLabel.setText(" =  " + formatter.format(val));
    }

    public void setThresholdRange(double min, double max)
    {
        minThreshold = min;
        minThresholdField.setText(Double.toString(minThreshold));
        maxThreshold = max;
        maxThresholdField.setText(Double.toString(maxThreshold));
    }

    public void setThresholdMin(double m)
    {
        minThreshold = m;
        minThresholdField.setText(Double.toString(minThreshold));
        minThresholdField.dispatchEvent(new ActionEvent(minThresholdField, 1001, Double.toString(minThreshold), 1001));
    }

    public void setThresholdMax(double m)
    {
        maxThreshold = m;
        maxThresholdField.setText(Double.toString(maxThreshold));
        maxThresholdField.dispatchEvent(new ActionEvent(maxThresholdField, 1001, Double.toString(maxThreshold), 1001));
    }

    public void setFunctionalFile(String s)
    {
        int ind = s.lastIndexOf('/');
        int ind2 = s.lastIndexOf('\\');
        if(ind2 > ind)
            ind = ind2;
        if(ind < 0)
            ind = 0;
        else
            ind++;
        String fname = s.substring(ind);
        if(fname.equalsIgnoreCase("none"))
            fname = "No activation file loaded";
        if(fname.length() > 25)
            fname = fname.substring(0, 25) + "...";
        functionalFile.setText(fname);
        functionalPopup.setTip(s);
        validate();
    }

    public void setStructuralFile(String s)
    {
        int ind = s.lastIndexOf('/');
        int ind2 = s.lastIndexOf('\\');
        if(ind2 > ind)
            ind = ind2;
        if(ind < 0)
            ind = 0;
        else
            ind++;
        String fname = s.substring(ind);
        if(fname.length() > 25)
            fname = fname.substring(0, 25) + "...";
        structuralFile.setText(fname);
        structuralPopup.setTip(s);
        validate();
    }

    public void setThresholdSign(int sign)
    {
        switch(sign)
        {
        default:
            break;

        case 3: // '\003'
            actChoice.select(0);
            break;

        case 1: // '\001'
            actChoice.select(1);
            break;

        case 2: // '\002'
            actChoice.select(2);
            break;

        case 4: // '\004'
            actChoice.select(3);
            break;

        case 0: // '\0'
            int current = actChoice.getSelectedIndex() + 1;
            if(current > 3)
                current = 0;
            actChoice.select(current);
            break;
        }
        actChoice.dispatchEvent(new ItemEvent(actChoice, 701, "", 1));
    }

    public boolean isLockDown()
    {
        return syncButton.isDown().booleanValue();
    }

    public int getDisplay()
    {
        return FIVImage.strToDisplay(displayChoice.getSelectedItem());
    }

    public int getView()
    {
        return FIVImage.strToView(viewChoice.getSelectedItem());
    }

    public boolean getRad()
    {
        return radButton.isDown().booleanValue();
    }

    public double getMinThreshold()
    {
        return minThreshold;
    }

    public Component getMinThresholdField()
    {
        return minThresholdField;
    }

    public Component getMaxThresholdField()
    {
        return maxThresholdField;
    }

    public double getMaxThreshold()
    {
        return maxThreshold;
    }

    public int getThresholdSign()
    {
        int sign = actChoice.getSelectedIndex();
        switch(sign)
        {
        case 0: // '\0'
            return 3;

        case 1: // '\001'
            return 1;

        case 2: // '\002'
            return 2;

        case 3: // '\003'
            return 4;
        }
        return 3;
    }

    public void actionPerformed(ActionEvent evt)
    {
        Object source = evt.getSource();
        if(source == minThresholdField || source == maxThresholdField)
            handleThresholdChange();
        else
        if(source == xfield || source == yfield || source == zfield)
            handleCoordChange((Component)evt.getSource());
    }

    public void focusGained(FocusEvent e)
    {
        Component c = e.getComponent();
        if(c == xfield || c == yfield || c == zfield)
            ((TextField)c).selectAll();
    }

    public void focusLost(FocusEvent evt)
    {
        Component c = evt.getComponent();
        if(c == minThresholdField || c == maxThresholdField)
            handleThresholdChange();
        else
        if(c == xfield || c == yfield || c == zfield)
            handleCoordChange((Component)evt.getSource());
    }

    public void itemStateChanged(ItemEvent evt)
    {
        Object source = evt.getSource();
        if(source == actChoice)
            handleThresholdChange();
    }

    private void handleThresholdChange()
    {
        Vector v = new Vector();
        try
        {
            minThreshold = Double.valueOf(minThresholdField.getText()).doubleValue();
            maxThreshold = Double.valueOf(maxThresholdField.getText()).doubleValue();
            v.addElement(new Double(minThreshold));
            v.addElement(new Double(maxThreshold));
            v.addElement(new Integer(getThresholdSign()));
            manager.doCommand("Threshold", v);
            minThresholdField.setText(Double.toString(minThreshold));
        }
        catch(NumberFormatException e)
        {
            minThresholdField.setText(Double.toString(minThreshold));
            maxThresholdField.setText(Double.toString(maxThreshold));
        }
    }

    public void handleCoordChange(Component c)
    {
        Vector v = new Vector();
        if(c == xfield)
            try
            {
                xCoord = Integer.valueOf(xfield.getText().trim()).intValue();
            }
            catch(NumberFormatException e)
            {
                xfield.setText(Integer.toString(xCoord));
            }
        else
        if(c == yfield)
            try
            {
                yCoord = Integer.valueOf(yfield.getText().trim()).intValue();
            }
            catch(NumberFormatException e)
            {
                yfield.setText(Integer.toString(yCoord));
            }
        else
        if(c == zfield)
        {
            try
            {
                zCoord = Integer.valueOf(zfield.getText().trim()).intValue();
            }
            catch(NumberFormatException e)
            {
                zfield.setText(Integer.toString(zCoord));
            }
        } else
        {
            System.out.println("FIVToolWindow: handleCoords:  unexpected component: " + c);
            return;
        }
        v.addElement(new Integer(xCoord));
        v.addElement(new Integer(yCoord));
        v.addElement(new Integer(zCoord));
        manager.doCommand("Coords", v);
    }

    public static void appendLog(int i, String s)
    {
        dualLogger.appendLog(i, s);
    }

    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        Component c = e.getComponent();
        if(key == 82 && e.isControlDown())
            manager.toFront();
        else
        if(key == 87 && e.isControlDown())
        {
            if(c == structuralFile)
                appendLog(3, "Structural file: " + structuralPopup.getTip());
            else
            if(c == functionalFile)
                appendLog(3, "Functional file: " + functionalPopup.getTip());
        } else
        if(key == 32)
        {
            if(c == structuralFile)
                structuralPopup.showTip();
            else
            if(c == functionalFile)
                functionalPopup.showTip();
        } else
        if(key == 82 || key == 32 || key == 66 || key == 83 || key == 80)
            manager.keyPressed(e);
    }

    public void mouseClicked(MouseEvent e)
    {
        e.getComponent().requestFocus();
    }

    public void mouseEntered(MouseEvent e)
    {
        if(getFocusOwner() != null)
            e.getComponent().requestFocus();
    }

    public void mouseExited(MouseEvent e)
    {
        if(getFocusOwner() != null)
            requestFocus();
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    FIVManager manager;
    static FIVDualLogger dualLogger;
    double minThreshold;
    double maxThreshold;
    int xCoord;
    int yCoord;
    int zCoord;
    private Panel topPanel;
    private Panel bottomPanel;
    private Panel buttonPanel;
    private Panel titleBarPanel;
    private Panel filePanel;
    private ImageButton closeButton;
    private Panel goPanel;
    private Choice displayChoice;
    private Choice viewChoice;
    private Button goButton;
    private Panel bCluster1;
    private Panel bCluster2;
    private Panel bCluster3;
    private Panel bCluster4;
    private ImageButton fileStructButton;
    private ImageButton fileFuncButton;
    private ImageButton saveButton;
    private ImageButton printButton;
    private ImageButton logButton;
    private ImageButton syncButton;
    private ImageButton ijButton;
    private ImageButton crosshairButton;
    private ImageButton arrowButton;
    private ImageButton contrastButton;
    private ImageButton scaleButton;
    private ImageButton radButton;
    private ImageButton prefsButton;
    private ImageButton surfaceButton;
    private ImageButton registryButton;
    private Panel activationPanel;
    private MapCanvas mapCanvas;
    private TextField minThresholdField;
    private TextField maxThresholdField;
    private Panel messagePanel;
    private Choice actChoice;
    private TextField xfield;
    private TextField yfield;
    private TextField zfield;
    private Label intValueLabel;
    private Label functionalFile;
    private Label structuralFile;
    private Canvas logCanvas;
    private PopupMessage structuralPopup;
    private PopupMessage functionalPopup;




}
