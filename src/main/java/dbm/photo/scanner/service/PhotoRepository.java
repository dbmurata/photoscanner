package dbm.photo.scanner.service;

//import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.*;
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
        mongoClient = new MongoClient(new MongoClientURI("mongodb://" + hostname + ":" + port));
        database = mongoClient.getDB("photoscanner");
        collection = database.getCollection("photo");
    }

    public Photo findById(String checksum) {
        log.info("Looking for checksum {}", checksum);
        DBObject query = new BasicDBObject("_id", checksum);
        DBObject photo = collection.find(query).one();
        new Photo(photo);
        return null;
    }

    public boolean existsById(String checksum) {
        log.info("Looking for checksum {}", checksum);
        DBObject query = new BasicDBObject("_id", checksum);
        DBObject photo = collection.find(query).one();
        new Photo(photo);
        return false;
    }

    public void save(Photo photo) {

    }
}
