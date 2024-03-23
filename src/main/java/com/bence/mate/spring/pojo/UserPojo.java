package com.bence.mate.spring.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPojo {

    private String username;

    private String firstName;

    private String lastName;

    private String userRole;
}
