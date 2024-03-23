package com.onedayoffer.taskdistribution.repositories;

import com.onedayoffer.taskdistribution.repositories.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

//    @Query(value = "select * from task where employee_id = :employeeId", nativeQuery = true)
    List<Task> findAllByEmployeeId(Integer employeeId);
}
