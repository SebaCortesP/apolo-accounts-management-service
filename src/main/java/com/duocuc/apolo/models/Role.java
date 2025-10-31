package com.duocuc.apolo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_seq")
    @SequenceGenerator(name = "rol_seq", sequenceName = "rol_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El tipo de rol es obligatorio")
    private String name;



}
