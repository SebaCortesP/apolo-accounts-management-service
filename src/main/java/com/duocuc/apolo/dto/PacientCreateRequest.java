package com.duocuc.apolo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacientCreateRequest {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
}