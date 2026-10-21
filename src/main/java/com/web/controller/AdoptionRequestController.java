package com.web.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    public List<AdoptionRequest> listar() {
        // findAll() devuelve todas las filas de la tabla cat, sin ningún orden concreto.
        return adoptionRequestRepository.findAll(Sort.by("id"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionRequest crear(@RequestBody AdoptionRequest adoptionRequest) {
        // Anulamos el id recibido: save() inserta si el id es null y actualiza si existe,
        // y aquí queremos que siempre cree un gato nuevo.
        adoptionRequest.setId(null);
        return adoptionRequestRepository.save(adoptionRequest);
    }
}