package com.pumppals.pumppalsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {
    private String name;
    private double weight;
    private double sets;
    private double reps;
}