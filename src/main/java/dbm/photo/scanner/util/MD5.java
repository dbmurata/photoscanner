package dbm.photo.scanner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String md5sum(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int nLen;

        while ((nLen = fis.read(buffer)) > 0)
            digest.update(buffer, 0, nLen);
        if (nLen > 0)
            digest.update(buffer, 0, nLen);
        byte[] sum = digest.digest();
        StringBuilder sumBuilder = new StringBuilder();
        for (byte b: sum) {
            if ((b & 0xff) < 16)
                sumBuilder.append("0");
            sumBuilder.append(Integer.toHexString(b & 0xff));
        }

        return sumBuilder.toString();
    }
}
