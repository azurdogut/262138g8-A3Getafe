package com.web.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.model.AdoptionRequest;
import com.web.repository.AdoptionRequestRepository;

// Sort procede de Spring Data e indica por qué campos se ordena una consulta.
import org.springframework.data.domain.Sort;

// RequiredArgsConstructor procede de Lombok, que Maven ha descargado con el pom.xml.
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/adoptionRequest")

// @RequiredArgsConstructor escribe un constructor con un parámetro por cada campo final
// (aquí, AdoptionRequestRepository). Es el constructor que antes escribíamos a mano.
@RequiredArgsConstructor

@Tag(name = "Solicitudes de adopción", description = "Endpoints para la gestión de las solicitudes de adopción de gatos")
public class AdoptionRequestController {
    private final AdoptionRequestRepository adoptionRequestRepository;

    @GetMapping
    @Operation(summary = "Listado de solicitudes de adopción", description = "Devuelve todas las solicitudes: ordenadas por fecha de solicitud")
    @ApiResponses(value = {
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de solicitudes obtenida correctamente (puede estar vacía)",
        content = @Content(
            mediaType = "application/json", 
            array = @ArraySchema(schema = @Schema(implementation = AdoptionRequest.class))
        )
    )
})  
    public List<AdoptionRequest> getAll() {
        return adoptionRequestRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
    }
    
@GetMapping("/{id}")
@Operation(summary = "Obtener solicitud de adopción por ID", description = "Busca y devuelve una solicitud de adopción específica a partir de su ID único")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Solicitud encontrada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdoptionRequest.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Solicitud no encontrada con el ID proporcionado", 
            content = @Content
        )
    })
    public ResponseEntity<AdoptionRequest> getById(@PathVariable Long id) {
        return adoptionRequestRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear nueva solicitud de adopción", description = "Registra una nueva solicitud de adopción en el sistema")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Solicitud creada con éxito",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdoptionRequest.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos inválidos en la solicitud", 
            content = @Content
        )
    })
    public AdoptionRequest create(@Valid @RequestBody AdoptionRequest adoptionRequest) {
        // Anulamos el id recibido: save() inserta si el id es null y actualiza si existe,
        // y aquí queremos que siempre cree un gato nuevo.
        adoptionRequest.setId(null);
        return adoptionRequestRepository.save(adoptionRequest);
    }
    
@PutMapping("/{id}")
@Operation(summary = "Actualizar solicitud de adopción", description = "Modifica los datos de una solicitud de adopción existente buscando por su ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Solicitud actualizada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdoptionRequest.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Solicitud no encontrada para actualizar", 
            content = @Content
        )
    })
    public ResponseEntity<AdoptionRequest> update(@PathVariable Long id, @RequestBody AdoptionRequest adoptionRequestDetails) {
        return adoptionRequestRepository.findById(id)
                .map(adoptionRequest -> {
                    adoptionRequest.setApplicantName(adoptionRequestDetails.getApplicantName());
                    adoptionRequest.setEmail(adoptionRequestDetails.getEmail());
                    adoptionRequest.setMessage(adoptionRequestDetails.getMessage());
                    adoptionRequest.setCreatedAt(adoptionRequestDetails.getCreatedAt());
                    adoptionRequest.setStatus(adoptionRequestDetails.getStatus());
                    
                    AdoptionRequest updatedAdoptionRequest = adoptionRequestRepository.save(adoptionRequest);
                    return ResponseEntity.ok(updatedAdoptionRequest);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar solicitud de adopción", description = "Elimina una solicitud de adopción del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Solicitud eliminada correctamente (sin contenido)", 
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Solicitud no encontrada para eliminar", 
            content = @Content
        )
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (adoptionRequestRepository.existsById(id)) {
            adoptionRequestRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Devuelve código 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}
