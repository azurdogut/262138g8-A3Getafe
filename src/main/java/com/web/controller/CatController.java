package com.web.controller;
import java.util.List;

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
  
public class CatController {
  private final CatRepository catRepository;

@GetMapping
  public List<Cat> getAll() {
        return catRepository.findAll(Sort.by("id"));
    }

  @GetMapping("/{id}")
    public ResponseEntity<Cat> getById(@PathVariable Long id) {
        return catRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Cat create(@Valid @RequestBody Cat cat) {
        // Anulamos el id recibido: save() inserta si el id es null y actualiza si existe,
        // y aquí queremos que siempre cree un gato nuevo.
        cat.setId(null);
        return catRepository.save(cat);
    }

  @PutMapping("/{id}")
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (catRepository.existsById(id)) {
            catRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Devuelve código 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}
