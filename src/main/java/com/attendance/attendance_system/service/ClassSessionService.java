package com.attendance.attendance_system.service;

import com.attendance.attendance_system.entity.ClassSession;
import com.attendance.attendance_system.repository.ClassSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class ClassSessionService {

    @Autowired
    private ClassSessionRepository classSessionRepository;

    public ClassSession createSession(ClassSession session) {
        session.setDate(LocalDate.now());
        session.setStartTime(LocalTime.now());
        session.setQrToken(generateQrToken());
        return classSessionRepository.save(session);
    }

    public ClassSession getSessionByQrToken(String qrToken) {
        return classSessionRepository.findByQrToken(qrToken)
                .orElseThrow(() -> new RuntimeException("Invalid QR token"));
    }

    public ClassSession getSessionById(Long id) {
        return classSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + id));
    }

    public List<ClassSession> getAllSessions() {
        return classSessionRepository.findAll();
    }

    private String generateQrToken() {
        return UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}