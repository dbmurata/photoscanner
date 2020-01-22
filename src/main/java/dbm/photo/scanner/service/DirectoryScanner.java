package dbm.photo.scanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Optional;

public class DirectoryScanner implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DirectoryScanner.class);

    private File root;
    private ThreadPoolTaskExecutor directoryScannerExecutor;
    private PhotoRepository photos;

    DirectoryScanner(File root, ThreadPoolTaskExecutor directoryScannerExecutor, PhotoRepository photos) {
        this.root = root;
        this.directoryScannerExecutor = directoryScannerExecutor;
        this.photos = photos;
    }

    @Override
    public void run() {
        log.info("Scanning directory: {}", root.getAbsolutePath());
        File[] files = root.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    directoryScannerExecutor.submit(new DirectoryScanner(file, directoryScannerExecutor, photos));
                } else {
                    log.info("Checking file {}", file.getAbsolutePath());
                    try {
                        Photo photo = new Photo(file);
                        if (photos.existsById(photo.id)) {
                            log.info("{} exists.", file.getName());
                            Optional<Photo> tmp = photos.findById(photo.id);
                            log.info("Is present: {}", tmp.isPresent());
                            if (tmp.isPresent()) {
                                Photo p = tmp.get();
                                if (!p.files.contains(file.getAbsolutePath())) {
                                    p.files.add(file.getAbsolutePath());

                                    log.info("Updating {}", file.getAbsolutePath());
                                    photos.save(p);
                                }
                            }
                        } else {
                            log.info("Adding {}", file.getAbsolutePath());
                            photos.save(photo);
                        }
                    } catch (IOException | NoSuchAlgorithmException | ParseException e) {
                        log.error(e.getMessage());
                        e.printStackTrace(System.err);
                    }
                }
            }
        }
    }
}
