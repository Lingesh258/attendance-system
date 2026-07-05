package com.attendance.attendance_system.repository;

import com.attendance.attendance_system.entity.Attendance;
import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.entity.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(Student student);
    boolean existsByStudentAndClassSession(Student student, ClassSession classSession);
}