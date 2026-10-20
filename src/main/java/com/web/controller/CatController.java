package com.web.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.web.model.Cat;
import com.web.repository.CatRepository;

// RequiredArgsConstructor procede de Lombok, que Maven ha descargado con el pom.xml.
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/a3getafe")
  
// @RequiredArgsConstructor escribe un constructor con un parámetro por cada campo final
// (aquí, CatRepository). Es el constructor que antes escribíamos a mano.
@RequiredArgsConstructor
  
public class CatController {
  private final CatRepository catRepository;

@GetMapping
  public List<Cat> listar() {
        // findAll() devuelve todas las filas de la tabla cat, sin ningún orden concreto.
        return catRepository.findAll();
    }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Cat crear(@RequestBody Cat cat) {
        // Anulamos el id recibido: save() inserta si el id es null y actualiza si existe,
        // y aquí queremos que siempre cree un gato nuevo.
        cat.setId(null);
        return catRepository.save(cat);
    }
}
