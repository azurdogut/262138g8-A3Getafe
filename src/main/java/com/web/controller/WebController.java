package com.web.controller;

import com.web.model.AdoptionRequest;
import com.web.model.Cat;
import com.web.repository.AdoptionRequestRepository;
import com.web.repository.CatRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final CatRepository catRepository;
    private final AdoptionRequestRepository adoptionRequestRepository;

    // 1. Página de inicio
    @GetMapping("/")
    public String index(Model model) {
        // Mostramos una selección de gatos en el inicio (por ejemplo, los primeros 4)
        model.addAttribute("cats", catRepository.findAll(Sort.by(Sort.Direction.ASC, "status")));
        return "index";
    }

    // 2. Catálogo completo de gatos
    @GetMapping("/cats")
    public String catalog(Model model) {
        model.addAttribute("cats", catRepository.findAll(Sort.by(Sort.Direction.ASC, "status")));
        return "catalog";
    }

    // 3. Ficha individual de cada gato
    @GetMapping("/cats/{id}")
    public String catDetail(@PathVariable Long id, Model model) {
        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Gato no encontrado con ID: " + id));
        model.addAttribute("cat", cat);
        return "cat-detail";
    }

    // 4. Formulario de interés en adoptar (GET)
    @GetMapping("/adopt/{catId}")
    public String showAdoptForm(@PathVariable Long catId, Model model) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Gato no encontrado con ID: " + catId));

        AdoptionRequest adoptionRequest = new AdoptionRequest();
        adoptionRequest.setCat(cat);

        model.addAttribute("cat", cat);
        model.addAttribute("adoptionRequest", adoptionRequest);
        return "adopt-form";
    }

    // 4. Procesar formulario de adopción (POST con validación)
    @PostMapping("/adopt")
    public String processAdoptForm(@Valid @ModelAttribute("adoptionRequest") AdoptionRequest adoptionRequest,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Si hay errores de validación, recargamos el gato asociado para mostrar la vista correctamente
            Cat cat = catRepository.findById(adoptionRequest.getCat().getId())
                    .orElseThrow();
            model.addAttribute("cat", cat);
            return "adopt-form";
        }

        adoptionRequestRepository.save(adoptionRequest);
        return "redirect:/success";
    }

    // Página de éxito
    @GetMapping("/success")
    public String success() {
        return "success";
    }

    // 5. Página "Colabora"
    @GetMapping("/colabora")
    public String colabora() {
        return "colabora";
    }
}