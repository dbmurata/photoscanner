package dbm.photo.scanner.service;

import com.mongodb.DBObject;
import dbm.photo.scanner.util.Exiftool;
import dbm.photo.scanner.util.CKSUM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.data.annotation.Id;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

class Photo {
    private static final Logger log = LoggerFactory.getLogger(Photo.class);

    //@Id
    String checksum;

    ArrayList<Object> files;
    Map<String, Object> exif;

    Photo(File file) throws NoSuchAlgorithmException, IOException, ParseException {
        checksum = CKSUM.md5sum(file);
        log.info("{} has checksum {}", file.getName(), checksum);
        files = new ArrayList<>();
        files.add(file.getAbsolutePath());
        exif = Exiftool.getMetadata(file);
        log.info("{} is done pulling data", file.getName());
    }

    protected Photo(DBObject obj) {
        checksum = (String)obj.get("_id");
        log.info("Found checksum {}", checksum);
        files = (ArrayList)obj.get("files");
        exif = (Map)obj.get("exif");

        log.info("Loaded photo {}", checksum);
    }

    public String toJSON() {
        StringBuilder json = new StringBuilder();

        json.append("{\"_id\":\"").append(checksum).append("\",\"files\":[");
        for (int i = 0; i < files.size(); i++) {
            json.append("\"").append(files.get(i).toString()).append("\"");
            if (i < files.size() - 1) json.append(",");
        }
        json.append("]");
        if (exif != null) {
            json.append(",\"exif\":{");
            Set<String> keyset = exif.keySet();
            Iterator<String> keys = keyset.iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                json.append("\"").append(key).append("\":\"").append(exif.get(key).toString()).append("\"");
                if (keys.hasNext()) json.append(",");
            }
            json.append("}");
        }
        json.append("}");

        return json.toString();
    }
}
