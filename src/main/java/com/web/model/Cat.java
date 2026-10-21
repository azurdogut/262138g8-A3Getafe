package com.web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString // Siempre que no haya relación con otra tabla porque se puede generar un bucle infinito con una fk

public class Cat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String name;
  private String ageDescription;
  private String description;
  private String imageUrl;
  private String status;

  public Cat(String name, String ageDescription, String description, String imageUrl, String status) {
    this.name = name;
    this.ageDescription = ageDescription;
    this.description = description;
    this.imageUrl = imageUrl;
    this.status = status;
  }
}
