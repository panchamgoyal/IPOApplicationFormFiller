package com.example.pdffiller.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PdfInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "pan_of_sole")
    private String panOfSole;
    @Column(name = "tel_number")
    private String telNumber;
    @Column(name = "filled")
    private Boolean filled;

}
