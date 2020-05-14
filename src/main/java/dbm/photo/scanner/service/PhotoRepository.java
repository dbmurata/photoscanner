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
        log.info("Looking for checksum {}", checksum);
        DBObject query = new BasicDBObject("_id", checksum);
        DBObject photo = collection.findOne(query);
        if (photo != null)
            return new Photo(photo);
        return null;
    }

    public boolean existsById(String checksum) {
        log.info("Looking for checksum {}", checksum);
        DBObject query = new BasicDBObject("_id", checksum);
        log.info("Query: {}", query.toString());
        DBObject photo = collection.findOne(query);
        /*if (photo != null)
            log.info("Found: {}", photo.toString());
        else
            log.info("Found: null");
        new Photo(photo); // */
        return photo != null;
    }

    public void save(Photo photo) {
        DBObject query = new BasicDBObject("_id", photo.checksum);
        DBObject p = (DBObject)JSON.parse(photo.toJSON());
        collection.update(query, p, true, false);
    }
}
