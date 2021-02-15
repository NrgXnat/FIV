// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Point3d.java


public class Point3d
{

    public Point3d()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    public Point3d(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3d(Point3d p)
    {
        x = p.x;
        y = p.y;
        z = p.z;
    }

    public void set(Point3d p)
    {
        x = p.x;
        y = p.y;
        z = p.z;
    }

    public void set(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Point3d that)
    {
        return x == that.x && y == that.y && z == that.z;
    }

    public String toString()
    {
        return new String(" x: " + x + " y: " + y + " z: " + z);
    }

    public int x;
    public int y;
    public int z;
}
