package com.web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString

public class AdoptionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;
    private String email;
    private String message;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String status = "PENDINGss";

    @ManyToOne
    private Cat cat;

    public AdoptionRequest(String applicantName, String email, String message, LocalDateTime createdAt, String status, Cat cat) {
        this.applicantName = applicantName;
        this.email = email;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
        this.cat = cat;
    }

}
