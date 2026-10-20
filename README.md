🐾 A3Getafe

## Aplicación web para una protectora de gatos

> **Objetivo del proyecto**
> 
> Crear una pequeña aplicación web para dar visibilidad a los gatos que buscan hogar, explicar la labor de A3G y facilitar que las personasinteresadas contacten con la asociación para iniciar una adopción.

* * *

## 📌 Índice

1. [Problema y usuarios](#-1-problema-y-usuarios)
2. [Funcionalidades mínimas (MVP)](#-2-funcionalidades-mínimas-mvp)
3. [Modelo de datos](#-3-modelo-de-datos)
4. [Thymeleaf y API REST](#-4-thymeleaf-y-api-rest)
5. [Dificultad, riesgos yampliaciones](#-5-dificultad-riesgos-y-ampliaciones)
6. [Competencias que se puedenevaluar](#-6-competencias-que-se-pueden-evaluar)

* * *

## 🧩 1. Problema y usuarios

### El problema

Una protectora pequeña puede necesitar una forma sencilla de mostrar los gatos disponibles para adopción sin depender exclusivamente de laspublicaciones en redes sociales ni de responder repetidamente a las mismas preguntas.

La web cubrirá tres necesidades:

* * *

Necesidad Cómo ayuda la web

* * *

**Visibilidad** Presenta los gatos en un catálogo organizado, con información e imágenes.

**Contacto** Permite que una persona interesada pregunte por un gato concreto.

**Colaboración** Explica qué hace A3G e informa sobre cómo ayudar mediante donaciones o voluntariado.

* * *

### Usuarios principales

* **Persona interesada en adoptar:** consulta los gatos, descubre sus características y envía una solicitud de contacto.
* **Persona que quiere colaborar:** conoce la labor de A3G y encuentra información para donar, hacerse voluntaria o contactar con laasociación.
* **Responsable de la protectora:** necesita mantener organizados los datos de los gatos y las solicitudes de contacto.

* * *

## 🛠️ 2. Funcionalidades mínimas (MVP)

El **MVP** (*Producto Mínimo Viable*) incluye las funcionalidades esenciales para que la aplicación sea útil y permita demostrar lastecnologías del proyecto.

### 1. Página de inicio

Breve presentación de A3G, explicación de su labor, selección de gatos y enlaces para adoptar o colaborar.

### 2. Catálogo de gatos

Listado obtenido de PostgreSQL. Cada gato mostrará:

* Fotografía.
* Nombre.
* Edad aproximada.
* Descripción breve.
* Estado de adopción.

### 3. Ficha individual de cada gato

Página con fotografía, descripción ampliada, edad y enlace para iniciar una solicitud de adopción.

### 4. Formulario de interés en adoptar

Incluirá nombre, correo electrónico y mensaje. El formulario estará asociado al gato elegido y guardará la solicitud en PostgreSQL. Tambiénmostrará errores de validación y una confirmación cuando el envío se complete correctamente.

### 5. Página «Colabora»

Información sobre la labor de la protectora y formas de ayudar, con enlaces externos de contacto o donación proporcionados por laasociación.

* * *

## 🗃️ 3. Modelo de datos

La base de datos tendrá dos entidades principales relacionadas entre sí.

### Entidad `Cat` --- gato

Campo Descripción

* * *

`id` Clave primaria. `name` Nombre del gato. `ageDescription` Edad aproximada. `description` Descripción del gato. `imageUrl` URL de la fotografía. `status` Estado: disponible, reservado o adoptado.

### Entidad `AdoptionRequest` --- solicitud de adopción

Campo Descripción

* * *

`id` Clave primaria. `applicantName` Nombre de la persona solicitante. `email` Correo electrónico. `message` Mensaje de la persona interesada. `createdAt` Fecha de creación. `status` Estado de la solicitud. `catId` Clave foránea que referencia a `Cat`.

### Relación entre las entidades

**Un gato puede tener varias solicitudes de adopción (relación 1:N).**Cada solicitud está vinculada al gato por el campo `catId`.

    erDiagram
        CAT ||--o{ ADOPTION_REQUEST : "recibe"
        CAT {
            Long id PK
            String name
            String ageDescription
            String description
            String imageUrl
            String status
        }
        ADOPTION_REQUEST {
            Long id PK
            String applicantName
            String email
            String message
            DateTime createdAt
            String status
            Long catId FK
        }

* * *

## 🌐 4. Thymeleaf y API REST

### A. Thymeleaf: la web visible

Se utilizarán **Spring MVC y Thymeleaf** para generar las páginas HTML en el servidor.

* * *

Página Funcionamiento

* * *

**Inicio y catálogo**           El controlador obtiene los gatos y los entrega a la plantilla. Thymeleaf genera una tarjeta por cada gato mediante                                                          una iteración.

**Ficha del gato**             La plantilla recibe un objeto `Cat` y muestra sus datos dinámicamente.

**Formulario de adopción** El formulario se vincula a un objeto de entrada, muestra los errores de validación y vuelve a presentar la página                                                           con un mensaje de confirmación.

* * *

Con estas páginas se podrán demostrar:

* Expresiones de Thymeleaf.
* Iteraciones y condiciones.
* Enlaces dinámicos.
* Fragmentos reutilizables para la cabecera y el pie de página.
* Vinculación de formularios con Spring MVC.

### B. API REST: datos para otros clientes

Se añadirá una API propia con un controlador independiente anotado con`@RestController`. Su objetivo será ofrecer los datos de los gatos en**JSON**, sin devolver páginas HTML.

#### Endpoints mínimos

Método y endpoint Función

* * *

`GET /api/cats` Devuelve los gatos disponibles en JSON. `GET /api/cats/{id}` Devuelve la información de un gato concreto.

#### Ejemplo ilustrativo de respuesta

`GET /api/cats`

    [
      {
        "id": 1,
        "name": "Luna",
        "ageDescription": "2 años",
        "status": "AVAILABLE"
      }
    ]

> ℹ️ Este JSON es una representación orientativa, no código de implementación.

#### Criterios de calidad de la API

* Devolver datos en formato JSON.
* Utilizar códigos HTTP apropiados, incluido `404 Not Found` cuando el gato no existe.
* Exponer DTOs en lugar de devolver directamente las entidades JPA.
* Mantener las mismas reglas de negocio y disponibilidad que la web.
* Gestionar los errores de forma consistente.

### C. Cómo demostrar que ambas tecnologías funcionan

1. Abrir la web y comprobar que Thymeleaf muestra los gatos guardados en PostgreSQL.
2. Abrir `/api/cats` en el navegador o en Postman y observar los mismos gatos en JSON.
3. Consultar `/api/cats/{id}` y comprobar que devuelve la ficha correspondiente.
4. Enviar el formulario de adopción y verificar que la solicitud queda almacenada.
5. Consultar la base de datos para comprobar que la solicitud está vinculada al gato correcto.

* * *

## 🚦 5. Dificultad, riesgos y ampliaciones

**Dificultad general estimada:** baja--media.

### Riesgos principales

* * *

Riesgo Solución propuesta

* * *

**Gestión de imágenes** Subir imágenes directamente al servidor puede provocar pérdidas al reiniciar contenedores en Docker/Kubernetes.                                                        Es preferible guardar la URL de la imagen y utilizar almacenamiento de Supabase o un servicio externo como                                                        Cloudinary.

**Formularios y validaciones** Empezar con tres campos y validaciones básicas mediante Bean Validation.

* * *

### Ampliaciones opcionales

Estas tareas pueden añadirse cuando el MVP esté terminado.

* **Filtros en el catálogo:** filtrar por estado o por una característica básica.
* **Panel privado de administración:** añadir, editar y retirar gatos, además de consultar solicitudes. Requiere autenticación yautorización.
* **Notificaciones por correo:** utilizar Spring Mail para avisar al recibir o actualizar una solicitud de adopción.
* **Automatización con Jenkins:** crear una pipeline que compile el proyecto y ejecute pruebas cada vez que se integre un cambio.
* **Docker:** crear una imagen de la aplicación y ejecutar la web en un contenedor con la configuración externa.
* **Kubernetes:** desplegar la aplicación en un entorno de aprendizaje mediante un `Deployment` y un `Service`.
* **Donaciones simuladas:** integrar una pasarela de pago como Stripe en modo de pruebas.

* * *

## 🎯 6. Competencias que se pueden evaluar

* * *

Competencia             Evidencia en el proyecto

* * *

**Java y POO**                         Entidades, DTOs, enumeraciones y servicios.

**Maven**                                             Dependencias, configuración y compilación reproducible.

**Spring Boot**                 Configuración, inyección de dependencias y estructura de la aplicación.

**Spring MVC y Thymeleaf**     Controladores, vistas dinámicas y formularios.

**JPA e Hibernate**              Mapeo objeto-relacional, relaciones y persistencia.

**PostgreSQL y Supabase**      Diseño de tablas, claves foráneas y consultas.

**API REST**                     Endpoints propios, JSON, códigos HTTP y manejo de errores.

**Validación**                   Control de datos de entrada y mensajes comprensibles.

**Git y GitHub**                 Historial de cambios, ramas y documentación del repositorio.

**Pruebas**                    Comprobación de servicios, controladores y operaciones principales.

**Docker y CI/CD**              Imagen ejecutable y automatización de compilación y pruebas, si se completa la                                                             ampliación.

* * *

* * *

## ✅ Orden recomendado de trabajo

1. Crear el proyecto y configurar Maven, Spring Boot y la conexión con PostgreSQL.
2. Definir las entidades `Cat` y `AdoptionRequest` y su relación.
3. Implementar el acceso a datos y los servicios básicos.
4. Crear las páginas Thymeleaf: inicio, catálogo, ficha y formulario.
5. Añadir validaciones y guardar solicitudes de adopción.
6. Implementar y probar los dos endpoints de la API REST.
7. Documentar el proyecto y subirlo a GitHub.
8. Si queda tiempo, abordar las ampliaciones de Jenkins, Docker y Kubernetes.
