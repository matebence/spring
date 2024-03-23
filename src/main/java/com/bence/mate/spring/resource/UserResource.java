package com.bence.mate.spring.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;

import com.bence.mate.spring.service.UserService;
import com.bence.mate.spring.pojo.UserPojo;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserResource {

    @Autowired
    private UserService usersService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getByUserName(@PathVariable String username) {
        return ResponseEntity.ok(usersService.findByUsername(username));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserPojo usersPojo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(usersPojo));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(usersService.findAll());
    }
}
