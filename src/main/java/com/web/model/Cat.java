package com.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString // Siempre que no haya relación con otra tabla porque se puede generar un bucle infinito con una fk

@Schema(description = "Gato en proceso de adopción")
public class Cat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Identificador de cada gato, generado por la base de datos", example = "4",
            accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @NotBlank(message = "El nombre es obligatorio")
  //@Size(max = 100, message = "El nombre admite como máximo 100 caracteres") --> Mirar en pgadmin VARCHAR (?)
  @Schema(description = "Nombre del gato", example = "Iris")
  private String name;
  @Schema(description = "Edad aproximada del gato", example = "2 años")
  private String ageDescription;
  //@Size(max = 150, message = "La descripción admite como máximo 150 caracteres") --> Mirar en pgadmin VARCHAR (?)
  @Schema(description = "Descripción de la personalidad del gato", example = "Cariñosa, juguetona y dormilona")
  private String description;
  @Schema(description = "Fotografía del gato", example = "iris.jpg")
  private String imageUrl;
  @NotBlank(message = "El estado de la adopción es obligatorio")
  @Schema(description = "Estado de la adopción", example = "AVAILABLE")
  private String status;

  public Cat(String name, String ageDescription, String description, String imageUrl, String status) {
    this.name = name;
    this.ageDescription = ageDescription;
    this.description = description;
    this.imageUrl = imageUrl;
    this.status = status;
  }
}
