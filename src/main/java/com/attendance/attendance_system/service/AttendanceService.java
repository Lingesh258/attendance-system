package com.attendance.attendance_system.service;

import com.attendance.attendance_system.entity.Attendance;
import com.attendance.attendance_system.entity.ClassSession;
import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ClassSessionService classSessionService;

    @Autowired
    private StudentService studentService;

    private static final int QR_VALID_SECONDS = 60;

    public Attendance markAttendance(String qrToken, Long studentId) {

        // 1. Find the session linked to this QR token
        ClassSession session = classSessionService.getSessionByQrToken(qrToken);

        // 2. Check if the QR token has expired (valid for 60 seconds)
        LocalTime now = LocalTime.now();
        long secondsElapsed = ChronoUnit.SECONDS.between(session.getStartTime(), now);
        if (secondsElapsed > QR_VALID_SECONDS) {
            throw new RuntimeException("QR code expired. Ask faculty to generate a new one.");
        }

        // 3. Get the student
        Student student = studentService.getStudentById(studentId);

        // 4. Check if this student already marked attendance for this session
        boolean alreadyMarked = attendanceRepository.existsByStudentAndClassSession(student, session);
        if (alreadyMarked) {
            throw new RuntimeException("Attendance already marked for this session.");
        }

        // 5. All checks passed — save attendance
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setClassSession(session);
        attendance.setDate(LocalDate.now());
        attendance.setTime(now);
        attendance.setStatus("PRESENT");

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByStudent(Long studentId) {
        Student student = studentService.getStudentById(studentId);
        return attendanceRepository.findByStudent(student);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
}