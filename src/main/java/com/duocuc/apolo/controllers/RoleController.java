package com.duocuc.apolo.controllers;

import com.duocuc.apolo.dto.ApiResponse;
import com.duocuc.apolo.models.Role;
import com.duocuc.apolo.repositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RoleController {

 @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Role>>> getAll() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Roles obtenidos correctamente", roles));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Role>> create(@RequestBody Role role) {
        if (roleRepository.existsByName(role.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "El rol '" + role.getName() + "' ya existe", null));
        }

        Role savedRole = roleRepository.save(role);
        return ResponseEntity.ok(new ApiResponse<>(true, "Rol creado correctamente", savedRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "El rol no existe", null));
        }

        roleRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Rol eliminado correctamente", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> getById(@PathVariable Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "El rol con ID " + id + " no existe", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Rol obtenido correctamente", role.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> update(@PathVariable Long id, @RequestBody Role updatedRole) {
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (roleOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "El rol con ID " + id + " no existe", null));
        }

        Role role = roleOpt.get();

        // Validar nombre duplicado en otro registro
        if (roleRepository.existsByName(updatedRole.getName()) && !role.getName().equalsIgnoreCase(updatedRole.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "El rol '" + updatedRole.getName() + "' ya existe", null));
        }

        role.setName(updatedRole.getName());
        Role saved = roleRepository.save(role);

        return ResponseEntity.ok(new ApiResponse<>(true, "Rol actualizado correctamente", saved));
    }
}
