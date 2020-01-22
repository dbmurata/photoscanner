package dbm.photo.scanner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CKSUM {

    private static String cksum(File file, String algorithm) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int nLen;

        while ((nLen = fis.read(buffer)) > 0) {
            System.err.println("Length: " + nLen);
            digest.update(buffer, 0, nLen);
        }
        System.err.println("Out of loop");
        if (nLen > 0)
            digest.update(buffer, 0, nLen);
        System.err.println("Digesting...");
        byte[] sum = digest.digest();
        StringBuilder sumBuilder = new StringBuilder();
        for (byte b: sum) {
            if ((b & 0xff) < 16)
                sumBuilder.append("0");
            sumBuilder.append(Integer.toHexString(b & 0xff));
        }

        return sumBuilder.toString();
    }

    public static String md5sum(File file) throws NoSuchAlgorithmException, IOException {
        return cksum(file, "MD5");
    }

    public static String shasum(File file) throws IOException, NoSuchAlgorithmException {
        return cksum(file, "SHA-1");
    }

    public static String sha256(File file) throws IOException, NoSuchAlgorithmException {
        return cksum(file, "SHA-256");
    }
}
