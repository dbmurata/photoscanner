package dbm.photo.scanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;

public class DirectoryScanner implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DirectoryScanner.class);

    private File root;
    private ThreadPoolTaskExecutor directoryScannerExecutor;
    private ThreadPoolTaskExecutor fileProcessorExecutor;
    private PhotoRepository photos;
    private String prefix;

    public DirectoryScanner(File root, ThreadPoolTaskExecutor directoryScannerExecutor, ThreadPoolTaskExecutor fileProcessorExecutor, PhotoRepository photos, String prefix) {
        this.root = root;
        this.directoryScannerExecutor = directoryScannerExecutor;
        this.fileProcessorExecutor = fileProcessorExecutor;
        this.photos = photos;

        this.prefix = prefix;
    }

    @Override
    public void run() {
        log.info("Scanning directory: {}", root.getAbsolutePath());
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    directoryScannerExecutor.submit(new DirectoryScanner(file, directoryScannerExecutor, fileProcessorExecutor, photos, prefix + " "));
                } else {
                    fileProcessorExecutor.submit(new FileProcessor(file, fileProcessorExecutor, photos, prefix + " "));
                }
            }
        }
    }

}
