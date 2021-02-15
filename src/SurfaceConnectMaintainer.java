// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SurfaceConnectMaintainer.java


public interface SurfaceConnectMaintainer
{

    public abstract void startSurfaceConnection();

    public abstract void stopSurfaceConnection();

    public abstract boolean isSurfaceConnectionRunning();

    public abstract void completedSurfaceConnection();

    public abstract void setCoordsFromSurface(int i, int j, int k);

    public abstract void printCoordsFromSurface();

    public abstract void printMessageFromSurface(String s);

    public abstract Point3d getCoordsForSurface();

    public abstract int getSurfacePort();

    public abstract String getSurfaceHost();
}
