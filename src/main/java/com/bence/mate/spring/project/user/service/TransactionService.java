package com.bence.mate.spring.project.user.service;

import com.bence.mate.spring.project.user.repository.UserTransactionRepository;
import com.bence.mate.spring.project.user.dto.TransactionResponseDto;
import com.bence.mate.spring.project.user.dto.TransactionRequestDto;
import com.bence.mate.spring.project.user.repository.UserRepository;
import com.bence.mate.spring.project.user.entity.UserTransaction;
import com.bence.mate.spring.project.user.dto.TransactionStatus;
import com.bence.mate.spring.project.user.util.EntityDtoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTransactionRepository transactionRepository;

    public Mono<TransactionResponseDto> createTransaction(TransactionRequestDto requestDto){
        return this.userRepository.updateUserBalance(requestDto.getUserId(), requestDto.getAmount())
                        .filter(Boolean::booleanValue)
                        .map(b -> EntityDtoUtil.toEntity(requestDto))
                        .flatMap(transactionRepository::save)
                        .map(ut -> EntityDtoUtil.toDto(requestDto, TransactionStatus.APPROVED))
                        .defaultIfEmpty(EntityDtoUtil.toDto(requestDto, TransactionStatus.DECLINED));
    }

    public Flux<UserTransaction> getByUserId(int userId){
        return this.transactionRepository.findByUserId(userId);
    }
}
