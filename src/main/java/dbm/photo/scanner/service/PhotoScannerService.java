package dbm.photo.scanner.service;

import dbm.photo.scanner.util.Exiftool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class PhotoScannerService {

    private static final Logger log = LoggerFactory.getLogger(PhotoScannerService.class);

    @Value("#{'${photoscanner.scanner.rootdirs:C:\\}'.split(',')}")
    private List<String> roots;

    @Value("${photoscanner.exiftool.path:exiftool}")
    private String exiftoolPath;

    @Value("${photoscanner.mongodb.server:localhost}")
    private String mongoServer;

    @Value("${photoscanner.mongodb.port:27017")
    private int mongoPort;

    @Autowired
    private ThreadPoolTaskExecutor directoryScannerExecutor;

    //@Autowired
    private PhotoRepository photos;

    @PostConstruct
    public void init() {
        Exiftool.setExiftoolPath(exiftoolPath);
        try {
            photos = new PhotoRepository(mongoServer, mongoPort);
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(fixedDelayString = "${photoscanner.scheduler.millis:9000000}")
    public void scanLocations() {
        for (String root: roots) {
            directoryScannerExecutor.submit(new DirectoryScanner(new File(root), directoryScannerExecutor, photos));
        }
    }
}
