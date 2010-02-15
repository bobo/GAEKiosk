/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.lan.kiosk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author mikael.sundberg
 */
public class Hasher {

    public String hash(String password) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.update(password.getBytes());

        byte[] messageDigest = instance.digest();
        String s = convertByteArrayToString(messageDigest);
        return s;
    }

    private String convertByteArrayToString(byte[] messageDigest) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String toHexString = Integer.toHexString(0xFF & messageDigest[i]);
            if (toHexString.length() == 1) {
                toHexString = 0 + toHexString;
            }
            hexString.append(toHexString);
        }
        return hexString.toString();
    }
}
