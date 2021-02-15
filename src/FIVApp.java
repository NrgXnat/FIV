// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVApp.java

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class FIVApp
{

    public FIVApp(String args[])
    {
        PropReader.load();
        PropReader.parseCommandline(args);
        if(PropReader.getString("showhelp") != null)
        {
            showHelp();
            System.exit(0);
        }
        if(PropReader.getString("showvers") != null)
        {
            showVersion();
            System.exit(0);
        }
        if(PropReader.getString("showmodes") != null)
        {
            showModes();
            System.exit(0);
        }
        String func = PropReader.getString("func.userimage");
        String anat = PropReader.getString("anat.userimage");
        manager = new FIVManager();
        tools = new FIVToolWindow(manager);
        manager.initImageJ();
        String argString = "";
        for(int i = 0; i < args.length; i++)
            argString = argString + args[i] + " ";

        if(args.length > 0)
        {
            FIVApp _tmp = this;
            FIVToolWindow.appendLog(2, "***" + argString + "***");
        }
        boolean notool;
        if(func == null && anat == null)
        {
            notool = false;
            tools.setFunctionalFile("  <no image loaded>  ");
            tools.setStructuralFile("<using default image>");
        } else
        {
            String path = System.getProperty("user.dir");
            if(func != null && !func.startsWith("/"))
                func = path + "/" + func;
            if(anat != null && !anat.startsWith("/"))
                anat = path + "/" + anat;
            manager.setFilenames(func, anat);
            boolean success = manager.showStartupImages();
            String mode = PropReader.getString("mode");
            if(mode == null)
                mode = manager.getDefaultMode();
            if(mode == null)
                notool = false;
            else
                notool = PropReader.getBoolean("mode." + mode + ".notool", false);
            if(!success)
            {
                FIVToolWindow.appendLog(1, "Unable to display the images. Are they formatted properly?");
                if(notool)
                {
                    System.out.println("Exiting FIV");
                    System.exit(1);
                }
            }
        }
        tools.updateSettings();
        if(manager.getImageCount() == 0)
            notool = false;
        String slice = PropReader.getString("slice");
        if(PropReader.getString("snapfile") != null)
        {
            System.out.println("Displaying slice: " + slice);
            try
            {
                Vector v = new Vector();
                v.addElement(new Integer(0));
                v.addElement(new Integer(0));
                v.addElement(new Integer(slice));
                manager.setSyncCoords(v);
            }
            catch(Exception e)
            {
                System.out.println("ERROR: Unable to set slice.");
                e.printStackTrace();
            }
        }
        String snapfile = PropReader.getString("snapfile");
        if(PropReader.getString("snapfile") != null)
        {
            System.out.println("The snapfile is: " + snapfile);
            try
            {
                Thread.sleep(2000L);
            }
            catch(Exception e) { }
            manager.saveCurrentImage(snapfile);
            System.exit(1);
        }
        if(notool)
            tools.setVisible(false);
        else
            tools.setVisible(true);
    }

    public void showVersion()
    {
        System.out.println(" __________________________________________");
        System.out.println("|                                          |");
        System.out.println("|                 FIV 1.2                  |");
        System.out.println("|          a product of the NRG            |");
        System.out.println("| Washington University School of Medicine |");
        System.out.println("|   Mallinckrodt Institute of Radiology    |");
        System.out.println("|    http://nrg.wustl.edu/projects/fiv/    |");
        System.out.println("|          nrgtech@npg.wustl.edu           |");
        System.out.println("|__________________________________________|");
    }

    public void showModes()
    {
        String modelist = PropReader.getString("modelist");
        System.out.println("The following modes are available:");
        if(modelist != null)
        {
            for(StringTokenizer st = new StringTokenizer(modelist, ","); st.hasMoreTokens(); System.out.println(st.nextToken()));
        } else
        {
            System.out.println("No valid modes");
        }
    }

    public void showHelp()
    {
        try
        {
            java.io.InputStream is = getClass().getResourceAsStream("/FIV_help.txt");
            if(is == null)
            {
                System.out.println("Help file resource unavailable");
            } else
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String s;
                while((s = br.readLine()) != null) 
                    System.out.println(s);
            }
        }
        catch(IOException e)
        {
            System.out.println("Help file unreadable.");
        }
    }

    public static void main(String args[])
    {
        new FIVApp(args);
    }

    private FIVManager manager;
    private FIVToolWindow tools;
    private FIVDualLogger dlogger;
}
