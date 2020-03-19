package dbm.photo.scanner.service;

//import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.*;

import java.net.UnknownHostException;
//import java.util.Optional;

class PhotoRepository { //extends MongoRepository<Photo, String> {

    private MongoClient mongoClient;
    private DB database;
    private DBCollection collection;

    public PhotoRepository(String hostname, int port) throws UnknownHostException {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://" + hostname + ":" + port));
        database = mongoClient.getDB("test");
        collection = database.getCollection("photo");
    }

    public Photo findById(String checksum) {
        DBObject query = new BasicDBObject("_id", checksum);
        DBObject photo = collection.find(query).one();
        return null;
    }

    public boolean existsById(String checksum) {
        return false;
    }

    public void save(Photo photo) {

    }
}
