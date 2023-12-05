package com.pumppals.pumppalsapi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pumppals.pumppalsapi.model.Workout;


@Repository
public interface WorkoutRepository extends MongoRepository<Workout, ObjectId> {
    Optional<Workout> findByWorkoutId(String workoutId);
    List<Workout> findByUsername(String username);
    List<Workout> findByDate(LocalDate date);
    List<Workout> findByDateAndUsername(LocalDate date, String username);
}
