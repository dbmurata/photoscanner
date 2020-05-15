package dbm.photo.scanner.service;

//import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
//import java.util.Optional;

class PhotoRepository { //extends MongoRepository<Photo, String> {
    private static final Logger log = LoggerFactory.getLogger(PhotoRepository.class);

    private MongoClient mongoClient;
    private DB database;
    private DBCollection collection;

    public PhotoRepository(String hostname, int port) throws UnknownHostException {
        log.info("Connecting to " + hostname + ":" + port);
        mongoClient = new MongoClient(new MongoClientURI("mongodb://" + hostname + ":" + port));
        database = mongoClient.getDB("photoscanner");
        collection = database.getCollection("photo");
    }

    public Photo findById(String checksum) {
        DBObject photo = collection.findOne(new BasicDBObject("_id", checksum));
        return photo == null ? null : new Photo(photo);
    }

    public Photo find(Photo photo) {
        return findById(photo.get("checksum").toString());
    }

    public boolean existsById(String checksum) {
        return collection.findOne(new BasicDBObject("_id", checksum)) != null;
    }

    public boolean exists(Photo photo) {
        return existsById(photo.get("checksum").toString());
    }

    public void save(Photo photo) {
        log.info("Saving {}", ((BasicDBList)(photo.get("files"))).get(0));
        DBObject query = new BasicDBObject("_id", photo.get("checksum"));
        log.info("Write photo: {}", photo.toString());
        //DBObject p = (DBObject)JSON.parse(photo.toJSON());
        collection.update(query, photo, true, false);
        log.info("Saved");
    }
}
