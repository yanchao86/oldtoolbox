package com.pixshow.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.pixshow.framework.exception.api.SysException;

public class FileUtility extends FileUtils {

    protected static final Log log = LogFactory.getLog(FileUtility.class);

    public static Resource[] getResources(String locationPattern) {
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = new Resource[0];
        try {
            resources = patternResolver.getResources(locationPattern);
        } catch (IOException e) {
            throw new SysException(e);
        }
        return resources;
    }

    public static String getMD5(File file) {
        try {
            return DigestUtility.md5Hex(FileUtility.readFileToByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String splicePath(String... paths) {
        String fullPath = null;
        for (String path : paths) {
            path = path.replaceAll("\\\\", "/");
            if (fullPath == null) {
                fullPath = path;
            } else if (fullPath.endsWith("/")) {
                fullPath += (path.startsWith("/") ? path.substring(1) : path);
            } else {
                fullPath += (path.startsWith("/") ? path : ("/" + path));
            }
        }
        return fullPath;
    }

    public static File createTempDir(String prefix) {
        try {
            File dir = File.createTempFile(prefix, "dir");
            if (dir.delete()) {
                if (dir.mkdir()) { return dir; }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<File> unZip(ZipFile zipFile, File dir, String fileNameEncode) {
        List<File> files = new ArrayList<File>();
        try {
            Enumeration<ZipArchiveEntry> enumeration = zipFile.getEntries();
            while (enumeration.hasMoreElements()) {
                ZipArchiveEntry entry = enumeration.nextElement();
                String fileName = new String(entry.getRawName(), fileNameEncode);
                if (!fileName.endsWith("/")) {
                    File outFile = new File(dir, fileName);
                    InputStream io = zipFile.getInputStream(entry);
                    outFile.getParentFile().mkdirs();
                    OutputStream output = new FileOutputStream(outFile);
                    IOUtils.copy(io, output);
                    files.add(outFile);
                    IOUtils.closeQuietly(io);
                    IOUtils.closeQuietly(output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public static void zip(List<File> files, File outfile) throws Exception {
        OutputStream zip_output = new FileOutputStream(outfile);
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream logical_zip = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zip_output);
        for (File file : files) {
            /* Create Archieve entry - write header information */
            logical_zip.putArchiveEntry(new ZipArchiveEntry(file.getName()));
            /* Copy input file */
            IOUtils.copy(new FileInputStream(file), logical_zip);
            /* Close Archieve entry, write trailer information */
            logical_zip.closeArchiveEntry();
        }
        /* Finish addition of entries to the file */
        logical_zip.finish();
        /* Close output stream, our files are zipped */
        zip_output.close();
    }
    
}
