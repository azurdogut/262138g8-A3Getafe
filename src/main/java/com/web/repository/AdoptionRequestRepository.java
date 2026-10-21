package com.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.web.model.AdoptionRequest;

 /* Concepto clave: el repositorio se declara, no se escribe. Spring Data genera
  en memoria la clase que implementa la interfaz al arrancar la aplicación.*/

// JpaRepository<AdoptionRequest, Long> = entidad que gestiona (AdoptionRequest) y tipo de su clave primaria (Long).
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long>{
}
