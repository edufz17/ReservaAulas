package es.iesjuanbosco.reservaaulas.controller;

import es.iesjuanbosco.reservaaulas.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfesorController {
    private final ProfesorRepository profesorRepository;
}
