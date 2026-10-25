package es.iesjuanbosco.reservaaulas.controller;

import es.iesjuanbosco.reservaaulas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaRepository reservaRepository;
}
