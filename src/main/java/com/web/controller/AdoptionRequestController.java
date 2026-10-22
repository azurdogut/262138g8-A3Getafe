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

public class AdoptionRequestController {
    private final AdoptionRequestRepository adoptionRequestRepository;

    @GetMapping
    public List<AdoptionRequest> getAll() {
        return adoptionRequestRepository.findAll(Sort.by("id"));
    }
    
  @GetMapping("/{id}")
    public ResponseEntity<AdoptionRequest> getById(@PathVariable Long id) {
        return adoptionRequestRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionRequest create(@RequestBody AdoptionRequest adoptionRequest) {
        // Anulamos el id recibido: save() inserta si el id es null y actualiza si existe,
        // y aquí queremos que siempre cree un gato nuevo.
        adoptionRequest.setId(null);
        return adoptionRequestRepository.save(adoptionRequest);
    }
    
  @PutMapping("/{id}")
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (adoptionRequestRepository.existsById(id)) {
            adoptionRequestRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Devuelve código 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}
