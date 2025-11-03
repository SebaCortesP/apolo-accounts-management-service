package com.duocuc.apolo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {
    
    private Long id;
    @NotBlank(message = "Full name is mandatory")
    private String name;
    @NotBlank(message = "lastname is mandatory")
    private String lastname;
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Role name is mandatory")
        @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private RoleDto      role;
    @Nullable()
    private String password;

}
