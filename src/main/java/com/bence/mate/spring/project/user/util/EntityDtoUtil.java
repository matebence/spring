package com.bence.mate.spring.project.user.util;

import com.bence.mate.spring.project.user.dto.TransactionResponseDto;
import com.bence.mate.spring.project.user.dto.TransactionRequestDto;
import com.bence.mate.spring.project.user.entity.UserTransaction;
import com.bence.mate.spring.project.user.dto.TransactionStatus;
import com.bence.mate.spring.project.user.dto.UserDto;
import com.bence.mate.spring.project.user.entity.User;

import org.springframework.beans.BeanUtils;
import java.time.LocalDateTime;

public class EntityDtoUtil {

    public static UserDto toDto(User user){
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
       
        return dto;
    }

    public static User toEntity(UserDto dto){
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        
        return user;
    }

    public static UserTransaction toEntity(TransactionRequestDto requestDto){
        UserTransaction ut = new UserTransaction();
        ut.setUserId(requestDto.getUserId());
        ut.setAmount(requestDto.getAmount());
        ut.setTransactionDate(LocalDateTime.now());
        
        return ut;
    }

    public static TransactionResponseDto toDto(TransactionRequestDto requestDto, TransactionStatus status){
        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setAmount(requestDto.getAmount());
        responseDto.setUserId(requestDto.getUserId());
        responseDto.setStatus(status);
        
        return responseDto;
    }
}
