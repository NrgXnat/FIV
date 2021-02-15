// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVColorModel.java

import java.awt.image.IndexColorModel;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FIVColorModel
{

    public FIVColorModel()
    {
        alpha = 255;
    }

    public void readLUT(String file)
    {
        ArrayList r = new ArrayList();
        ArrayList g = new ArrayList();
        ArrayList b = new ArrayList();
        try
        {
            File f = new File(file);
            FileInputStream dataIn = new FileInputStream(f);
            InputStreamReader streamReader = new InputStreamReader(dataIn);
            BufferedReader reader = new BufferedReader(streamReader);
            int count = 0;
            String line;
            while((line = reader.readLine()) != null) 
                try
                {
                    StringTokenizer tok = new StringTokenizer(line, ",");
                    if(tok.countTokens() != 3)
                        break;
                    r.add(new Integer(tok.nextToken()));
                    g.add(new Integer(tok.nextToken()));
                    b.add(new Integer(tok.nextToken()));
                    count++;
                }
                catch(Exception e) { }
            byte reds[] = new byte[count];
            byte greens[] = new byte[count];
            byte blues[] = new byte[count];
            byte alphas[] = new byte[count];
            for(int i = 0; i < count; i++)
            {
                reds[i] = (byte)(((Integer)r.get(i)).intValue() & 0xff);
                greens[i] = (byte)(((Integer)g.get(i)).intValue() & 0xff);
                blues[i] = (byte)(((Integer)b.get(i)).intValue() & 0xff);
            }

            for(int i = 0; i < count; i++)
                alphas[i] = -1;

            alphas[0] = 0;
            cmPos = new IndexColorModel(8, count, reds, greens, blues, alphas);
            r = new ArrayList();
            g = new ArrayList();
            b = new ArrayList();
            count = 0;
            while((line = reader.readLine()) != null) 
                try
                {
                    StringTokenizer tok = new StringTokenizer(line, ",");
                    if(tok.countTokens() != 3)
                        break;
                    r.add(new Integer(tok.nextToken()));
                    g.add(new Integer(tok.nextToken()));
                    b.add(new Integer(tok.nextToken()));
                    count++;
                }
                catch(Exception e) { }
            byte redsNeg[] = new byte[count];
            byte greensNeg[] = new byte[count];
            byte bluesNeg[] = new byte[count];
            byte alphasNeg[] = new byte[count];
            for(int i = 0; i < count; i++)
            {
                redsNeg[i] = (byte)(((Integer)r.get(i)).intValue() & 0xff);
                greensNeg[i] = (byte)(((Integer)g.get(i)).intValue() & 0xff);
                bluesNeg[i] = (byte)(((Integer)b.get(i)).intValue() & 0xff);
            }

            for(int i = 0; i < count; i++)
                alphasNeg[i] = -1;

            alphasNeg[count - 1] = 0;
            cmNeg = new IndexColorModel(8, count, redsNeg, greensNeg, bluesNeg, alphasNeg);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Unable to load colormap. Using default. " + e.getMessage(), "Error", 0);
            makeDefaultLUT();
            return;
        }
    }

    public void makeFireLUT()
    {
        int size = 256;
        byte reds[] = new byte[size];
        byte greens[] = new byte[size];
        byte blues[] = new byte[size];
        byte alphas[] = new byte[size];
        for(int i = 0; i < 256; i++)
        {
            reds[i] = (byte)(fire[i * 4 + 1] & 0xff);
            greens[i] = (byte)(fire[i * 4 + 2] & 0xff);
            blues[i] = (byte)(fire[i * 4 + 3] & 0xff);
        }

        for(int i = 0; i < size; i++)
            alphas[i] = -1;

        alphas[0] = 0;
        cmNeg = new IndexColorModel(8, 256, reds, greens, blues, alphas);
        cmPos = new IndexColorModel(8, 256, reds, greens, blues, alphas);
    }

    public void makeDefaultLUT()
    {
        int n = 32;
        int size = 256;
        int rp[] = {
            150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 
            250, 255, 255, 255, 255, 255, 255, 255, 255, 255, 
            255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 
            255, 255
        };
        int gp[] = {
            0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 
            80, 88, 96, 104, 112, 120, 128, 136, 144, 152, 
            160, 168, 176, 184, 192, 200, 208, 216, 224, 232, 
            240, 248, 255
        };
        int bp[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 25, 50, 75, 100, 125, 150, 
            175, 200
        };
        int rn[] = {
            200, 175, 150, 125, 100, 75, 50, 25, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0
        };
        int gn[] = {
            255, 255, 255, 245, 235, 225, 215, 205, 195, 185, 
            175, 160, 152, 144, 136, 128, 120, 112, 104, 96, 
            88, 80, 72, 64, 56, 48, 40, 32, 24, 16, 
            8, 0
        };
        int bn[] = {
            100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 
            200, 210, 220, 230, 240, 250, 255, 255, 255, 255, 
            255, 250, 240, 230, 220, 210, 200, 190, 180, 170, 
            160, 150
        };
        byte reds[] = new byte[size];
        byte greens[] = new byte[size];
        byte blues[] = new byte[size];
        byte alphas[] = new byte[size];
        double scale = (double)n / (double)size;
        for(int i = 0; i < size; i++)
        {
            int i1 = (int)((double)i * scale);
            int i2 = i1 + 1;
            if(i2 == n)
                i2 = n - 1;
            double fraction = (double)i * scale - (double)i1;
            reds[i] = (byte)(int)((1.0D - fraction) * (double)(rp[i1] & 0xff) + fraction * (double)(rp[i2] & 0xff));
            greens[i] = (byte)(int)((1.0D - fraction) * (double)(gp[i1] & 0xff) + fraction * (double)(gp[i2] & 0xff));
            blues[i] = (byte)(int)((1.0D - fraction) * (double)(bp[i1] & 0xff) + fraction * (double)(bp[i2] & 0xff));
        }

        for(int i = 0; i < size; i++)
            alphas[i] = (byte)alpha;

        alphas[0] = 0;
        cmPos = new IndexColorModel(8, size, reds, greens, blues, alphas);
        for(int i = 0; i < size; i++)
        {
            int i1 = (int)((double)i * scale);
            int i2 = i1 + 1;
            if(i2 == n)
                i2 = n - 1;
            double fraction = (double)i * scale - (double)i1;
            reds[i] = (byte)(int)((1.0D - fraction) * (double)(rn[i1] & 0xff) + fraction * (double)(rn[i2] & 0xff));
            greens[i] = (byte)(int)((1.0D - fraction) * (double)(gn[i1] & 0xff) + fraction * (double)(gn[i2] & 0xff));
            blues[i] = (byte)(int)((1.0D - fraction) * (double)(bn[i1] & 0xff) + fraction * (double)(bn[i2] & 0xff));
        }

        for(int i = 0; i < size; i++)
            alphas[i] = (byte)alpha;

        alphas[size - 1] = 0;
        cmNeg = new IndexColorModel(8, size, reds, greens, blues, alphas);
    }

    public void makeFlipLUT()
    {
        int n = 32;
        int size = 256;
        int rn[] = {
            255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 
            255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 
            255, 250, 240, 230, 220, 210, 200, 190, 180, 170, 
            160, 150
        };
        int gn[] = {
            255, 248, 240, 232, 224, 216, 208, 200, 192, 184, 
            176, 168, 160, 152, 144, 136, 128, 120, 112, 104, 
            96, 88, 80, 72, 64, 56, 48, 40, 32, 24, 
            16, 8, 0
        };
        int bn[] = {
            200, 175, 150, 125, 100, 75, 50, 25, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0
        };
        int rp[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 25, 50, 75, 100, 125, 150, 
            175, 200
        };
        int gp[] = {
            0, 8, 16, 24, 32, 40, 48, 56, 64, 72, 
            80, 88, 98, 104, 112, 120, 128, 136, 144, 152, 
            160, 175, 185, 195, 205, 215, 225, 235, 245, 255, 
            255, 255
        };
        int bp[] = {
            150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 
            250, 255, 255, 255, 255, 255, 250, 240, 230, 220, 
            210, 200, 190, 180, 170, 160, 150, 140, 130, 120, 
            110, 100
        };
        byte reds[] = new byte[size];
        byte greens[] = new byte[size];
        byte blues[] = new byte[size];
        byte alphas[] = new byte[size];
        double scale = (double)n / (double)size;
        for(int i = 0; i < size; i++)
        {
            int i1 = (int)((double)i * scale);
            int i2 = i1 + 1;
            if(i2 == n)
                i2 = n - 1;
            double fraction = (double)i * scale - (double)i1;
            reds[i] = (byte)(int)((1.0D - fraction) * (double)(rp[i1] & 0xff) + fraction * (double)(rp[i2] & 0xff));
            greens[i] = (byte)(int)((1.0D - fraction) * (double)(gp[i1] & 0xff) + fraction * (double)(gp[i2] & 0xff));
            blues[i] = (byte)(int)((1.0D - fraction) * (double)(bp[i1] & 0xff) + fraction * (double)(bp[i2] & 0xff));
        }

        for(int i = 0; i < size; i++)
            alphas[i] = (byte)alpha;

        alphas[0] = 0;
        cmPos = new IndexColorModel(8, size, reds, greens, blues, alphas);
        for(int i = 0; i < size; i++)
        {
            int i1 = (int)((double)i * scale);
            int i2 = i1 + 1;
            if(i2 == n)
                i2 = n - 1;
            double fraction = (double)i * scale - (double)i1;
            reds[i] = (byte)(int)((1.0D - fraction) * (double)(rn[i1] & 0xff) + fraction * (double)(rn[i2] & 0xff));
            greens[i] = (byte)(int)((1.0D - fraction) * (double)(gn[i1] & 0xff) + fraction * (double)(gn[i2] & 0xff));
            blues[i] = (byte)(int)((1.0D - fraction) * (double)(bn[i1] & 0xff) + fraction * (double)(bn[i2] & 0xff));
        }

        for(int i = 0; i < size; i++)
            alphas[i] = (byte)alpha;

        alphas[size - 1] = 0;
        cmNeg = new IndexColorModel(8, size, reds, greens, blues, alphas);
    }

    public void setAlpha(int a)
    {
        alpha = a;
        makeDefaultLUT();
    }

    public int getAlpha()
    {
        return alpha;
    }

    public IndexColorModel getPos()
    {
        return cmPos;
    }

    public IndexColorModel getNeg()
    {
        return cmNeg;
    }

    public String toString()
    {
        String out = super.toString();
        out = out + "\ncmPos: " + cmPos;
        out = out + "\ncmNeg: " + cmNeg;
        return out;
    }

    public static void main(String args[])
    {
        FIVColorModel fcm = new FIVColorModel();
        fcm.makeFireLUT();
        System.out.println(fcm);
        IndexColorModel cm = fcm.getPos();
        int size = cm.getMapSize();
        byte r[] = new byte[size];
        byte g[] = new byte[size];
        byte b[] = new byte[size];
        System.out.println("Index\tRed\tGreen\tBlue");
        int count = 0;
        cm = fcm.getNeg();
        cm.getReds(r);
        cm.getGreens(g);
        cm.getBlues(b);
        for(int i = 0; i < cm.getMapSize(); i++)
        {
            System.out.println(count + "\t" + (r[i] & 0xff) + "\t" + (g[i] & 0xff) + "\t" + (b[i] & 0xff));
            count++;
        }

        cm = fcm.getPos();
        cm.getReds(r);
        cm.getGreens(g);
        cm.getBlues(b);
        for(int i = 0; i < cm.getMapSize(); i++)
        {
            System.out.println(count + "\t" + (r[i] & 0xff) + "\t" + (g[i] & 0xff) + "\t" + (b[i] & 0xff));
            count++;
        }

    }

    IndexColorModel cmPos;
    IndexColorModel cmNeg;
    int alpha;
    int fire[] = {
        0, 0, 0, 31, 1, 0, 0, 34, 2, 0, 
        0, 38, 3, 0, 0, 42, 4, 0, 0, 46, 
        5, 0, 0, 49, 6, 0, 0, 53, 7, 0, 
        0, 57, 8, 0, 0, 61, 9, 0, 0, 65, 
        10, 0, 0, 69, 11, 0, 0, 74, 12, 0, 
        0, 78, 13, 0, 0, 82, 14, 0, 0, 87, 
        15, 0, 0, 91, 16, 1, 0, 96, 17, 4, 
        0, 100, 18, 7, 0, 104, 19, 10, 0, 108, 
        20, 13, 0, 113, 21, 16, 0, 117, 22, 19, 
        0, 121, 23, 22, 0, 125, 24, 25, 0, 130, 
        25, 28, 0, 134, 26, 31, 0, 138, 27, 34, 
        0, 143, 28, 37, 0, 147, 29, 40, 0, 151, 
        30, 43, 0, 156, 31, 46, 0, 160, 32, 49, 
        0, 165, 33, 52, 0, 168, 34, 55, 0, 171, 
        35, 58, 0, 175, 36, 61, 0, 178, 37, 64, 
        0, 181, 38, 67, 0, 185, 39, 70, 0, 188, 
        40, 73, 0, 192, 41, 76, 0, 195, 42, 79, 
        0, 199, 43, 82, 0, 202, 44, 85, 0, 206, 
        45, 88, 0, 209, 46, 91, 0, 213, 47, 94, 
        0, 216, 48, 98, 0, 220, 49, 101, 0, 220, 
        50, 104, 0, 221, 51, 107, 0, 222, 52, 110, 
        0, 223, 53, 113, 0, 224, 54, 116, 0, 225, 
        55, 119, 0, 226, 56, 122, 0, 227, 57, 125, 
        0, 224, 58, 128, 0, 222, 59, 131, 0, 220, 
        60, 134, 0, 218, 61, 137, 0, 216, 62, 140, 
        0, 214, 63, 143, 0, 212, 64, 146, 0, 210, 
        65, 148, 0, 206, 66, 150, 0, 202, 67, 152, 
        0, 199, 68, 154, 0, 195, 69, 156, 0, 191, 
        70, 158, 0, 188, 71, 160, 0, 184, 72, 162, 
        0, 181, 73, 163, 0, 177, 74, 164, 0, 173, 
        75, 166, 0, 169, 76, 167, 0, 166, 77, 168, 
        0, 162, 78, 170, 0, 158, 79, 171, 0, 154, 
        80, 173, 0, 151, 81, 174, 0, 147, 82, 175, 
        0, 143, 83, 177, 0, 140, 84, 178, 0, 136, 
        85, 179, 0, 132, 86, 181, 0, 129, 87, 182, 
        0, 125, 88, 184, 0, 122, 89, 185, 0, 118, 
        90, 186, 0, 114, 91, 188, 0, 111, 92, 189, 
        0, 107, 93, 190, 0, 103, 94, 192, 0, 100, 
        95, 193, 0, 96, 96, 195, 0, 93, 97, 196, 
        1, 89, 98, 198, 3, 85, 99, 199, 5, 82, 
        100, 201, 7, 78, 101, 202, 8, 74, 102, 204, 
        10, 71, 103, 205, 12, 67, 104, 207, 14, 64, 
        105, 208, 16, 60, 106, 209, 19, 56, 107, 210, 
        21, 53, 108, 212, 24, 49, 109, 213, 27, 45, 
        110, 214, 29, 42, 111, 215, 32, 38, 112, 217, 
        35, 35, 113, 218, 37, 31, 114, 220, 40, 27, 
        115, 221, 43, 23, 116, 223, 46, 20, 117, 224, 
        48, 16, 118, 226, 51, 12, 119, 227, 54, 8, 
        120, 229, 57, 5, 121, 230, 59, 4, 122, 231, 
        62, 3, 123, 233, 65, 3, 124, 234, 68, 2, 
        125, 235, 70, 1, 126, 237, 73, 1, 127, 238, 
        76, 0, 128, 240, 79, 0, 129, 241, 81, 0, 
        130, 243, 84, 0, 131, 244, 87, 0, 132, 246, 
        90, 0, 133, 247, 92, 0, 134, 249, 95, 0, 
        135, 250, 98, 0, 136, 252, 101, 0, 137, 252, 
        103, 0, 138, 252, 105, 0, 139, 253, 107, 0, 
        140, 253, 109, 0, 141, 253, 111, 0, 142, 254, 
        113, 0, 143, 254, 115, 0, 144, 255, 117, 0, 
        145, 255, 119, 0, 146, 255, 121, 0, 147, 255, 
        123, 0, 148, 255, 125, 0, 149, 255, 127, 0, 
        150, 255, 129, 0, 151, 255, 131, 0, 152, 255, 
        133, 0, 153, 255, 134, 0, 154, 255, 136, 0, 
        155, 255, 138, 0, 156, 255, 140, 0, 157, 255, 
        141, 0, 158, 255, 143, 0, 159, 255, 145, 0, 
        160, 255, 147, 0, 161, 255, 148, 0, 162, 255, 
        150, 0, 163, 255, 152, 0, 164, 255, 154, 0, 
        165, 255, 155, 0, 166, 255, 157, 0, 167, 255, 
        159, 0, 168, 255, 161, 0, 169, 255, 162, 0, 
        170, 255, 164, 0, 171, 255, 166, 0, 172, 255, 
        168, 0, 173, 255, 169, 0, 174, 255, 171, 0, 
        175, 255, 173, 0, 176, 255, 175, 0, 177, 255, 
        176, 0, 178, 255, 178, 0, 179, 255, 180, 0, 
        180, 255, 182, 0, 181, 255, 184, 0, 182, 255, 
        186, 0, 183, 255, 188, 0, 184, 255, 190, 0, 
        185, 255, 191, 0, 186, 255, 193, 0, 187, 255, 
        195, 0, 188, 255, 197, 0, 189, 255, 199, 0, 
        190, 255, 201, 0, 191, 255, 203, 0, 192, 255, 
        205, 0, 193, 255, 206, 0, 194, 255, 208, 0, 
        195, 255, 210, 0, 196, 255, 212, 0, 197, 255, 
        213, 0, 198, 255, 215, 0, 199, 255, 217, 0, 
        200, 255, 219, 0, 201, 255, 220, 0, 202, 255, 
        222, 0, 203, 255, 224, 0, 204, 255, 226, 0, 
        205, 255, 228, 0, 206, 255, 230, 0, 207, 255, 
        232, 0, 208, 255, 234, 0, 209, 255, 235, 4, 
        210, 255, 237, 8, 211, 255, 239, 13, 212, 255, 
        241, 17, 213, 255, 242, 21, 214, 255, 244, 26, 
        215, 255, 246, 30, 216, 255, 248, 35, 217, 255, 
        248, 42, 218, 255, 249, 50, 219, 255, 250, 58, 
        220, 255, 251, 66, 221, 255, 252, 74, 222, 255, 
        253, 82, 223, 255, 254, 90, 224, 255, 255, 98, 
        225, 255, 255, 105, 226, 255, 255, 113, 227, 255, 
        255, 121, 228, 255, 255, 129, 229, 255, 255, 136, 
        230, 255, 255, 144, 231, 255, 255, 152, 232, 255, 
        255, 160, 233, 255, 255, 167, 234, 255, 255, 175, 
        235, 255, 255, 183, 236, 255, 255, 191, 237, 255, 
        255, 199, 238, 255, 255, 207, 239, 255, 255, 215, 
        240, 255, 255, 223, 241, 255, 255, 227, 242, 255, 
        255, 231, 243, 255, 255, 235, 244, 255, 255, 239, 
        245, 255, 255, 243, 246, 255, 255, 247, 247, 255, 
        255, 251, 248, 255, 255, 255, 249, 255, 255, 255, 
        250, 255, 255, 255, 251, 255, 255, 255, 252, 255, 
        255, 255, 253, 255, 255, 255, 254, 255, 255, 255, 
        255, 255, 255, 255
    };
}
