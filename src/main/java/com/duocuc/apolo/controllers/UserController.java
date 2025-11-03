package com.duocuc.apolo.controllers;

import com.duocuc.apolo.dto.ApiResponse;
import com.duocuc.apolo.dto.ChangePasswordRequest;
import com.duocuc.apolo.dto.UserDto;
import com.duocuc.apolo.mappers.RoleMapper;
import com.duocuc.apolo.mappers.UserMapper;
import com.duocuc.apolo.models.User;
import com.duocuc.apolo.repositories.UserRepository;
import com.duocuc.apolo.utils.JwtTokenUtil;
import com.duocuc.apolo.repositories.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

     @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAll() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok( ApiResponse.success( "Usuarios obtenidos correctamente", users.stream().map(UserMapper::toDto).toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new ApiResponse<>(false, "Usuario no encontrado", null));
        }
        return ResponseEntity.ok( ApiResponse.success( "Usuario encontrado", UserMapper.toDto(user.get())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> create(@RequestBody UserDto user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body( ApiResponse.failure( "El correo ya está registrado."));
        }

        if (user.getRole() == null || user.getRole().getId() == null) {
            return ResponseEntity
                    .badRequest()
                    .body( ApiResponse.failure( "Debe especificar un rol válido."));
        }

        var role = roleRepository.findById(user.getRole().getId())
                .orElse(null);

        if (role == null) {
            return ResponseEntity
                    .badRequest()
                    .body( ApiResponse.failure( "Rol no encontrado."));
        }

        User newUser = UserMapper.toEntity(user);
        newUser.setRole(role);
        User saved = userRepository.save(newUser);
        return ResponseEntity
                .ok( ApiResponse.success( "Usuario creado exitosamente", UserMapper.toDto(saved)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> update(@PathVariable Long id, @RequestBody UserDto updatedUser) {
        Optional<User> existing = userRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(ApiResponse.failure( "Usuario no encontrado"));
        }

        User user = existing.get();
        user.setName(updatedUser.getName());
        user.setLastname(updatedUser.getLastname());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRole(RoleMapper.toEntity(updatedUser.getRole()));

        User saved = userRepository.save(user);
        return ResponseEntity.ok( ApiResponse.success("Usuario actualizado correctamente", UserMapper.toDto(saved)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity
                    .status(404)
                    .body( ApiResponse.failure("Usuario no encontrado"));
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario eliminado correctamente", null));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User credentials) {
        Optional<User> userOpt = userRepository.findByEmail(credentials.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Correo no registrado", null));
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(credentials.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Contraseña incorrecta", null));
        }
        //token JWT
        String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole().getName());
        return ResponseEntity.ok(new ApiResponse<>(true, "Inicio de sesión exitoso", token));
    }


    // CAMBIAR CONTRASEÑA
   @PostMapping("/change-password/{id}")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Usuario no encontrado", null));
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "La contraseña actual no coincide", null));
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Contraseña actualizada correctamente", null));
    }


}
