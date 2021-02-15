// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVCoords.java

import java.awt.Point;
import java.awt.Rectangle;
import java.io.PrintStream;

public class FIVCoords
{

    public FIVCoords()
    {
        radiologic = 1;
        montageIncrement = 0;
        montageStartSlice = 0;
        nMontageColumns = 0;
        boxLoadedFlag = false;
        voxelSizeFlag = false;
        imageSizeFlag = false;
        imageDim = new Point3d();
        bboxDim = new Point3d();
        bboxStart = new Point3d();
        origin = new Point3d();
        talPos = new Point3d();
        view = 0;
        display = 0;
    }

    public String toString()
    {
        return new String("image dimensions: " + imageDim + "\nbox start: " + bboxStart + "\nbox dimensions: " + bboxDim);
    }

    public void loadBoundingBox()
    {
        if(!voxelSizeFlag)
            FIVToolWindow.appendLog(1, "FIVImage: voxel size must be specified before calling loadBoundingBox");
        if(!imageSizeFlag)
            FIVToolWindow.appendLog(1, "FIVImage: image size must be specified before calling loadBoundingBox");
        String voxSize;
        if(voxelSize == 1)
            voxSize = "111";
        else
        if(voxelSize == 2)
            voxSize = "222";
        else
        if(voxelSize == 3)
        {
            voxSize = "333";
        } else
        {
            System.out.println("Invalid voxel size specified: " + voxelSize);
            System.out.println("Using 222");
            voxSize = "222";
        }
        String mode = PropReader.getString("mode");
        if(mode == null)
            mode = voxSize;
        bboxStart.x = PropReader.getInt("mode." + mode + ".box.x", 0);
        bboxStart.y = PropReader.getInt("mode." + mode + ".box.y", 0);
        bboxStart.z = PropReader.getInt("mode." + mode + ".box.z", 0);
        bboxDim.x = PropReader.getInt("mode." + mode + ".box.width", imageDim.x);
        bboxDim.y = PropReader.getInt("mode." + mode + ".box.depth", imageDim.y);
        bboxDim.z = PropReader.getInt("mode." + mode + ".box.height", imageDim.z);
        if(display == 1)
            scale = PropReader.getInt("mode." + mode + ".scale.montage", 2);
        else
            scale = PropReader.getInt("mode." + mode + ".scale.stack", 2);
        switch(voxelSize)
        {
        case 1: // '\001'
            origin.x = 88;
            origin.y = 84;
            origin.z = 75;
            break;

        case 2: // '\002'
            origin.x = 63;
            origin.y = 63;
            origin.z = 34;
            break;

        case 3: // '\003'
            origin.x = 24;
            origin.y = 29;
            origin.z = 20;
            break;

        default:
            origin.x = 0;
            origin.y = 0;
            origin.z = 0;
            break;
        }
        origin.x = origin.x - bboxStart.x;
        origin.y = origin.y - bboxStart.y;
        origin.z = origin.z - bboxStart.z;
        boxLoadedFlag = true;
    }

    public void setDisplay(int d)
    {
        display = d;
    }

    public void setView(int v)
    {
        view = v;
    }

    public void setImageSize(int x, int y, int z)
    {
        imageDim.x = x;
        imageDim.y = y;
        imageDim.z = z;
        imageSizeFlag = true;
    }

    public void setVoxelSize(int s)
    {
        voxelSize = s;
        voxelSizeFlag = true;
    }

    public void setRadiologic(boolean r)
    {
        if(r)
            radiologic = -1;
        else
            radiologic = 1;
    }

    public void setTalFromStack(int winX, int winY, int slice)
    {
        int scale = getScale();
        winX /= scale;
        winY /= scale;
        slice--;
        switch(view)
        {
        case 3: // '\003'
            talPos.x = radiologic * (winX - origin.x) * voxelSize;
            talPos.y = (origin.y - winY) * voxelSize;
            talPos.z = (slice - origin.z) * voxelSize;
            break;

        case 1: // '\001'
            talPos.x = radiologic * (winX - origin.x) * voxelSize;
            talPos.y = (origin.y - slice) * voxelSize;
            talPos.z = (bboxDim.z - 1 - origin.z - winY) * voxelSize;
            break;

        case 2: // '\002'
            talPos.x = (slice - origin.x) * voxelSize;
            talPos.y = (origin.y - winX) * voxelSize;
            talPos.z = (bboxDim.z - 1 - origin.z - winY) * voxelSize;
            break;

        default:
            System.out.println("FIVCoords: setCoordsFromStack: Bad view");
            return;
        }
    }

    public void setTalFromSlice(int slice)
    {
        slice--;
        switch(view)
        {
        case 3: // '\003'
            talPos.z = (slice - origin.z) * voxelSize;
            break;

        case 1: // '\001'
            talPos.y = (origin.y - slice) * voxelSize;
            break;

        case 2: // '\002'
            talPos.x = (slice - origin.x) * voxelSize;
            break;

        default:
            System.out.println("FIVCoords: setCoordsBySlice: Bad view");
            return;
        }
    }

    public void setTalFromMontage(int winX, int winY)
    {
        int x = 0;
        int y = 0;
        int z = 0;
        int sliceNum = 0;
        int scale = getScale();
        winX /= scale;
        winY /= scale;
        if(view == 3)
        {
            x = winX % bboxDim.x;
            talPos.x = radiologic * (x - origin.x) * voxelSize;
            y = winY % bboxDim.y;
            talPos.y = (origin.y - y) * voxelSize;
            sliceNum = (int)Math.floor(winY / bboxDim.y) * nMontageColumns + (int)Math.ceil(winX / bboxDim.x);
            talPos.z = ((montageStartSlice + montageIncrement * sliceNum) - origin.z) * voxelSize;
        } else
        if(view == 1)
        {
            x = winX % bboxDim.x;
            talPos.x = radiologic * (x - origin.x) * voxelSize;
            sliceNum = (int)Math.floor(winY / bboxDim.z) * nMontageColumns + (int)Math.ceil(winX / bboxDim.x);
            talPos.y = (origin.y - (montageStartSlice + montageIncrement * sliceNum)) * voxelSize;
            z = winY % bboxDim.z;
            talPos.z = -(z + (origin.z - bboxDim.z)) * voxelSize;
        } else
        if(view == 2)
        {
            sliceNum = (int)Math.floor(winY / bboxDim.z) * nMontageColumns + (int)Math.ceil(winX / bboxDim.y);
            talPos.x = ((montageStartSlice + montageIncrement * sliceNum) - origin.x) * voxelSize;
            y = winX % bboxDim.y;
            talPos.y = (origin.y - y) * voxelSize;
            z = winY % bboxDim.z;
            talPos.z = -(z + (origin.z - bboxDim.z)) * voxelSize;
        }
    }

    public void setTalFromTal(int x, int y, int z)
    {
        talPos.set(x, y, z);
    }

    public void setTalFromTal(Point3d p)
    {
        talPos.set(p);
    }

    public void setMontageIncrement(int inc)
    {
        montageIncrement = inc;
    }

    public void setMontageStartSlice(int slice)
    {
        montageStartSlice = slice;
    }

    public void setMontageColumnCount(int col)
    {
        nMontageColumns = col;
    }

    public int getView()
    {
        return view;
    }

    public int getDisplay()
    {
        return display;
    }

    public int getScale()
    {
        return scale;
    }

    public void setScale(int s)
    {
        if(s < 1 || s > 6)
        {
            return;
        } else
        {
            scale = s;
            return;
        }
    }

    public Rectangle getBoundingBox()
    {
        switch(view)
        {
        case 3: // '\003'
            return new Rectangle(bboxStart.x, bboxStart.y, bboxDim.x, bboxDim.y);

        case 1: // '\001'
            return new Rectangle(bboxStart.x, bboxStart.z, bboxDim.x, bboxDim.z);

        case 2: // '\002'
            return new Rectangle(bboxStart.y, bboxStart.z, bboxDim.y, bboxDim.z);
        }
        System.out.println("Error: FIVCoords:getBoundingBox: View not set");
        return new Rectangle(0, 0, 0, 0);
    }

    public int getFirstSliceInBox()
    {
        switch(view)
        {
        case 3: // '\003'
            return imageDim.z - bboxStart.z - bboxDim.z;

        case 1: // '\001'
            return bboxStart.y + 1;

        case 2: // '\002'
            return bboxStart.x + 1;
        }
        System.out.println("Error: FIVCoords:getFirstSliceInBox: View not set");
        return 0;
    }

    public int getLastSliceInBox()
    {
        switch(view)
        {
        case 3: // '\003'
            return imageDim.z - bboxStart.z;

        case 1: // '\001'
            return bboxStart.y + bboxDim.y;

        case 2: // '\002'
            return bboxStart.x + bboxDim.x;
        }
        System.out.println("Error: FIVCoords:getLastSliceInBox: View not set");
        return 0;
    }

    public int getVoxelSize()
    {
        return voxelSize;
    }

    public Point3d getPosTal()
    {
        return talPos;
    }

    public Point getPosImagePlus()
    {
        int x = 0;
        int y = 0;
        switch(display)
        {
        case 2: // '\002'
            switch(view)
            {
            case 3: // '\003'
                if(radiologic == 1)
                    x = bboxStart.x + (origin.x + talPos.x / voxelSize);
                else
                    x = imageDim.x - 1 - bboxStart.x - (origin.x + talPos.x / voxelSize);
                y = bboxStart.y + (origin.y - talPos.y / voxelSize);
                break;

            case 1: // '\001'
                if(radiologic == 1)
                    x = bboxStart.x + (origin.x + talPos.x / voxelSize);
                else
                    x = imageDim.x - 1 - bboxStart.x - (origin.x + talPos.x / voxelSize);
                y = (bboxStart.z + (bboxDim.z - 1 - origin.z)) - talPos.z / voxelSize;
                break;

            case 2: // '\002'
                x = bboxStart.y + (origin.y - talPos.y / voxelSize);
                y = (bboxStart.z + (bboxDim.z - 1 - origin.z)) - talPos.z / voxelSize;
                break;

            default:
                System.out.println("FIVCoords: getPosImagePlus: getStackPos: Bad view");
                break;
            }
            break;

        case 1: // '\001'
            switch(view)
            {
            case 3: // '\003'
            {
                int nBox = (montageStartSlice - talPos.z / voxelSize - origin.z) / Math.abs(montageIncrement);
                int nRow = nBox / nMontageColumns;
                int nColumn = nBox - nRow * nMontageColumns;
                x = nColumn * bboxDim.x + origin.x + radiologic * (talPos.x / voxelSize);
                y = nRow * bboxDim.y + (origin.y - talPos.y / voxelSize);
                break;
            }

            case 1: // '\001'
            {
                int nBox = -((montageStartSlice + talPos.y / voxelSize) - origin.y) / Math.abs(montageIncrement);
                int nRow = nBox / nMontageColumns;
                int nColumn = nBox - nRow * nMontageColumns;
                x = nColumn * bboxDim.x + (origin.x + talPos.x / voxelSize);
                y = nRow * bboxDim.z - ((talPos.z / voxelSize + origin.z) - bboxDim.z);
                break;
            }

            case 2: // '\002'
            {
                int nBox = ((talPos.x / voxelSize - montageStartSlice) + origin.x) / Math.abs(montageIncrement);
                int nRow = nBox / nMontageColumns;
                int nColumn = nBox - nRow * nMontageColumns;
                x = nColumn * bboxDim.y + (origin.y - talPos.y / voxelSize);
                y = nRow * bboxDim.z - ((talPos.z / voxelSize + origin.z) - bboxDim.z);
                break;
            }

            default:
            {
                System.out.println("FIVCoords: getPosImagePlus: Bad view");
                break;
            }
            }
            break;

        default:
            System.out.println("FIVCoords: getPosImagePlus: Bad display");
            break;
        }
        Point p = new Point(x, y);
        return p;
    }

    public Point getPosWindow()
    {
        int x = 0;
        int y = 0;
        int scale = getScale();
        switch(display)
        {
        case 2: // '\002'
            switch(view)
            {
            case 3: // '\003'
                x = radiologic * (talPos.x / voxelSize) + origin.x;
                y = origin.y - talPos.y / voxelSize;
                break;

            case 1: // '\001'
                x = (radiologic * talPos.x) / voxelSize + origin.x;
                y = bboxDim.z - origin.z - talPos.z / voxelSize;
                break;

            case 2: // '\002'
                x = origin.y - talPos.y / voxelSize;
                y = bboxDim.z - origin.z - talPos.z / voxelSize;
                break;

            default:
                System.out.println("FIVCoords: getStackPos: Bad view");
                break;
            }
            break;

        case 1: // '\001'
            switch(view)
            {
            case 3: // '\003'
            {
                int nBox = (montageStartSlice - talPos.z / voxelSize - origin.z) / Math.abs(montageIncrement);
                int nRow = nBox / nMontageColumns;
                int nColumn = nBox - nRow * nMontageColumns;
                x = nColumn * bboxDim.x + (origin.x + talPos.x / voxelSize);
                y = nRow * bboxDim.y + (origin.y - talPos.y / voxelSize);
                break;
            }

            case 1: // '\001'
            {
                int nBox = -((montageStartSlice + talPos.y / voxelSize) - origin.y) / Math.abs(montageIncrement);
                int nRow = nBox / nMontageColumns;
                int nColumn = nBox - nRow * nMontageColumns;
                x = nColumn * bboxDim.x + (origin.x + talPos.x / voxelSize);
                y = nRow * bboxDim.z - ((talPos.z / voxelSize + origin.z) - bboxDim.z);
                break;
            }

            case 2: // '\002'
            {
                int nBox = ((talPos.x / voxelSize - montageStartSlice) + origin.x) / Math.abs(montageIncrement);
                int nRow = nBox / nMontageColumns;
                int nColumn = nBox - nRow * nMontageColumns;
                x = nColumn * bboxDim.y + (origin.y - talPos.y / voxelSize);
                y = nRow * bboxDim.z - ((talPos.z / voxelSize + origin.z) - bboxDim.z);
                break;
            }

            default:
            {
                System.out.println("FIVCoords: getPosImagePlus: Bad view");
                break;
            }
            }
            break;

        default:
            System.out.println("FIVCoords: getPosWindow: Bad display");
            break;
        }
        Point p = new Point(x * scale, y * scale);
        return p;
    }

    public int getSlice()
    {
        switch(view)
        {
        case 3: // '\003'
            return talPos.z / voxelSize + origin.z + 1;

        case 1: // '\001'
            return (origin.y - talPos.y / voxelSize) + 1;

        case 2: // '\002'
            return talPos.x / voxelSize + origin.x + 1;
        }
        System.out.println("FIVCoords: setCoordsBySlice: Bad view");
        return 0;
    }

    private static final int ORIGIN_333_X = 24;
    private static final int ORIGIN_333_Y = 29;
    private static final int ORIGIN_333_Z = 20;
    private static final int ORIGIN_222_X = 63;
    private static final int ORIGIN_222_Y = 63;
    private static final int ORIGIN_222_Z = 34;
    private static final int ORIGIN_111_X = 88;
    private static final int ORIGIN_111_Y = 84;
    private static final int ORIGIN_111_Z = 75;
    Point3d imageDim;
    Point3d bboxDim;
    Point3d bboxStart;
    Point3d origin;
    Point3d talPos;
    int voxelSize;
    int radiologic;
    int view;
    int display;
    int scale;
    int montageIncrement;
    int montageStartSlice;
    int nMontageColumns;
    boolean boxLoadedFlag;
    boolean voxelSizeFlag;
    boolean imageSizeFlag;

}
