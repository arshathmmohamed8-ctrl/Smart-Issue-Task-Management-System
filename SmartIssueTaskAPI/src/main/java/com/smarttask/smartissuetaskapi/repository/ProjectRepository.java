package com.smarttask.smartissuetaskapi.repository;

import com.smarttask.smartissuetaskapi.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findTop10ByOrderByUpdatedAtDesc();
}
