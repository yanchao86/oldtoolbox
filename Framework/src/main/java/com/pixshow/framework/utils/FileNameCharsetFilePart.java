/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:FileNameFilePart.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Feb 26, 2013 12:05:03 PM
 * 
 */
package com.pixshow.framework.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.util.EncodingUtil;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Feb 26, 2013
 * 
 */

public class FileNameCharsetFilePart extends FilePart {

    /** Attachment's file name as a byte array */
    private static final byte[] FILE_NAME_BYTES = EncodingUtil.getAsciiBytes(FILE_NAME);

    private static final String DEFAULT_CHARSET = "UTF-8";

    public FileNameCharsetFilePart(String name, File file) throws FileNotFoundException {
        this(name, file, DEFAULT_CHARSET);
    }

    public FileNameCharsetFilePart(String name, File file, String charset) throws FileNotFoundException {
        super(name, file, null, charset);
    }

    public FileNameCharsetFilePart(String name, String fileName, File file) throws FileNotFoundException {
        this(name, fileName, file, DEFAULT_CHARSET);
    }

    public FileNameCharsetFilePart(String name, String fileName, File file, String charset) throws FileNotFoundException {
        super(name, fileName, file, null, charset);
    }

    @Override
    protected void sendDispositionHeader(OutputStream out) throws IOException {
        super.sendDispositionHeader(out);
        String filename = getSource().getFileName();
        if (filename != null) {
            out.write(FILE_NAME_BYTES);
            out.write(QUOTE_BYTES);
            out.write(EncodingUtil.getBytes(filename, getCharSet()));
            out.write(QUOTE_BYTES);
        }
    }
}
