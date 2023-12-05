package com.pumppals.pumppalsapi.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pumppals.pumppalsapi.model.Workout;
import com.pumppals.pumppalsapi.service.UserService;
import com.pumppals.pumppalsapi.service.WorkoutService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/workout")
public class WorkoutController {
    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private UserService userService;

    // get all workouts
    // sort by most recent date
    @GetMapping("/all")
    public List<Workout> getAllWorkouts() {
        System.out.println("Getting all workouts");
        return workoutService.getAllWorkouts();
    }

    // get user workouts
    // sort by most recent date
    @GetMapping("/user")
    public List<Workout> getUserWorkouts(Principal principal) {
        String username = principal.getName();
        System.out.println("Getting workouts for username: " + username);
        return workoutService.getWorkoutsByUsername(username);
    }

    // get all workouts for a user
    // sort by most recent date
    @GetMapping("/{username}")
    public List<Workout> getWorkoutsByUsername(@PathVariable String username) {
        System.out.println("Getting workouts for username: " + username);
        return workoutService.getWorkoutsByUsername(username);
    }

    // get a workout by workoutId
    @GetMapping("/id/{workoutId}")
    public Workout getWorkoutByWorkoutId(@PathVariable String workoutId) {
        System.out.println("Getting workout by workoutId: " + workoutId);
        return workoutService.getWorkoutByWorkoutId(workoutId);
    }

    // get a workout by username and date
    @GetMapping("/{username}/{date}")
    public List<Workout> getWorkoutByUsernameAndDate(@PathVariable String username, @PathVariable String date) {
        System.out.println("Getting workouts for username: " + username + " and date: " + date);
        return workoutService.getWorkoutsByDateAndUsername(date, username);
    }

    // get all workouts for a date
    @GetMapping("/date/{date}")
    public List<Workout> getWorkoutsByDate(@PathVariable String date) {
        System.out.println("Getting workouts for date: " + date);
        return workoutService.getWorkoutsByDate(date);
    }

    // create a workout
    @PostMapping("/create")
    public ResponseEntity<String> createWorkout(Principal principal, @RequestBody Workout workout) {
        System.out.println("Creating workout with exercises: " + workout.getExercises());
        String username = principal.getName();
        workout.setUsername(username);
        // set workout date to current date
        LocalDate date = LocalDate.now();
        workout.setDate(date);
        System.out.println("Creating workout for username: " + username);
        Workout savedWorkout = workoutService.createWorkout(workout);
        System.out.println(savedWorkout);
        return ResponseEntity.ok("Workout created successfully");
    }

    // get a list of workouts from accounts the user follows
    @GetMapping("/following")
    public List<Workout> getFollowingWorkouts(Principal principal) {
        String username = principal.getName();
        System.out.println("Getting workouts for username: " + username);
        return workoutService.getFollowingWorkouts(username);
    }
}
