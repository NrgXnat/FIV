// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SurfaceConnectThread.java

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class SurfaceConnectThread extends Thread
{

    public SurfaceConnectThread(SurfaceConnectMaintainer maintainer)
    {
        stopFlag = false;
        surfaceMaintainer = maintainer;
    }

    public void disconnect()
    {
        try
        {
            if(dataOut != null)
                dataOut.close();
            if(dataIn != null)
                dataIn.close();
            if(dataSocket != null)
                dataSocket.close();
            if(dataSS != null)
                dataSS.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopThread()
    {
        System.out.println("Stop signal received.");
        stopFlag = true;
    }

    public synchronized void run()
    {
        boolean clientConnected = false;
        int dataPort = 0;
        char buffer[] = new char[256];
        int bufCount = 0;
        Point3d lastCoords = new Point3d(-1000, -1000, -1000);
        int commPort = surfaceMaintainer.getSurfacePort();
        try
        {
            ServerSocket ss = new ServerSocket(commPort);
            ss.setSoTimeout(500);
            do
            {
                do
                {
                    System.out.println("Polling for client connection on port " + commPort);
                    surfaceMaintainer.printMessageFromSurface("Polling for client connection on port " + commPort + "\n");
                    Socket client = null;
                    do
                    {
                        try
                        {
                            client = ss.accept();
                        }
                        catch(Exception e) { }
                        if(stopFlag)
                        {
                            disconnect();
                            return;
                        }
                    } while(client == null);
                    System.out.println("Connection accepted from" + client.getInetAddress().getHostName());
                    surfaceMaintainer.completedSurfaceConnection();
                    surfaceMaintainer.printMessageFromSurface("** Connection accepted from " + client.getInetAddress().getHostName() + " **\n");
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                    String line = in.readLine();
                    String iochan = new String(line);
                    System.out.println("Received line: " + line);
                    surfaceMaintainer.printMessageFromSurface("Incoming message: " + line + "\n");
                    if(iochan.indexOf("\n") >= 0)
                        iochan = iochan.substring(0, iochan.indexOf("\n"));
                    surfaceMaintainer.printMessageFromSurface("Incoming message: " + line + "\n");
                    if(iochan.startsWith("TAL_PORT"))
                    {
                        StringTokenizer tok = new StringTokenizer(iochan.substring(iochan.indexOf(' ') + 1), ":");
                        tok.nextToken();
                        String dataHost = tok.nextToken();
                        dataPort = Integer.parseInt(tok.nextToken());
                        out.write("TAL_OK\0");
                        out.flush();
                        clientConnected = true;
                        System.out.println("Requested data port: " + dataPort);
                        surfaceMaintainer.printMessageFromSurface("Requested data port: " + dataPort + "\n");
                    } else
                    {
                        System.out.println("PhoneLine error: Failed to get proper information from client");
                        surfaceMaintainer.printMessageFromSurface("PhoneLine error: Failed to get proper information from client\n");
                        clientConnected = false;
                    }
                    in.close();
                    out.close();
                    client.close();
                    ss.close();
                } while(!clientConnected);
                dataSS = new ServerSocket(dataPort);
                dataSocket = dataSS.accept();
                dataIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                dataOut = new PrintWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
                dataSocket.setSoTimeout(100);
                try
                {
                    bufCount = dataIn.read(buffer, 0, 256);
                }
                catch(Exception e)
                {
                    bufCount = 0;
                    buffer[0] = '\0';
                }
                while(bufCount >= 0) 
                {
                    if(stopFlag)
                    {
                        disconnect();
                        return;
                    }
                    String inline = String.valueOf(buffer);
                    if(bufCount > 0)
                    {
                        inline = inline.substring(0, bufCount - 1);
                        StringTokenizer tok = new StringTokenizer(inline);
                        if(tok.countTokens() == 4 && tok.nextToken().equals("TAL_XYZ"))
                        {
                            int in_x = Integer.parseInt(tok.nextToken());
                            int in_y = Integer.parseInt(tok.nextToken());
                            int in_z = Integer.parseInt(tok.nextToken());
                            surfaceMaintainer.setCoordsFromSurface(in_x, in_y, in_z);
                            surfaceMaintainer.printCoordsFromSurface();
                        }
                    }
                    try
                    {
                        bufCount = dataIn.read(buffer, 0, 256);
                        dataOut.print("TAL_OK\0");
                        dataOut.flush();
                    }
                    catch(Exception e)
                    {
                        buffer[0] = '\0';
                        bufCount = 0;
                    }
                    Point3d currentCoords = surfaceMaintainer.getCoordsForSurface();
                    if(currentCoords != null)
                    {
                        if(!currentCoords.equals(lastCoords))
                        {
                            surfaceMaintainer.printMessageFromSurface("Sending coords: " + currentCoords + "\n");
                            String outline = "TAL_XYZ " + currentCoords.x + " " + currentCoords.y + " " + currentCoords.z + "\0";
                            dataOut.write(outline);
                            dataOut.flush();
                        }
                        lastCoords.set(currentCoords);
                    }
                }
                System.out.println("closing connection");
                disconnect();
                clientConnected = false;
            } while(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean writeCoords;
    BufferedReader dataIn;
    PrintWriter dataOut;
    Socket dataSocket;
    ServerSocket dataSS;
    private SurfaceConnectMaintainer surfaceMaintainer;
    private boolean stopFlag;
}
