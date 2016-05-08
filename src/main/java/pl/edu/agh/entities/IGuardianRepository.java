package pl.edu.agh.entities;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mere on 2016-04-03.
 */
public interface IGuardianRepository extends CrudRepository<GuardianArticle, Long> {
    Long countBySearch(Search search);
}
