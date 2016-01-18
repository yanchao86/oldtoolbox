/*
 * Copyright (c) 2010-2012 300.cn All Rights Reserved
 *
 * File:CipherUtility.java Project: Framework
 * 
 * Creator:<a href="mailto:jifangliang@163.com">Time</a> 
 * Date:Oct 17, 2012 1:47:46 PM
 * 
 */
package com.pixshow.framework.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 
 * 
 * @author <a href="mailto:jifangliang@163.com">Time</a>
 * @author $Author: jifangliang.300.cn $
 * @version $Revision: 1.2 $ $Date: 2012/10/22 05:33:52 $
 * @since Oct 17, 2012
 * 
 */

public class CipherUtility {
    /**
     * <h1>AES对称加密算法 </h1>
     * <hr>
     * 这里演示的是其Java6.0的实现,理所当然的BouncyCastle也支持AES对称加密算，另外,我们也可以以AES算法实现为参考,完成RC2,RC4和Blowfish算法的实现
     * <hr>
     * 由于DES的不安全性以及DESede算法的低效,于是催生了AES算法(Advanced Encryption Standard) <br>
     * 该算法比DES要快,安全性高,密钥建立时间短,灵敏性好,内存需求低,在各个领域应用广泛 <br>
     * 目前,AES算法通常用于移动通信系统以及一些软件的安全外壳,还有一些无线路由器中也是用AES算法构建加密协议<br>
     * <hr>
     * 由于Java6.0支持大部分的算法,但受到出口限制,其密钥长度不能满足需求<br>
     * 所以特别需要注意的是:如果使用256位的密钥,则需要无政策限制文件(Unlimited Strength Jurisdiction Policy Files) <br>
     * 不过Sun是通过权限文件local_poblicy.jar和US_export_policy.jar做的相应限制,我们可以在Sun官网下载替换文件,减少相关限制<br>
     * 网址为http://www.oracle.com/technetwork/java/javase/downloads/index.html <br>
     * 在该页面的最下方找到Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 6,<br>
     * 点击下载 http://download.oracle.com/otn-pub/java/jce_policy/6/jce_policy-6.zip<br>
     * http://download.oracle.com/otn-pub/java/jce/7/UnlimitedJCEPolicyJDK7.zip <br>
     * 然后覆盖本地JDK目录和JRE目录下的security目录下的文件即可<br>
     * <hr>
     * 
     * @author <a href="mailto:jifangliang@163.com">Time</a>
     * @author $Author: jifangliang.300.cn $
     * @version $Revision: 1.2 $ $Date: 2012/10/22 05:33:52 $
     * @since Oct 17, 2012
     */
    public static class AES {
        // 密钥算法
        public static final String KEY_ALGORITHM = "AES";
        // 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
        public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

        /**
         * 生成密钥
         */
        protected static Key getKey(String password) throws Exception {
            // KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM); // 实例化密钥生成器
            // kg.init(128, new SecureRandom(password.getBytes()));// 初始化密钥生成器:AES要求密钥长度为128,192,256位
            // SecretKey secretKey = kg.generateKey(); // 生成密钥
            return new SecretKeySpec(DigestUtility.md5(password.getBytes()), KEY_ALGORITHM); // MD5 128bit
        }

        /**
         * 加密数据
         * 
         * @param data
         * @param password
         * @return
         */
        public static byte[] encrypt(byte[] data, String password) {
            try {
                Key k = getKey(password);// 还原密钥
                // 使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)
                // Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM); // 实例化Cipher对象，它用于完成实际的加密操作
                cipher.init(Cipher.ENCRYPT_MODE, k); // 初始化Cipher对象，设置为加密模式
                byte[] bytes = cipher.doFinal(data);
                return bytes;
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 加密数据
         * 
         * @param data
         *            待加密数据
         * @param key
         *            密钥
         * @return 加密后的数据
         */
        public static String encrypt(String data, String password) {
            return encrypt(data, password, true);
        }

        /**
         * 加密数据 BASE 64
         * 
         * @param data
         *            待加密数据
         * @param key
         *            密钥
         * @return 加密后的数据
         */
        public static String encrypt(String data, String password, boolean urlSafe) {
            try {
                byte[] bytes = encrypt(data.getBytes(), password);
                if (urlSafe) {
                    // System.out.println(Hex.encodeHexString(bytes));
                    // System.out.println(Hex.encodeHexString(Base64.encodeBase64(bytes)));
                    return Base64.encodeBase64URLSafeString(bytes); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
                } else {
                    return Base64.encodeBase64String(bytes); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
                }
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 解密数据
         * 
         * @param data
         * @param password
         * @return
         */
        public static byte[] decrypt(byte[] data, String password) {
            try {
                Key k = getKey(password); // 还原密钥
                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, k); // 初始化Cipher对象，设置为解密模式
                byte[] bytes = cipher.doFinal(data);// 执行解密操作
                return bytes;
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 解密数据
         * 
         * @param data
         *            待解密数据
         * @param key
         *            密钥
         * @return 解密后的数据
         */
        public static String decrypt(String data, String password) {
            try {
                byte[] bytes = decrypt(Base64.decodeBase64(data), password);
                return new String(bytes); // 执行解密操作
            } catch (Exception e) {
                return null;
            }
        }

    }

    public static class HMAC_SHA1 {
        public static String encrypt(String data, String password) {
            try {
                SecretKeySpec signingKey = new SecretKeySpec(password.getBytes(), "HmacSHA1");
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(signingKey);
                byte[] rawHmac = mac.doFinal(data.getBytes());
                String dfd = Base64Utility.encodeBase64URLSafeString2(rawHmac);
                return dfd;
            } catch (Exception e) {
                return null;
            }
        }
    }

}
