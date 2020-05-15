package dbm.photo.scanner.service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
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

class Photo extends BasicDBObject {
    private static final Logger log = LoggerFactory.getLogger(Photo.class);

    Photo(File file) throws NoSuchAlgorithmException, IOException, ParseException {
        put("_id", CKSUM.md5sum(file));
        BasicDBList files = new BasicDBList();
        files.add(file.getAbsolutePath());
        put("files", files);
        Map<String, Object> exif = Exiftool.getMetadata(file);
        if (exif != null) {
            put("exif", new BasicDBObject(exif));
        }
    }

    protected Photo(DBObject obj) {
        super(obj.toMap());
        /*checksum = (String)obj.get("_id");
        files = (ArrayList)obj.get("files");
        exif = (Map)obj.get("exif"); // */
    }

    public String getChecksum() {
        return get("_id").toString();
    }

    public boolean hasFilePath(String path) {
        return ((BasicDBList)(get("files"))).contains(path);
    }

    public void addFilePath(String path) {
        ((BasicDBList)(get("files"))).add(path);
    }

    /*public String toJSON() {
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
    } // */
}
