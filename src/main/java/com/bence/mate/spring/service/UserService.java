package com.bence.mate.spring.service;

import com.bence.mate.spring.exception.AccountNotFoundException;
import com.bence.mate.spring.repository.UserRepository;
import com.bence.mate.spring.entity.UserEntity;
import com.bence.mate.spring.pojo.UserPojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository usersRepository;

    public UserEntity findByUsername(String username) throws AccountNotFoundException {
        return usersRepository.findByUsername(username).orElseThrow(AccountNotFoundException::new);
    }

    public UserEntity save(UserPojo usersPojo){
        UserEntity usersEntity = new UserEntity();
        usersEntity.setUserRole(usersPojo.getUserRole());
        usersEntity.setFirstName(usersPojo.getFirstName());
        usersEntity.setLastName(usersPojo.getLastName());
        usersEntity.setUsername(usersPojo.getUsername());

        return usersRepository.save(usersEntity);
    }

    public List<UserEntity> findAll(){
        return usersRepository.findAll();
    }
}
