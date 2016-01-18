/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:UUIDUtility.java Project: LvFramework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Mar 13, 2013 11:33:23 AM
 * 
 */
package com.pixshow.framework.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Mar 13, 2013
 * 
 */

public class UUIDUtility {

    public static String uuid22f36(String uuid36) {
        UUID uuid = UUID.fromString(uuid36);
        byte[] binaryData = asBytes(uuid);
        return Base64.encodeBase64URLSafeString(binaryData);
    }

    public static String uuid22() {
        UUID uuid = UUID.randomUUID();
        byte[] binaryData = asBytes(uuid);
        return Base64.encodeBase64URLSafeString(binaryData);
    }

    public static String uuid32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String uuid36() {
        return UUID.randomUUID().toString();
    }

    public static String uuid36f22(String uuid22) {
        byte[] binaryData = Base64.decodeBase64(uuid22);
        UUID uuid = asUuid(binaryData);
        return uuid.toString();
    }

    private static UUID asUuid(byte[] binaryData) {
        ByteBuffer bb = ByteBuffer.wrap(binaryData);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    private static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

}
