package org.example.repository;

import org.example.models.Function;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionRepository extends CrudRepository<Function, Integer> {

    List<Function> findAll();
}
