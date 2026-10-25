package es.iesjuanbosco.reservaaulas.controller;

import es.iesjuanbosco.reservaaulas.entity.FranjaHoraria;
import es.iesjuanbosco.reservaaulas.repository.FranjaHorariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FranjaHorariaController {
    private final FranjaHorariaRepository franjaHorariaRepository;
}
