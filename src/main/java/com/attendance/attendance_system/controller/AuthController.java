package com.attendance.attendance_system.controller;

import com.attendance.attendance_system.dto.request.LoginRequest;
import com.attendance.attendance_system.dto.response.LoginResponse;
import com.attendance.attendance_system.entity.Admin;
import com.attendance.attendance_system.entity.Faculty;
import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.repository.AdminRepository;
import com.attendance.attendance_system.repository.FacultyRepository;
import com.attendance.attendance_system.repository.StudentRepository;
import com.attendance.attendance_system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        var authResult = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.replace("ROLE_", ""))
                .orElse("UNKNOWN");

        String token = jwtUtil.generateToken(request.getEmail(), role);

        return new LoginResponse(token, request.getEmail(), role);
    }

    @PostMapping("/register/student")
    public Student registerStudent(@RequestBody Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    @PostMapping("/register/faculty")
    public Faculty registerFaculty(@RequestBody Faculty faculty) {
        faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
        return facultyRepository.save(faculty);
    }

    @PostMapping("/register/admin")
    public Admin registerAdmin(@RequestBody Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
}