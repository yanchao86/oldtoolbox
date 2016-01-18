/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:ImageIm4j.java Project: LvWeatherManager
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 6, 2013 5:53:14 PM
 * 
 */
package com.pixshow.framework.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 6, 2013
 * 
 */
public class ImageUtility {

    public static class ImageInfo {
        public int  width;
        public int  height;
        public long size;

        @Override
        public String toString() {
            return String.format("width:%d,height:%d,size:%d", width, height, size);
        }
    }

    public static ImageInfo getImageInfo(String fileName) {
        FileInputStream inputStream = null;
        try {
            File image = new File(fileName);
            inputStream = new FileInputStream(image);
            BufferedImage sourceImg;
            sourceImg = ImageIO.read(inputStream);
            ImageInfo info = new ImageInfo();
            info.width = sourceImg.getWidth();
            info.height = sourceImg.getHeight();
            info.size = image.length();
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static ImageInfo getImageInfo4GM(String fileName) {
        try {
            IMOperation op = new IMOperation();
            op.format("%w,%h");
            op.addImage(1);
            IdentifyCmd identifyCmd = new IdentifyCmd(true);
            ArrayListOutputConsumer output = new ArrayListOutputConsumer();
            identifyCmd.setOutputConsumer(output);
            identifyCmd.run(op, fileName);
            ArrayList<String> cmdOutput = output.getOutput();
            String line = cmdOutput.get(0);
            String[] arr = line.split(",");
            ImageInfo info = new ImageInfo();
            info.width = Integer.valueOf(arr[0]);
            info.height = Integer.valueOf(arr[1]);
            //            info.size = Long.valueOf(arr[2]);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean cropCenterSquare(String inFileName, String outFileName, int width, double quality) {
        ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.addImage(inFileName);
        op.resize();
        op.addRawArgs(width + "x" + width + "^"); //"300x300^"
        op.quality(quality); //80.00
        op.gravity("Center");
        op.crop(width, width, 0, 0);
        op.addImage(outFileName);
        try {
            cmd.run(op);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean resize(String inFileName, String outFileName, String width, double quality) {
        ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.addImage(inFileName);
        op.resize();
        op.addRawArgs(width); //"300x"
        op.quality(quality); //80.00
        op.addImage(outFileName);
        try {
            cmd.run(op);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
