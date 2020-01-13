package dbm.photo.scanner.service;

import dbm.photo.scanner.util.Exiftool;
import dbm.photo.scanner.util.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

public class DirectoryScanner implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DirectoryScanner.class);

    private File root;
    private ThreadPoolTaskExecutor directoryScannerExecutor;

    public DirectoryScanner(File root, ThreadPoolTaskExecutor directoryScannerExecutor) {
        this.root = root;
        this.directoryScannerExecutor = directoryScannerExecutor;
    }

    @Override
    public void run() {
        log.info("Scanning directory: {}", root.getAbsolutePath());
        File[] files = root.listFiles();

        for (File file: files) {
            if (file.isDirectory()) {
                directoryScannerExecutor.submit(new DirectoryScanner(file, directoryScannerExecutor));
            }
            else {
                try {
                    log.info("Found file: {}", file.getName());
                    String md5 = MD5.md5sum(file);
                    log.info("MD5: {}", md5);
                    Map<String, String> exif = Exiftool.getMetadata(file);
                    Set<String> keys = exif.keySet();
                    for (String key: keys) {
                        log.info("{} = {}", key, exif.get(key));
                    } // */
                }
                catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
