package dbm.photo.scanner.service;

import dbm.photo.scanner.util.Exiftool;
import dbm.photo.scanner.util.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Photo {
    private static final Logger log = LoggerFactory.getLogger(Photo.class);

    @Id
    String id;

    List<String> files;
    Map<String, Object> exif;

    Photo(File file) throws NoSuchAlgorithmException, IOException, ParseException {
        id = MD5.md5sum(file);
        files = new ArrayList<String>();
        files.add(file.getAbsolutePath());
        exif = Exiftool.getMetadata(file);
    }
}
