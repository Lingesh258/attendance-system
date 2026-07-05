package com.attendance.attendance_system.repository;

import com.attendance.attendance_system.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}