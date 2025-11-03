package com.duocuc.apolo.mappers;

import com.duocuc.apolo.dto.UserDto;
import com.duocuc.apolo.models.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(RoleMapper.toDto(user.getRole()))
                .build();
    }
    public static User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
       user.setRole(RoleMapper.toEntity(userDto.getRole()));
        return user;
    }
}
