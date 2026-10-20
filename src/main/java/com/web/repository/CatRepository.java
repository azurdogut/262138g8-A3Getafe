package com.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.web.model.Cat;

 /* Concepto clave: el repositorio se declara, no se escribe. Spring Data genera
  en memoria la clase que implementa la interfaz al arrancar la aplicación.*/

// JpaRepository<Cat, Long> = entidad que gestiona (Cat) y tipo de su clave primaria (Long).
public interface CatRepository extends JpaRepository<Cat, Long>{
}
