package com.attendance.attendance_system.repository;

import com.attendance.attendance_system.entity.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
    Optional<ClassSession> findByQrToken(String qrToken);
}