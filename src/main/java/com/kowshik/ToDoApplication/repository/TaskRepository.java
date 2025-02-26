package com.kowshik.ToDoApplication.repository;

import com.kowshik.ToDoApplication.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> searchTasks(@Param("keyword") String keyword);

    @Query("SELECT t FROM Task t WHERE t.priority = :priority")
    List<Task> filterByPriority(@Param("priority") Task.PriorityLevel priority);

    @Query("SELECT t FROM Task t WHERE t.status = :status")
    List<Task> filterByCompletionStatus(@Param("status") Task.CompletionStatus status);

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :currentDate AND :futureDate")
    List<Task> filterByDueDate(@Param("currentDate") LocalDateTime currentDate, @Param("futureDate") LocalDateTime futureDate);
}