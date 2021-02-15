// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropReader.java

import java.io.*;
import java.util.Properties;

public class PropReader
{

    public PropReader()
    {
    }

    public static void load()
    {
        loadProps("fiv.prefs");
    }

    public static void loadProps(String fileName)
    {
        String currentDir = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");
        String fivHome = System.getProperty("fivhome");
        String defaultHome = ".";
        System.out.println("FIV home: " + fivHome);
        InputStream f;
        try
        {
            propFile = userHome + "/" + fileName;
            f = new FileInputStream(propFile);
        }
        catch(FileNotFoundException e)
        {
            f = null;
        }
        if(f == null)
            try
            {
                propFile = fivHome + "/" + fileName;
                f = new FileInputStream(propFile);
            }
            catch(FileNotFoundException e)
            {
                f = null;
            }
        if(f == null)
            try
            {
                propFile = defaultHome + "/" + fileName;
                f = new FileInputStream(propFile);
            }
            catch(FileNotFoundException e)
            {
                f = null;
            }
        System.out.println("Properties file: " + propFile);
        if(f == null)
        {
            System.out.println(fileName + " could not be located in " + userHome + ", " + currentDir + ", or at " + defaultHome);
            return;
        }
        try
        {
            fileProps.load(f);
            f.close();
        }
        catch(IOException e)
        {
            System.out.println("Error loading properties file " + fileName);
        }
    }

    public static void parseCommandline(String args[])
    {
        if(args.length > 0 && args[0].charAt(0) != '-')
        {
            instanceProps.put("func.userimage", args[0]);
            if(args.length > 1 && args[1].charAt(0) != '-')
                instanceProps.put("anat.userimage", args[1]);
        }
        for(int i = 0; i < args.length; i++)
            if(args[i].equalsIgnoreCase("-a"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("func.userimage", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-lstat"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("func.low", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-hstat"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("func.high", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-sign"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("func.sign", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-colormap"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("func.colormap", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-s"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("anat.userimage", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-lanat"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("anat.low", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-hanat"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("anat.high", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-lut"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("lutFile", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-snap"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("snapfile", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-slice"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("slice", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-m"))
            {
                if(args.length == 1)
                    instanceProps.put("showmodes", "true");
                else
                if(i + 1 < args.length)
                    instanceProps.put("mode", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-o"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("orient", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-d"))
            {
                if(i + 1 < args.length)
                    instanceProps.put("display", args[i + 1]);
            } else
            if(args[i].equalsIgnoreCase("-nosig"))
                instanceProps.put("nosig", "true");
            else
            if(args[i].equalsIgnoreCase("-notool"))
                instanceProps.put("notool", "true");
            else
            if(args[i].equalsIgnoreCase("-help") || args[i].equalsIgnoreCase("-h"))
                instanceProps.put("showhelp", "true");
            else
            if(args[i].equalsIgnoreCase("-version") || args[i].equalsIgnoreCase("-v"))
                instanceProps.put("showvers", "true");

    }

    public static void writeProperties()
    {
        try
        {
            String userHome = System.getProperty("user.home");
            String propFile = userHome + "/" + "fiv.prefs";
            FileOutputStream out = new FileOutputStream(propFile);
            fileProps.save(out, "FIV user properties");
        }
        catch(Exception e)
        {
            FIVToolWindow.appendLog(1, "Unable to write properties to output file.");
            FIVToolWindow.appendLog(1, e.toString());
        }
    }

    public static void setInstanceProperty(String key, String value)
    {
        instanceProps.put(key, value);
    }

    public static void setProperty(String key, String value)
    {
        fileProps.put(key, value);
    }

    public static String getString(String key)
    {
        if(instanceProps.getProperty(key) != null)
            return instanceProps.getProperty(key);
        else
            return fileProps.getProperty(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue)
    {
        if(instanceProps == null)
            return defaultValue;
        String s;
        if(instanceProps.getProperty(key) != null)
            s = instanceProps.getProperty(key);
        else
            s = fileProps.getProperty(key);
        if(s == null)
            return defaultValue;
        else
            return s.trim().equalsIgnoreCase("true");
    }

    public static int getInt(String key, int defaultValue)
    {
        if(instanceProps == null)
            return defaultValue;
        String s;
        if(instanceProps.getProperty(key) != null)
            s = instanceProps.getProperty(key);
        else
            s = fileProps.getProperty(key);
        if(s != null)
            try
            {
                return Integer.decode(s).intValue();
            }
            catch(NumberFormatException e)
            {
                System.out.println("Error reading property value: " + e);
            }
        else
            System.out.println("Using default value for property " + key);
        return defaultValue;
    }

    public static double getDouble(String key, double defaultValue)
    {
        if(instanceProps == null)
            return defaultValue;
        String s;
        if(instanceProps.getProperty(key) != null)
            s = instanceProps.getProperty(key);
        else
            s = fileProps.getProperty(key);
        Double d = null;
        if(s != null)
        {
            try
            {
                d = new Double(s);
            }
            catch(NumberFormatException e)
            {
                d = null;
            }
            if(d != null)
                return d.doubleValue();
        }
        return defaultValue;
    }

    public static final String USER_PREFS = "fiv.prefs";
    private static String propFile;
    static Properties fileProps = new Properties();
    static Properties instanceProps = new Properties();

}
