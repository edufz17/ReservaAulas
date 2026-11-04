package es.iesjuanbosco.reservaaulas.backend.controller;

import es.iesjuanbosco.reservaaulas.backend.entity.Aula;
import es.iesjuanbosco.reservaaulas.backend.entity.FranjaHoraria;
import es.iesjuanbosco.reservaaulas.backend.entity.Profesor;
import es.iesjuanbosco.reservaaulas.backend.entity.Reserva;
import es.iesjuanbosco.reservaaulas.backend.repository.AulaRepository;
import es.iesjuanbosco.reservaaulas.backend.repository.FranjaHorariaRepository;
import es.iesjuanbosco.reservaaulas.backend.repository.ProfesorRepository;
import es.iesjuanbosco.reservaaulas.backend.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaControllerTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private FranjaHorariaRepository franjaHorariaRepository;

    @Mock
    private ProfesorRepository profesorRepository;

    private ReservaController reservaController;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        reservaController = new ReservaController(
                reservaRepository, aulaRepository, franjaHorariaRepository, profesorRepository);
        reserva = new Reserva();
        reserva.setFechaReserva(LocalDateTime.now().plusDays(1));
        reserva.setAula(entidadConId(Aula.class));
        reserva.setFranjaHoraria(entidadConId(FranjaHoraria.class));
        reserva.setProfesor(entidadConId(Profesor.class));
    }

    @Test
    void creaReservaCuandoLaFechaYLaFranjaEstanDisponibles() {
        when(aulaRepository.existsById(anyLong())).thenReturn(true);
        when(franjaHorariaRepository.existsById(anyLong())).thenReturn(true);
        when(profesorRepository.existsById(anyLong())).thenReturn(true);
        when(reservaRepository.existsByAulaIdAndFranjaHorariaIdAndFechaReservaBetween(
                anyLong(), anyLong(), any(), any())).thenReturn(false);
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        assertEquals(HttpStatus.CREATED, reservaController.create(reserva).getStatusCode());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void rechazaFechaAnteriorYNoGuarda() {
        reserva.setFechaReserva(LocalDateTime.now().minusDays(1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> reservaController.create(reserva));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void rechazaFranjaOcupadaYNoGuarda() {
        when(aulaRepository.existsById(anyLong())).thenReturn(true);
        when(franjaHorariaRepository.existsById(anyLong())).thenReturn(true);
        when(profesorRepository.existsById(anyLong())).thenReturn(true);
        when(reservaRepository.existsByAulaIdAndFranjaHorariaIdAndFechaReservaBetween(
                anyLong(), anyLong(), any(), any())).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class, () -> reservaController.create(reserva));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(reservaRepository, never()).save(any());
    }

    private <T> T entidadConId(Class<T> tipo) {
        if (tipo == Aula.class) {
            Aula aula = new Aula();
            aula.setId(1L);
            return tipo.cast(aula);
        }
        if (tipo == FranjaHoraria.class) {
            FranjaHoraria franjaHoraria = new FranjaHoraria();
            franjaHoraria.setId(1L);
            return tipo.cast(franjaHoraria);
        }
        Profesor profesor = new Profesor();
        profesor.setId(1L);
        return tipo.cast(profesor);
    }
}
