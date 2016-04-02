package pl.edu.agh.entities;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Mere on 2016-04-03.
 */
public interface IGuardianRepository extends MongoRepository<GuardianArticle, String> {
}
