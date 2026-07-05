package com.attendance.attendance_system.security;

import com.attendance.attendance_system.entity.Admin;
import com.attendance.attendance_system.entity.Faculty;
import com.attendance.attendance_system.entity.Student;
import com.attendance.attendance_system.repository.AdminRepository;
import com.attendance.attendance_system.repository.FacultyRepository;
import com.attendance.attendance_system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return buildUser(student.get().getEmail(), student.get().getPassword(), student.get().getRole());
        }

        Optional<Faculty> faculty = facultyRepository.findByEmail(email);
        if (faculty.isPresent()) {
            return buildUser(faculty.get().getEmail(), faculty.get().getPassword(), faculty.get().getRole());
        }

        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            return buildUser(admin.get().getEmail(), admin.get().getPassword(), admin.get().getRole());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    private UserDetails buildUser(String email, String password, String role) {
        return new User(
                email,
                password,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}