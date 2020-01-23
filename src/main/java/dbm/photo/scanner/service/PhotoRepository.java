package dbm.photo.scanner.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

interface PhotoRepository extends MongoRepository<Photo, String> {
    Optional<Photo> findById(String checksum);
}
