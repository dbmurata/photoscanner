package dbm.photo.scanner.service;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfiguration {


    //@Value("${photoscanner.mongodb.server:localhost}")
    //private String mongoServer;

    //@Value("${photoscanner.mongodb.port:27017")
    //private int mongoPort;
    //public @Bean
    //MongoClient mongoClient() {
    //    return new MongoClient(mongoServer);
    //}
}