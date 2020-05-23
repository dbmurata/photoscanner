package dbm.photo.scanner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Optional;

public class FileProcessor implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FileProcessor.class);

    private File file;
    private ThreadPoolTaskExecutor fileProcessorExecutor;
    private PhotoRepository photos;
    private String prefix;

    public FileProcessor(File file, ThreadPoolTaskExecutor fileProcessorExecutor, PhotoRepository photos, String prefix) {
        this.file = file;
        this.fileProcessorExecutor = fileProcessorExecutor;
        this.photos = photos;

        this.prefix = prefix;
    }

    @Override
    public void run() {
        log.info("Processing file {}", file.getAbsolutePath());
        try {
            Photo photo = new Photo(file);
            Optional<Photo> op = photos.findById(photo.id);
            if (op.isPresent()) {
                Photo p = op.get();
                if (!(p.files.contains(file.getAbsolutePath()))) {
                    p.files.add(file.getAbsolutePath());

                    log.info("Updating {}", file.getAbsolutePath());
                    photos.save(p);
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
