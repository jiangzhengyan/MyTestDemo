package com.sinoufc.jarinvoke;

import java.security.MessageDigest;

/**
 * 用于MD5签名
 */
public class UFCSecurityUtil {

    /**
     * @param s     待签名的字符串
     * @return      签名后的字符串
     */
    public static String getMessageMD5Digest(String s) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] buffer = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");

            //使用指定的字节更新摘要
            mdTemp.update(buffer);

            //获得密文
            byte[] md = mdTemp.digest();

            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}
