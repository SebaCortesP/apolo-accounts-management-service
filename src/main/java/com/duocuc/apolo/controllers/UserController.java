package com.duocuc.apolo.controllers;

import com.duocuc.apolo.dto.ApiResponse;
import com.duocuc.apolo.dto.ChangePasswordRequest;
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
    public ResponseEntity<ApiResponse<List<User>>> getAll() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuarios obtenidos correctamente", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new ApiResponse<>(false, "Usuario no encontrado", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario encontrado", user.get()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "El correo ya está registrado.", null));
        }

        if (user.getRole() == null || user.getRole().getId() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "Debe especificar un rol válido.", null));
        }

        var role = roleRepository.findById(user.getRole().getId())
                .orElse(null);

        if (role == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "Rol no encontrado.", null));
        }

        user.setRole(role);
        User saved = userRepository.save(user);
        return ResponseEntity
                .ok(new ApiResponse<>(true, "Usuario creado exitosamente", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> update(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existing = userRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new ApiResponse<>(false, "Usuario no encontrado", null));
        }

        User user = existing.get();
        user.setName(updatedUser.getName());
        user.setLastname(updatedUser.getLastname());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setRole(updatedUser.getRole());

        User saved = userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario actualizado correctamente", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity
                    .status(404)
                    .body(new ApiResponse<>(false, "Usuario no encontrado", null));
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuario eliminado correctamente", null));
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
