package es.iesjuanbosco.reservaaulas.backend.controller;

import es.iesjuanbosco.reservaaulas.backend.entity.Reserva;
import es.iesjuanbosco.reservaaulas.backend.repository.AulaRepository;
import es.iesjuanbosco.reservaaulas.backend.repository.FranjaHorariaRepository;
import es.iesjuanbosco.reservaaulas.backend.repository.ProfesorRepository;
import es.iesjuanbosco.reservaaulas.backend.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final AulaRepository aulaRepository;
    private final FranjaHorariaRepository franjaHorariaRepository;
    private final ProfesorRepository profesorRepository;

    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> findAll() {
        return ResponseEntity.ok(reservaRepository.findAll());
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Long id) {
        return reservaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/reservas")
    public ResponseEntity<Reserva> create(@RequestBody Reserva reserva) {
        validarReserva(reserva);
        reserva.setId(null);
        Reserva guardada = reservaRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<Reserva> update(@PathVariable Long id, @RequestBody Reserva reserva) {
        if (!reservaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reserva.setId(id);
        return ResponseEntity.ok(reservaRepository.save(reserva));
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!reservaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reservaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validarReserva(Reserva reserva) {
        if (reserva == null || reserva.getFechaReserva() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La fecha de la reserva es obligatoria.");
        }
        if (reserva.getFechaReserva().toLocalDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de la reserva no puede ser anterior a la fecha actual.");
        }
        if (reserva.getAula() == null || reserva.getAula().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El aula es obligatoria.");
        }
        if (reserva.getFranjaHoraria() == null || reserva.getFranjaHoraria().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La franja horaria es obligatoria.");
        }
        if (reserva.getProfesor() == null || reserva.getProfesor().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El profesor es obligatorio.");
        }
        if (!aulaRepository.existsById(reserva.getAula().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El aula indicada no existe.");
        }
        if (!franjaHorariaRepository.existsById(reserva.getFranjaHoraria().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "La franja horaria indicada no existe.");
        }
        if (!profesorRepository.existsById(reserva.getProfesor().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "El profesor indicado no existe.");
        }

        LocalDate fecha = reserva.getFechaReserva().toLocalDate();
        boolean ocupada = reservaRepository
                .existsByAulaIdAndFranjaHorariaIdAndFechaReservaBetween(
                        reserva.getAula().getId(),
                        reserva.getFranjaHoraria().getId(),
                        fecha.atStartOfDay(),
                        fecha.atTime(LocalTime.MAX));
        if (ocupada) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La franja horaria seleccionada ya está ocupada para la fecha indicada.");
        }
    }
}
