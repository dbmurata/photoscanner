package dbm.photo.scanner.service;

import dbm.photo.scanner.util.Exiftool;
import dbm.photo.scanner.util.CKSUM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

@Document(collection = "photo")
public class Photo {
    private static final Logger log = LoggerFactory.getLogger(Photo.class);

    @Id
    public String id;

    public List<String> files;
    public Map<String, Object> exif;

    Photo() {}

    public Photo(String id, List<String> files, Map<String, Object> exif) {
        this.id = id;
        this.files = files;
        this.exif = exif;
    }

    public Photo(File file) throws NoSuchAlgorithmException, IOException, ParseException {
        id = CKSUM.md5sum(file);
        files = new ArrayList<>();
        files.add(file.getAbsolutePath());
        exif = Exiftool.getMetadata(file);
    }

}
