package com.pumppals.pumppalsapi.model;

import java.time.LocalDate;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "workout")
public class Workout {
    @Id
    private ObjectId id;
    private List<Exercise> exercises;
    private LocalDate date;
    private String username;
    private String workoutId;
}
