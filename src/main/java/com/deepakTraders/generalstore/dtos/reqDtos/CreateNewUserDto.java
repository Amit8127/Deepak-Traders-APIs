package com.deepakTraders.generalstore.dtos.reqDtos;

import lombok.Data;

@Data
public class CreateNewUserDto {

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String roles;
    private String mobile;
}
