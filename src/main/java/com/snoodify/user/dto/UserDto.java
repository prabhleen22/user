package com.snoodify.user.dto;

import com.snoodify.user.model.Gender;
import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String email;
    private Integer age;
    private Gender gender;
}
