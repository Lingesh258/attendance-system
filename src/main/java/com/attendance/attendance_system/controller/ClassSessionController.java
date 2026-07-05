package com.attendance.attendance_system.controller;

import com.attendance.attendance_system.entity.ClassSession;
import com.attendance.attendance_system.service.ClassSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class ClassSessionController {

    @Autowired
    private ClassSessionService classSessionService;

    @PostMapping("/create")
    public ClassSession createSession(@RequestBody ClassSession session) {
        return classSessionService.createSession(session);
    }

    @GetMapping("/{id}")
    public ClassSession getSessionById(@PathVariable Long id) {
        return classSessionService.getSessionById(id);
    }

    @GetMapping
    public List<ClassSession> getAllSessions() {
        return classSessionService.getAllSessions();
    }
}