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
//@Builder
// Permite crear objetos de forma muy limpia y legible sin preocuparte por el orden de los parámetros ni por el id
public class Cat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String name;
  private String ageDescription;
  private String description;
  private String imageUrl;
  private String status;
  
}
