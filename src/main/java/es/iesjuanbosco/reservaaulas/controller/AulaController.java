package es.iesjuanbosco.reservaaulas.controller;

import es.iesjuanbosco.reservaaulas.repository.AulaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AulaController {
    private final AulaRepository aulaRepository;
}
