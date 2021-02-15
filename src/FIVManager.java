// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FIVManager.java

import ij.*;
import ij.io.FileInfo;
import ij.io.FileOpener;
import ij.measure.Calibration;
import ij.process.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class FIVManager
    implements KeyListener, MouseListener, SurfaceConnectMaintainer
{

    public FIVManager()
    {
        serialString = "";
        functionalFilename = null;
        structuralFilename = null;
        serialEntry = 0;
        currentDirectory = null;
        surfacePort = -1;
        vImages = new Vector();
        slash = System.getProperty("file.separator");
        contrastAdjuster = new FIVContrastAdjuster(this);
        contrastAdjuster.setVisible(false);
    }

    public void initImageJ()
    {
        if(ij == null)
            ij = new FIVImageJ();
    }

    public void initImageJ(Applet a)
    {
        if(ij == null)
            ij = new FIVImageJ(a);
    }

    public void setToolWindow(FIVToolWindow t)
    {
        toolWindow = t;
    }

    public void setFilenames(String functional, String structural)
    {
        try
        {
            functionalFilename = functional.trim();
            functionalFilename = appendFilename(functionalFilename);
        }
        catch(Exception e)
        {
            functionalFilename = null;
        }
        try
        {
            structuralFilename = structural.trim();
            structuralFilename = appendFilename(structuralFilename);
        }
        catch(Exception e)
        {
            structuralFilename = null;
        }
    }

    public void toggleImagejVisibility()
    {
        if(ij.isVisible())
            ij.setVisible(false);
        else
            ij.setVisible(true);
    }

    public int getImageCount()
    {
        return vImages.size();
    }

    public String getDefaultMode()
    {
        FileInfo fi = new FileInfo();
        boolean success = false;
        if(functionalFilename == null && structuralFilename == null)
            return null;
        if(functionalFilename != null)
            try
            {
                success = readHeader(functionalFilename, fi);
            }
            catch(Exception e)
            {
                success = false;
            }
        else
        if(structuralFilename != null)
            try
            {
                success = readHeader(structuralFilename, fi);
            }
            catch(Exception e)
            {
                success = false;
            }
        if(!success)
        {
            FIVToolWindow.appendLog(1, "FIVManager: getDefaultMode:");
            FIVToolWindow.appendLog(1, "Unable to determine a default mode because the");
            FIVToolWindow.appendLog(1, "header file could not be read and not enough");
            FIVToolWindow.appendLog(1, "information could be gathered from the image file.");
            return null;
        }
        int voxSize = (int)fi.pixelWidth;
        String voxString = "";
        if(voxSize == 1)
            voxString = "111";
        else
        if(voxSize == 2)
            voxString = "222";
        else
        if(voxSize == 3)
        {
            voxString = "333";
        } else
        {
            FIVToolWindow.appendLog(1, "Unknown voxel size: " + voxSize);
            FIVToolWindow.appendLog(1, "Default mode could not be determined");
            return null;
        }
        return voxString;
    }

    public boolean isModeValid(String mode)
    {
        if(mode == null)
            return false;
        for(StringTokenizer tk = new StringTokenizer(PropReader.getString("modelist"), ","); tk.hasMoreTokens();)
            if(tk.nextToken().equalsIgnoreCase(mode))
                return true;

        return false;
    }

    public boolean showStartupImages()
    {
        int voxSize = 0;
        int voxSizeAnat = 0;
        int voxSizeFunc = 0;
        String modeName = PropReader.getString("mode");
        if(modeName == null)
            modeName = getDefaultMode();
        if(!isModeValid(modeName))
        {
            FIVModeErrorDialog ed = new FIVModeErrorDialog(toolWindow, true);
            ed.setTitle("Mode error");
            ed.show();
            modeName = ed.getMode();
            if(modeName == null)
            {
                FIVToolWindow.appendLog(1, "The mode was not set.");
                return false;
            }
        }
        PropReader.setInstanceProperty("mode", modeName);
        System.out.println("Using mode: ***" + modeName + "***");
        if(functionalFilename != null)
            toolWindow.setFunctionalFile(functionalFilename);
        else
            toolWindow.setFunctionalFile(" <no image loaded> ");
        if(structuralFilename != null)
        {
            System.out.println("Start image: structural file: " + structuralFilename);
            toolWindow.setStructuralFile(structuralFilename);
        } else
        {
            toolWindow.setStructuralFile(" <no image loaded> ");
        }
        try
        {
            if(PropReader.getString("mode." + modeName + ".transverse.montage").equals("on") && !show(3, 1, false))
                return false;
            if(PropReader.getString("mode." + modeName + ".coronal.montage").equals("on") && !show(1, 1, false))
                return false;
            if(PropReader.getString("mode." + modeName + ".sagittal.montage").equals("on") && !show(2, 1, false))
                return false;
            if(PropReader.getString("mode." + modeName + ".transverse.stack").equals("on") && !show(3, 2, false))
                return false;
            if(PropReader.getString("mode." + modeName + ".coronal.stack").equals("on") && !show(1, 2, false))
                return false;
            if(PropReader.getString("mode." + modeName + ".sagittal.stack").equals("on") && !show(2, 2, false))
                return false;
        }
        catch(Exception e)
        {
            FIVToolWindow.appendLog(1, "FIVManager: showStartupImages: The mode you selected is absent or corrupt.");
            FIVToolWindow.appendLog(1, "Startup images will not be shown");
            return false;
        }
        return true;
    }

    public boolean show(int view, int display, boolean radiologic)
    {
        String mode = PropReader.getString("mode");
        if(!isModeValid(mode))
        {
            FIVModeErrorDialog ed = new FIVModeErrorDialog(toolWindow, true);
            ed.setTitle("Mode error");
            ed.show();
            mode = ed.getMode();
            if(mode == null)
            {
                FIVToolWindow.appendLog(1, "The mode has not been properly set.");
                return false;
            }
            PropReader.setInstanceProperty("mode", mode);
        }
        if(functionalImage == null && functionalFilename != null)
        {
            System.out.println("loading file " + functionalFilename);
            functionalImage = load(functionalFilename);
            if(functionalImage == null)
            {
                FIVToolWindow.appendLog(1, "The functional image was not loaded properly.");
                FIVToolWindow.appendLog(1, "Please check format and permissions of the file.");
                return false;
            }
            String mask = PropReader.getString("mode." + mode + ".func.mask");
            System.out.println("load: " + mask);
            if(mask != null && !mask.equalsIgnoreCase("none") && !mask.equalsIgnoreCase(""))
            {
                System.out.println("load: Going to mask.");
                functionalImage = maskImage(functionalImage);
            }
        }
        if(structuralFilename == null && functionalImage != null)
        {
            int voxSize = (int)functionalImage.getCalibration().pixelWidth;
            String name = PropReader.getString("mode." + mode + ".defaultimage");
            if(!name.equals("none"))
            {
                structuralFilename = appendFilename(name);
                toolWindow.setStructuralFile(structuralFilename);
            } else
            {
                toolWindow.setStructuralFile("<No default image>");
            }
        }
        if(structuralImage == null && structuralFilename != null)
        {
            System.out.println("show: structural file: " + structuralFilename);
            structuralImage = load(structuralFilename);
            if(structuralImage == null)
            {
                FIVToolWindow.appendLog(1, "The structural image was not loaded properly.");
                FIVToolWindow.appendLog(1, "Please check format and permissions of the file.");
                return false;
            }
        }
        if(structuralImage == null && functionalImage == null)
        {
            FIVToolWindow.appendLog(1, "No image files were loaded. Please check format and permissions of the files.");
            return false;
        }
        FIVImage im = new FIVImage(functionalImage, structuralImage, this);
        int opac = PropReader.getInt("mode." + mode + ".func.opacity", 100);
        im.setAlpha((opac * 255) / 100);
        im.setToolWindow(toolWindow);
        boolean success = im.show(view, display, radiologic);
        if(!success)
            return false;
        if(toolWindow != null)
        {
            im.setThreshold(toolWindow.getMinThreshold(), toolWindow.getMaxThreshold(), toolWindow.getThresholdSign());
            im.setSync(toolWindow.isLockDown());
        } else
        {
            String s = PropReader.getString("mode." + mode + ".func.sign");
            int sign;
            if(s.indexOf("both") > -1)
                sign = 3;
            else
            if(s.indexOf("pos") > -1)
                sign = 1;
            else
            if(s.indexOf("neg") > -1)
                sign = 2;
            else
            if(s.indexOf("none") > -1 || s.indexOf("neither") > -1)
                sign = 4;
            else
                sign = 3;
            im.setThreshold(PropReader.getDouble("mode." + mode + ".func.low", 5D), PropReader.getDouble("mode." + mode + ".func.high", 12D), sign);
            im.setSync(true);
        }
        if(syncCoords != null)
        {
            System.out.println("Setting coords to: " + im.getCoords());
            im.setCoords(syncCoords);
        } else
        {
            System.out.println("No coords available");
        }
        vImages.addElement(im);
        currentImage = im;
        return true;
    }

    public void setSyncCoords(Vector v)
    {
        if(syncCoords == null)
            syncCoords = new FIVCoords();
        int x = ((Integer)v.elementAt(0)).intValue();
        int y = ((Integer)v.elementAt(1)).intValue();
        int z = ((Integer)v.elementAt(2)).intValue();
        syncCoords.setTalFromTal(x, y, z);
        System.out.println("syncCoords: " + syncCoords);
    }

    public static void addImage(FIVImage im)
    {
        vImages.addElement(im);
    }

    public static void removeImage(FIVImage im)
    {
        vImages.removeElement(im);
        if(vImages.isEmpty() && !toolWindow.isVisible())
            System.exit(0);
    }

    public static void setCurrentImage(FIVImage im)
    {
        currentImage = im;
        contrastAdjuster.setMaximum(currentImage.getMaxIntensity());
        contrastAdjuster.setMinimum(currentImage.getMinIntensity());
    }

    public static Point getWindowPos(FIVImage im)
    {
        int count = 0;
        int offset = 30;
        Rectangle toolPos;
        if(toolWindow != null)
            toolPos = toolWindow.getBounds();
        else
            toolPos = new Rectangle(100, 100, 100, 50);
        int display = im.getDisplay();
        for(int i = 0; i < vImages.size(); i++)
            if(((FIVImage)vImages.elementAt(i)).getDisplay() == display)
                count++;

        return new Point(toolPos.x + count * offset, toolPos.y + toolPos.height + count * offset);
    }

    public static boolean isCrosshairVisible()
    {
        return crosshairVisible;
    }

    public static boolean isContrastAdjustable()
    {
        return contrastAdjustable;
    }

    public static boolean isContinuousUpdate()
    {
        return continuousUpdate;
    }

    public void toFront()
    {
        for(int i = 0; i < vImages.size(); i++)
        {
            FIVImage im = (FIVImage)vImages.elementAt(i);
            im.getWindow().disableActivation(true);
            im.getWindow().toFront();
        }

        currentImage.getWindow().toFront();
        toolWindow.toFront();
    }

    public void doCommand(String command, Vector args)
    {
        if(command.equalsIgnoreCase("Crosshair"))
            crosshairVisible = !crosshairVisible;
        else
        if(command.equalsIgnoreCase("Contrast"))
        {
            contrastAdjustable = !contrastAdjustable;
            if(currentImage != null)
            {
                contrastAdjuster.setMinimum(currentImage.getMinIntensity());
                contrastAdjuster.setMaximum(currentImage.getMaxIntensity());
            }
            Point loc = toolWindow.getLocationOnScreen();
            contrastAdjuster.setLocationRelativeTo(toolWindow);
            contrastAdjuster.setVisible(contrastAdjustable);
        } else
        if(command.equalsIgnoreCase("File"))
            selectFile(((String)args.elementAt(0)).toString());
        else
        if(command.equalsIgnoreCase("Save"))
        {
            if(currentImage != null)
                currentImage.save();
            else
                FIVToolWindow.appendLog(1, "There is no image to save.");
        } else
        if(command.equalsIgnoreCase("Print"))
        {
            if(currentImage != null)
                currentImage.print();
            else
                FIVToolWindow.appendLog(1, "There is no image to print.");
        } else
        if(command.equalsIgnoreCase("Sync"))
        {
            for(int i = 0; i < vImages.size(); i++)
            {
                FIVImage im = (FIVImage)vImages.elementAt(i);
                Boolean b = (Boolean)args.elementAt(0);
                im.setSync(b.booleanValue());
            }

        } else
        if(command.equalsIgnoreCase("Scale"))
        {
            for(int i = 0; i < vImages.size(); i++)
            {
                FIVImage im = (FIVImage)vImages.elementAt(i);
                if(im.isSynced() || im == currentImage)
                    FIVToolWindow.appendLog(2, "Rescale the image...");
            }

        } else
        if(command.equalsIgnoreCase("Threshold"))
        {
            Double min = (Double)args.elementAt(0);
            Double max = (Double)args.elementAt(1);
            Integer sign = (Integer)args.elementAt(2);
            for(int i = 0; i < vImages.size(); i++)
            {
                FIVImage im = (FIVImage)vImages.elementAt(i);
                if(im.isSynced() || im == currentImage)
                    im.setThreshold(min.doubleValue(), max.doubleValue(), sign.intValue());
            }

        } else
        if(command.equalsIgnoreCase("Coords"))
        {
            int x = ((Integer)args.elementAt(0)).intValue();
            int y = ((Integer)args.elementAt(1)).intValue();
            int z = ((Integer)args.elementAt(2)).intValue();
            currentImage.setCoords(x, y, z);
            syncToMouseClick(null);
            if(currentImage != null)
                toolWindow.setThresholdValue(currentImage.getActivationLevel());
        }
    }

    public void saveCurrentImage(String f)
    {
        if(currentImage != null)
            currentImage.save(f);
        else
            System.out.println("ERROR: No image has been loaded.");
    }

    public void syncToMouseClick(FIVImage clickedImage)
    {
        if(vImages == null)
            return;
        if(clickedImage != null)
            syncCoords = clickedImage.getCoords();
        else
            syncCoords = currentImage.getCoords();
        for(int i = 0; i < vImages.size(); i++)
        {
            FIVImage im = (FIVImage)vImages.elementAt(i);
            if(im == currentImage || im.isSynced())
            {
                im.setCoords(syncCoords);
                im.updateSlice();
                if(isCrosshairVisible())
                    im.updateCrosshairs();
            }
        }

        currentImage.printCoords();
    }

    public void syncToMouseMotion(FIVImage motionImage)
    {
        if(vImages == null)
            return;
        FIVCoords coords;
        if(motionImage != null)
            coords = motionImage.getCoords();
        else
            coords = currentImage.getCoords();
        for(int i = 0; i < vImages.size(); i++)
        {
            FIVImage im = (FIVImage)vImages.elementAt(i);
            im.setCoords(coords);
            if((im == currentImage || im.isSynced()) && isCrosshairVisible())
                im.updateCrosshairs();
        }

        currentImage.printCoords();
    }

    public void syncToScroll()
    {
        if(vImages == null)
            return;
        FIVCoords coords = currentImage.getCoords();
        int currentView = currentImage.getView();
        for(int i = 0; i < vImages.size(); i++)
        {
            FIVImage im = (FIVImage)vImages.elementAt(i);
            if(im == currentImage || im.isSynced())
            {
                im.setCoords(coords);
                if(im.getView() == currentView)
                    im.updateSlice();
                else
                if(isCrosshairVisible())
                    im.updateCrosshairs();
            }
        }

        currentImage.printCoords();
    }

    public void syncToIntensity(double min, double max)
    {
        if(vImages == null)
            return;
        for(int i = 0; i < vImages.size(); i++)
        {
            FIVImage im = (FIVImage)vImages.elementAt(i);
            if(im == currentImage || im.isSynced())
                im.setIntensity(min, max);
        }

        if(currentImage != null)
        {
            contrastAdjuster.setMinimum(currentImage.getMinIntensity());
            contrastAdjuster.setMaximum(currentImage.getMaxIntensity());
        }
    }

    public void selectFile(String s)
    {
        FIVFileChooser chooser = new FIVFileChooser(toolWindow, true);
        if(currentDirectory == null)
        {
            String cd = System.getProperty("user.dir");
            currentDirectory = new File(cd);
        }
        chooser.setCurrentDirectory(currentDirectory);
        chooser.show();
        if(chooser.getExitState() == 0)
            return;
        File file = chooser.getFile();
        if(file == null)
            return;
        String filename = "file:///" + file.getAbsolutePath();
        currentDirectory = chooser.getCurrentDirectory();
        if(filename == null)
            return;
        if(s.equalsIgnoreCase("structural"))
        {
            structuralFilename = filename;
            structuralImage = null;
            toolWindow.setStructuralFile(filename);
        } else
        {
            functionalFilename = filename;
            functionalImage = null;
            toolWindow.setFunctionalFile(filename);
        }
        String newMode = chooser.getMode();
        String currentMode = PropReader.getString("mode");
        if(newMode.equalsIgnoreCase("current") && currentMode == null)
        {
            System.out.println("Warning: using default mode since there is no current mode.");
            newMode = "default";
        } else
        if(newMode.equalsIgnoreCase("current"))
            return;
        if(newMode.equalsIgnoreCase("default"))
            newMode = getDefaultMode();
        PropReader.setInstanceProperty("mode", newMode);
        toolWindow.updateSettings();
    }

    public void close()
    {
    }

    public String appendFilename(String f)
    {
        if(!f.endsWith(".img"))
        {
            if(!f.endsWith(".4dfp"))
                f = f + ".4dfp";
            f = f + ".img";
        }
        if(!f.startsWith("file:///"))
            f = "file:///" + f;
        return f;
    }

    public ImagePlus load(String filename)
    {
        System.out.println("Loading image");
        FileInfo fi = new FileInfo();
        if(filename == null)
            return null;
        try
        {
            System.out.println("REading header");
            readHeader(filename, fi);
        }
        catch(IOException e)
        {
            FIVToolWindow.appendLog(1, new String("Error loading image header file: " + e.getMessage()));
        }
        fi.url = filename.substring(0, filename.lastIndexOf("/") + 1);
        fi.fileName = filename.substring(filename.lastIndexOf("/") + 1);
        fi.fileFormat = 1;
        fi.unit = "pixels";
        System.out.println(fi);
        FileOpener fo = new FileOpener(fi);
        ImagePlus image = fo.open(false);
        if(image == null)
        {
            FIVToolWindow.appendLog(1, "The image file was not loaded.");
            return null;
        }
        Calibration c = new Calibration();
        c.pixelWidth = fi.pixelWidth;
        c.pixelHeight = fi.pixelHeight;
        c.pixelDepth = fi.pixelDepth;
        c.setUnit(fi.unit);
        image.setCalibration(c);
        int lastSlash = filename.lastIndexOf("/") + 1;
        if(lastSlash <= 0)
            lastSlash = filename.lastIndexOf("\\") + 1;
        if(lastSlash <= 0)
            lastSlash = 0;
        String title = filename.substring(lastSlash);
        image.setTitle(title);
        return image;
    }

    public boolean readHeader(String origFilename, FileInfo fi)
        throws IOException
    {
        System.out.println("Reading header:" + fi);
        String filename = origFilename;
        if(filename.endsWith(".img"))
            filename = filename.substring(0, filename.length() - 4);
        if(!filename.endsWith(".4dfp"))
            filename = filename + ".4dfp";
        filename = filename + ".ifh";
        Scanner s = new Scanner(filename);
        try
        {
            s.open();
            try
            {
                int pixelDepth = (new Integer(s.getDelimitedString("number of bytes per pixel", ":="))).intValue();
                if(pixelDepth == 1)
                    fi.fileType = 0;
                else
                if(pixelDepth == 4)
                    fi.fileType = 4;
                fi.pixelWidth = (new Double(s.getDelimitedString("scaling factor (mm/pixel) [1]", ":="))).doubleValue();
                fi.pixelHeight = (new Double(s.getDelimitedString("scaling factor (mm/pixel) [2]", ":="))).doubleValue();
                fi.pixelDepth = (new Double(s.getDelimitedString("scaling factor (mm/pixel) [3]", ":="))).doubleValue();
                fi.width = (new Integer(s.getDelimitedString("matrix size [1]", ":="))).intValue();
                fi.height = (new Integer(s.getDelimitedString("matrix size [2]", ":="))).intValue();
                fi.nImages = (new Integer(s.getDelimitedString("matrix size [3]", ":="))).intValue();
                String byteOrder = s.getDelimitedString("imagedata byte order", ":=");
                System.out.println("***Byte order: " + byteOrder);
                if(byteOrder.equals("littleendian"))
                {
                    System.out.println("Using little endian");
                    fi.intelByteOrder = true;
                } else
                {
                    fi.intelByteOrder = false;
                }
                return true;
            }
            catch(NumberFormatException e)
            {
                FIVToolWindow.appendLog(1, new String("FIVManager: readHeader: error reading header file values: " + e));
                FIVToolWindow.appendLog(2, new String("Attempting to determine appropriate dimensions..."));
            }
            catch(NullPointerException e)
            {
                FIVToolWindow.appendLog(1, new String("FIVManager: readHeader: error reading header file values(empty header?): " + e));
                FIVToolWindow.appendLog(2, new String("Attempting to determine appropriate dimensions..."));
            }
        }
        catch(IOException e)
        {
            FIVToolWindow.appendLog(1, new String("FIVManager: readHeader: error opening header file " + filename + ": " + e));
            FIVToolWindow.appendLog(2, new String("Attempting to determine appropriate dimensions from file name..."));
        }
        if(filename.indexOf("111") >= 0)
        {
            FIVToolWindow.appendLog(2, "Using standard 111 image parameters");
            fi.fileType = 4;
            fi.pixelWidth = 1.0D;
            fi.pixelHeight = 1.0D;
            fi.pixelDepth = 1.0D;
            fi.width = 176;
            fi.height = 208;
            fi.nImages = 176;
            return true;
        }
        if(filename.indexOf("222") >= 0)
        {
            FIVToolWindow.appendLog(2, "Using standard 222 image parameters");
            fi.fileType = 4;
            fi.pixelWidth = 2D;
            fi.pixelHeight = 2D;
            fi.pixelDepth = 2D;
            fi.width = 128;
            fi.height = 128;
            fi.nImages = 75;
            return true;
        }
        if(filename.indexOf("333") >= 0)
        {
            FIVToolWindow.appendLog(2, "Using standard 333 image parameters");
            fi.fileType = 4;
            fi.pixelWidth = 3D;
            fi.pixelHeight = 3D;
            fi.pixelDepth = 3D;
            fi.width = 48;
            fi.height = 64;
            fi.nImages = 48;
            return true;
        }
        FIVToolWindow.appendLog(2, "Attempting to determine appropriate dimensions from file size...");
        URL u = new URL(origFilename);
        if(u != null)
        {
            if(u.openConnection().getContentLength() == 0x1894000)
            {
                FIVToolWindow.appendLog(2, "Using 111 space...");
                fi.fileType = 4;
                fi.pixelWidth = 1.0D;
                fi.pixelHeight = 1.0D;
                fi.pixelDepth = 1.0D;
                fi.width = 176;
                fi.height = 208;
                fi.nImages = 176;
                return true;
            }
            if(u.openConnection().getContentLength() == 0x4b0000)
            {
                FIVToolWindow.appendLog(2, "Using 222 space...");
                fi.fileType = 4;
                fi.pixelWidth = 2D;
                fi.pixelHeight = 2D;
                fi.pixelDepth = 2D;
                fi.width = 128;
                fi.height = 128;
                fi.nImages = 75;
                return true;
            }
            if(u.openConnection().getContentLength() == 0x90000)
            {
                FIVToolWindow.appendLog(2, "Using 333 space...");
                fi.fileType = 4;
                fi.pixelWidth = 3D;
                fi.pixelHeight = 3D;
                fi.pixelDepth = 3D;
                fi.width = 48;
                fi.height = 64;
                fi.nImages = 48;
                return true;
            }
        }
        FIVToolWindow.appendLog(1, "Voxel size could not be determined from header file, file name, or file size.");
        FIVToolWindow.appendLog(1, "Are you sure the selected file is a valid image?");
        FIVToolWindow.appendLog(1, "Attempting to use standard 222 space...");
        fi.pixelWidth = 2D;
        fi.pixelHeight = 2D;
        fi.pixelDepth = 2D;
        fi.width = 128;
        fi.height = 128;
        fi.nImages = 75;
        return false;
    }

    public void mouseEntered(MouseEvent me)
    {
        System.out.println("Manager got a mouse enter: " + me);
    }

    public void keyPressed(KeyEvent e)
    {
        int keycode = e.getKeyCode();
        char keyChar = e.getKeyChar();
        if(keycode == 61 || keycode == 34)
        {
            currentImage.sliceIncrement(1);
            syncToScroll();
        } else
        if(keycode == 45 || keycode == 33)
        {
            currentImage.sliceDecrement(1);
            syncToScroll();
        } else
        if(keycode == 36)
        {
            currentImage.sliceTop();
            syncToScroll();
        } else
        if(keycode == 35)
        {
            currentImage.sliceBottom();
            syncToScroll();
        } else
        if(keycode == 9 || keycode == 32)
            currentImage.showPopup();
        else
        if(keycode == 87)
            currentImage.logCoords();
        else
        if(keycode == 37)
        {
            for(int i = 0; i < vImages.size(); i++)
            {
                FIVImage im = (FIVImage)vImages.elementAt(i);
                if(im.isSynced() || im == currentImage)
                    im.adjustBrightness(-0.025000000000000001D);
            }

        } else
        if(keycode == 39)
        {
            for(int i = 0; i < vImages.size(); i++)
            {
                FIVImage im = (FIVImage)vImages.elementAt(i);
                if(im.isSynced() || im == currentImage)
                    im.adjustBrightness(0.025000000000000001D);
            }

        } else
        if(keycode == 38)
        {
            for(int i = 0; i < vImages.size(); i++)
            {
                FIVImage im = (FIVImage)vImages.elementAt(i);
                if(im.isSynced() || im == currentImage)
                    im.adjustContrast(-0.050000000000000003D);
            }

        } else
        if(keycode == 40)
        {
            for(int i = 0; i < vImages.size(); i++)
            {
                FIVImage im = (FIVImage)vImages.elementAt(i);
                if(im.isSynced() || im == currentImage)
                    im.adjustContrast(0.050000000000000003D);
            }

        } else
        if(keycode == 65 && e.isControlDown())
            toolWindow.setThresholdSign(0);
        else
        if(keycode == 84 && e.isControlDown())
        {
            if(toolWindow != null)
                toolWindow.show(!toolWindow.isVisible());
        } else
        if(keycode == 73 && e.isControlDown())
        {
            for(int i = 0; i < vImages.size(); i++)
            {
                FIVImage im = (FIVImage)vImages.elementAt(i);
                if(im.isSynced() || im == currentImage)
                    im.setIntensityToDefault();
            }

        } else
        if(keycode == 76 && e.isControlDown())
        {
            TextField tf = (TextField)toolWindow.getMinThresholdField();
            tf.selectAll();
            serialEntry = 1;
            serialString = "";
        } else
        if(keycode == 72 && e.isControlDown())
        {
            TextField tf = (TextField)toolWindow.getMaxThresholdField();
            tf.selectAll();
            serialEntry = 2;
            serialString = "";
        } else
        if(keycode == 66 && e.isControlDown())
            continuousUpdate = !continuousUpdate;
        else
        if(keycode == 82 && e.isControlDown())
            toFront();
        else
        if(keycode == 83 && e.isControlDown())
            currentImage.save();
        else
        if(keycode == 80 && e.isControlDown())
            currentImage.print();
        else
        if(keycode == 10)
            try
            {
                double val = Double.valueOf(serialString).doubleValue();
                TextField tf;
                if(serialEntry == 1)
                    tf = (TextField)toolWindow.getMinThresholdField();
                else
                if(serialEntry == 2)
                    tf = (TextField)toolWindow.getMaxThresholdField();
                else
                    return;
                tf.setSelectionEnd(0);
                tf.dispatchEvent(new ActionEvent(tf, 1001, Double.toString(val), 1001));
            }
            catch(NumberFormatException ne)
            {
                System.out.println("Text entry not a valid number: " + serialString);
                System.out.println(ne);
            }
        else
        if(keycode == 8 || keycode == 127)
        {
            TextField tf;
            if(serialEntry == 1)
                tf = (TextField)toolWindow.getMinThresholdField();
            else
            if(serialEntry == 2)
                tf = (TextField)toolWindow.getMaxThresholdField();
            else
                return;
            serialString = serialString.substring(0, serialString.length() - 1);
            tf.setText(serialString);
            tf.selectAll();
        } else
        if(Character.isDigit(keyChar) || keyChar == '.')
        {
            if(serialEntry != 1 && serialEntry != 2)
            {
                currentImage.scale(Integer.parseInt(String.valueOf(keyChar)));
                return;
            }
            TextField tf;
            if(serialEntry == 1)
                tf = (TextField)toolWindow.getMinThresholdField();
            else
            if(serialEntry == 2)
                tf = (TextField)toolWindow.getMaxThresholdField();
            else
                return;
            serialString += keyChar;
            tf.setText(serialString);
            tf.selectAll();
        }
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    public void runSurface()
    {
        surfaceFrame = new SurfaceConnectFrame(this);
        surfaceFrame.show();
    }

    public void startSurfaceConnection()
    {
        if(surfaceThread == null)
            surfaceThread = new SurfaceConnectThread(this);
        if(surfaceThread.isAlive())
            surfaceThread.stop();
        surfaceThread.start();
        surfaceFrame.connectionStarted();
    }

    public void stopSurfaceConnection()
    {
        if(surfaceThread != null && surfaceThread.isAlive())
            surfaceThread.stopThread();
        if(surfaceFrame != null)
            surfaceFrame.connectionClosed();
    }

    public void completedSurfaceConnection()
    {
        surfaceFrame.connectionEstablished();
    }

    public boolean isSurfaceConnectionRunning()
    {
        if(surfaceThread == null)
            return false;
        return surfaceThread.isAlive();
    }

    public Point3d getCoordsForSurface()
    {
        if(syncCoords != null)
            return syncCoords.getPosTal();
        else
            return null;
    }

    public void printCoordsFromSurface()
    {
        if(surfaceFrame == null);
    }

    public void printMessageFromSurface(String m)
    {
        surfaceFrame.appendMessage(m);
    }

    public void setCoordsFromSurface(int x, int y, int z)
    {
        surfaceFrame.appendMessage("x: " + x + "\ty: " + y + "\tz: " + z);
        toolWindow.setCoords(x, y, z, 0.0D);
        for(int i = 0; i < vImages.size(); i++)
        {
            FIVImage im = (FIVImage)vImages.elementAt(i);
            if(im == currentImage || im.isSynced())
            {
                im.setCoords(x, y, z);
                im.updateSlice();
                if(isCrosshairVisible())
                    im.updateCrosshairs();
            }
        }

    }

    public int getSurfacePort()
    {
        if(surfacePort < 0)
            surfacePort = 6500;
        return surfacePort;
    }

    public String getSurfaceHost()
    {
        try
        {
            InetAddress localaddr = InetAddress.getLocalHost();
            return localaddr.getHostAddress();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "Unknown";
    }

    public ImagePlus maskImage(ImagePlus toMask)
    {
        String displayMode = PropReader.getString("mode");
        String maskFile = PropReader.getString("mode." + displayMode + ".func.mask");
        ImagePlus mask = load("file:///" + maskFile);
        (new StackConverter(mask)).convertToGray8();
        int size = mask.getStack().getSize();
        for(int i = 1; i <= size; i++)
        {
            ImageProcessor sp = mask.getStack().getProcessor(i);
            sp.threshold(0);
            sp.multiply(0.0039215686274509803D);
        }

        (new StackConverter(mask)).convertToGray32();
        int mode = 5;
        ImageStack stack1 = toMask.getStack();
        StackProcessor sp = new StackProcessor(stack1, toMask.getProcessor());
        try
        {
            if(mask.getStackSize() == 1)
                sp.copyBits(mask.getProcessor(), 0, 0, mode);
            else
                sp.copyBits(mask.getStack(), 0, 0, mode);
        }
        catch(IllegalArgumentException e)
        {
            IJ.error("\"" + toMask.getTitle() + "\": " + e.getMessage());
            return null;
        }
        toMask.setStack(null, stack1);
        if(toMask.getType() != 0)
            toMask.getProcessor().resetMinAndMax();
        return toMask;
    }

    private static final int SERIAL_NONE = 0;
    private static final int SERIAL_MIN = 1;
    private static final int SERIAL_MAX = 2;
    private String serialString;
    private String functionalFilename;
    private String structuralFilename;
    private int serialEntry;
    private ImagePlus functionalImage;
    private ImagePlus structuralImage;
    private FIVImageJ ij;
    private static FIVToolWindow toolWindow;
    private static FIVContrastAdjuster contrastAdjuster;
    private static Vector vImages;
    private static FIVImage currentImage;
    private Point prevPosition;
    private static boolean crosshairVisible = true;
    private static boolean contrastAdjustable = false;
    private static boolean continuousUpdate = false;
    private File currentDirectory;
    private String slash;
    private SurfaceConnectThread surfaceThread;
    private SurfaceConnectFrame surfaceFrame;
    private int surfacePort;
    private FIVCoords syncCoords;

}
