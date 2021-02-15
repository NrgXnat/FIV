// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Scanner.java

import java.io.*;
import java.net.URL;

public class Scanner
{

    public Scanner(String url)
    {
        this.url = url;
        nBytes = 0;
    }

    public void open()
        throws IOException
    {
        URL u = new URL(url);
        DataInputStream dataIn = new DataInputStream(u.openStream());
        InputStreamReader streamReader = new InputStreamReader(dataIn);
        reader = new BufferedReader(streamReader);
        reader.mark(scanLimit);
    }

    public void close()
        throws IOException
    {
        reader.close();
    }

    public String getDelimitedString(String pattern, String delimiter)
        throws IOException
    {
        reader.reset();
        String s;
        while((s = reader.readLine()) != null) 
        {
            nBytes += s.getBytes().length;
            if(nBytes > scanLimit)
            {
                System.out.println("Error: Scanner exceeded limit.");
                return null;
            }
            int ind;
            if((ind = s.indexOf(pattern)) != -1)
            {
                ind = s.indexOf(delimiter);
                if(ind == -1)
                {
                    System.out.println("Scanner:getDelimitedString: found pattern but not delimiter.");
                    return null;
                } else
                {
                    String totrim = s.substring(ind + delimiter.length() + 1);
                    return totrim.trim();
                }
            }
        }
        return null;
    }

    String url;
    BufferedReader reader;
    int nBytes;
    static int scanLimit = 10000;

}
