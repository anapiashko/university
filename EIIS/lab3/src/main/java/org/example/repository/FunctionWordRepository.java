package org.example.repository;

import org.example.models.FunctionWord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionWordRepository extends CrudRepository<FunctionWord, Integer> {
}
