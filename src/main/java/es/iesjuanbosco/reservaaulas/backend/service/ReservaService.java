package es.iesjuanbosco.reservaaulas.backend.service;

import es.iesjuanbosco.reservaaulas.backend.entity.Reserva;
import es.iesjuanbosco.reservaaulas.backend.repository.AulaRepository;
import es.iesjuanbosco.reservaaulas.backend.repository.FranjaHorariaRepository;
import es.iesjuanbosco.reservaaulas.backend.repository.ProfesorRepository;
import es.iesjuanbosco.reservaaulas.backend.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final AulaRepository aulaRepository;
    private final FranjaHorariaRepository franjaHorariaRepository;
    private final ProfesorRepository profesorRepository;

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    public Reserva create(Reserva reserva) {
        if (reserva.getFechaReserva() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de la reserva es obligatoria.");
        }

        // Validación: La fecha de la reserva no puede ser anterior a la fecha actual
        if (reserva.getFechaReserva().toLocalDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de la reserva no puede ser anterior a la fecha actual.");
        }

        if (reserva.getAula() == null || reserva.getAula().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El aula es obligatoria.");
        }
        if (reserva.getFranjaHoraria() == null || reserva.getFranjaHoraria().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La franja horaria es obligatoria.");
        }
        if (reserva.getProfesor() == null || reserva.getProfesor().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El profesor es obligatorio.");
        }

        if (!aulaRepository.existsById(reserva.getAula().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El aula indicada no existe.");
        }
        if (!franjaHorariaRepository.existsById(reserva.getFranjaHoraria().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La franja horaria indicada no existe.");
        }
        if (!profesorRepository.existsById(reserva.getProfesor().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El profesor indicado no existe.");
        }

        // Validación: La franja horaria seleccionada debe estar libre para la fecha indicada
        LocalDate fecha = reserva.getFechaReserva().toLocalDate();
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(LocalTime.MAX);

        boolean yaOcupada = reservaRepository.existsByAulaIdAndFranjaHorariaIdAndFechaReservaBetween(
                reserva.getAula().getId(),
                reserva.getFranjaHoraria().getId(),
                inicioDia,
                finDia
        );

        if (yaOcupada) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La franja horaria seleccionada ya está ocupada para la fecha indicada.");
        }

        reserva.setId(null);
        return reservaRepository.save(reserva);
    }

    public Optional<Reserva> update(Long id, Reserva reserva) {
        if (!reservaRepository.existsById(id)) {
            return Optional.empty();
        }
        reserva.setId(id);
        return Optional.of(reservaRepository.save(reserva));
    }

    public boolean delete(Long id) {
        if (!reservaRepository.existsById(id)) {
            return false;
        }
        reservaRepository.deleteById(id);
        return true;
    }
}
