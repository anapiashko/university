package org.example.repository;

import org.example.models.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends CrudRepository<Position, Integer> {

    Position findByFunctionIdAndRussianWordPosition(Integer functionId, String russianWordPosition);
}
