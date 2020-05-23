package dbm.photo.scanner.service;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class MongoConfiguration {


    @Value("${photoscanner.mongodb.server:localhost}")
    private String mongoServer;

    @Value("${photoscanner.mongodb.port:27017}")
    private int mongoPort;

    @Value("${photoscanner.mongodb.database.name:photoscanner}")
    private String databaseName;

    @Bean
    public MongoDbFactory mongoDbFactory() {
        MongoClient mongoClient = new MongoClient(mongoServer + ":" + mongoPort);

        return new SimpleMongoDbFactory(mongoClient, databaseName );
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}