package com.pumppals.pumppalsapi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pumppals.pumppalsapi.model.UserInfo;
import com.pumppals.pumppalsapi.model.Workout;
import com.pumppals.pumppalsapi.repository.WorkoutRepository;

@Service
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserService userService;

    public List<Workout> getAllWorkouts() {
        // sort by most recent date
        List<Workout> workouts = workoutRepository.findAll();
        workouts.sort((w1, w2) -> w1.getDate().compareTo(w2.getDate()));    
        return workouts;
    }

    public List<Workout> getWorkoutsByUsername(String username) {
        List<Workout> workouts = workoutRepository.findByUsername(username);
        workouts.sort((w1, w2) -> w2.getDate().compareTo(w1.getDate()));    
        return workouts;
    }

    public Workout getWorkoutByWorkoutId(String workoutId) {
        return workoutRepository.findByWorkoutId(workoutId).orElse(null);
    }

    public Workout createWorkout(Workout workout) {
        workout = workoutRepository.save(workout);
        workout.setWorkoutId(workout.getId().toString());
        return workoutRepository.save(workout);
    }

    public void deleteWorkout(String workoutId) {
        Optional<Workout> workout = workoutRepository.findByWorkoutId(workoutId);
        if (workout.isPresent()) {
            workoutRepository.delete(workout.get());
        }
    }

    public List<Workout> getWorkoutsByDate(String date) {
        // convert date string to LocalDate
        LocalDate localDate = LocalDate.parse(date);
        return workoutRepository.findByDate(localDate);
    }

    public List<Workout> getWorkoutsByDateAndUsername(String date, String username) {
        // convert date string to LocalDate
        LocalDate localDate = LocalDate.parse(date);
        return workoutRepository.findByDateAndUsername(localDate, username);
    }

    public List<Workout> getFollowingWorkouts (String username) {
        Optional<UserInfo> user = userService.getUserByUsername(username);
        if (!user.isPresent()) {
            return null;
        }
        List<String> following = user.get().getFollowing();
        List<Workout> workouts = new ArrayList<>();
        if (following.size() > 0) {
            workouts = workoutRepository.findByUsername(following.get(0));
            for (int i = 1; i < following.size(); i++) {
                workouts.addAll(workoutRepository.findByUsername(following.get(i)));
            }
        }
        // add user's workouts
        workouts.addAll(workoutRepository.findByUsername(username));
        workouts.sort((w1, w2) -> w2.getDate().compareTo(w1.getDate()));
        return workouts;
    }
}
