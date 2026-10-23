package com.web.controller;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.web.model.Cat;
import com.web.repository.CatRepository;

// Sort procede de Spring Data e indica por qué campos se ordena una consulta.
import org.springframework.data.domain.Sort;

// RequiredArgsConstructor procede de Lombok, que Maven ha descargado con el pom.xml.
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cat")
  
// @RequiredArgsConstructor escribe un constructor con un parámetro por cada campo final
// (aquí, CatRepository). Es el constructor que antes escribíamos a mano.
@RequiredArgsConstructor
  
@Tag(name = "Gatos", description = "Endpoints para la gestión del catálogo de gatos en proceso de adopción")
  public class CatController {
  private final CatRepository catRepository;

@GetMapping
@Operation(summary = "Catálogo de gatos", description = "Devuelve todos los gatos: primero los que están esperando adopción y luego los adoptados")
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de gatos obtenida correctamente (puede estar vacía)",
        content = @Content(
            mediaType = "application/json", 
            array = @ArraySchema(schema = @Schema(implementation = Cat.class))
        )
    )
})  
    public List<Cat> getAll() {
        return catRepository.findAll(Sort.by(Sort.Direction.ASC, "status"));
    }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener un gato por ID", description = "Busca y devuelve un gato específico a partir de su ID único")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Gato encontrado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cat.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Gato no encontrado con el ID proporcionado", 
            content = @Content
        )
    })
    public ResponseEntity<Cat> getById(@PathVariable Long id) {
        return catRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Crear un nuevo gato", description = "Registra un nuevo gato en el sistema")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Gato creado con éxito",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cat.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos inválidos (falló la validación @Valid)", 
            content = @Content
        )
    })
  public Cat create(@Valid @RequestBody Cat cat) {
        // Anulamos el id recibido: save() inserta si el id es null y actualiza si existe,
        // y aquí queremos que siempre cree un gato nuevo.
        cat.setId(null);
        return catRepository.save(cat);
    }

  @PutMapping("/{id}")
  @Operation(summary = "Actualizar un gato", description = "Modifica los datos de un gato existente buscando por su ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Gato actualizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cat.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Gato no encontrado para actualizar", 
            content = @Content
        )
    })
    public ResponseEntity<Cat> update(@PathVariable Long id, @RequestBody Cat catDetails) {
        return catRepository.findById(id)
                .map(cat -> {
                    cat.setName(catDetails.getName());
                    cat.setAgeDescription(catDetails.getAgeDescription());
                    cat.setDescription(catDetails.getDescription());
                    cat.setImageUrl(catDetails.getImageUrl());
                    cat.setStatus(catDetails.getStatus());
                    
                    Cat updatedCat = catRepository.save(cat);
                    return ResponseEntity.ok(updatedCat);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un gato", description = "Da de baja o elimina un gato del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Gato eliminado correctamente (sin contenido)", 
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Gato no encontrado para eliminar", 
            content = @Content
        )
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (catRepository.existsById(id)) {
            catRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Devuelve código 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}
