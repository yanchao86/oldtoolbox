package com.pixshow.framework.storage.api;

import java.io.File;

/**
 * 
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since May 22, 2013
 *
 */
public interface Storage {

    /**
     * 同步上传
     * @param storageSpace
     * @param fileName
     * @param file
     * @param fileType
     * @return
     *
     */
    public String upload(String storageSpace, String fileName, File file, String fileType);

    /**
     * 异步上传
     * @param storageSpace
     * @param fileName
     * @param file
     * @param fileType
     * @return
     *
     */
    public String uploadAsync(String storageSpace, String fileName, File file, String fileType);
}
