package es.iesjuanbosco.reservaaulas.backend.service;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private FranjaHorariaRepository franjaHorariaRepository;

    @Mock
    private ProfesorRepository profesorRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Aula aula;
    private Profesor profesor;
    private FranjaHoraria franjaHoraria;

    @BeforeEach
    void setUp() {
        aula = new Aula();
        aula.setId(1L);
        aula.setNombre("Aula 101");
        aula.setCapacidad(30);
        aula.setTieneOrdenadores(true);

        profesor = new Profesor();
        profesor.setId(1L);
        profesor.setNombre("Carlos");
        profesor.setApellidos("Gómez");
        profesor.setEmail("carlos@example.com");
        profesor.setTelefono("600123456");

        franjaHoraria = new FranjaHoraria();
        franjaHoraria.setId(1L);
        franjaHoraria.setNombre("1ª Hora");
    }

    @Test
    void testCrearReservaValida() {
        Reserva reserva = new Reserva();
        reserva.setAula(aula);
        reserva.setProfesor(profesor);
        reserva.setFranjaHoraria(franjaHoraria);
        reserva.setFechaReserva(LocalDateTime.now().plusDays(2));

        when(aulaRepository.existsById(1L)).thenReturn(true);
        when(franjaHorariaRepository.existsById(1L)).thenReturn(true);
        when(profesorRepository.existsById(1L)).thenReturn(true);
        when(reservaRepository.existsByAulaIdAndFranjaHorariaIdAndFechaReservaBetween(
                eq(1L), eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva r = invocation.getArgument(0);
            r.setId(10L);
            return r;
        });

        Reserva creada = reservaService.create(reserva);

        assertNotNull(creada);
        assertEquals(10L, creada.getId());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void testCrearReservaFechaAnteriorFalla() {
        Reserva reserva = new Reserva();
        reserva.setAula(aula);
        reserva.setProfesor(profesor);
        reserva.setFranjaHoraria(franjaHoraria);
        reserva.setFechaReserva(LocalDateTime.now().minusDays(1));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            reservaService.create(reserva);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("anterior a la fecha actual"));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void testCrearReservaConflictoFranjaOcupadaFalla() {
        Reserva reserva = new Reserva();
        reserva.setAula(aula);
        reserva.setProfesor(profesor);
        reserva.setFranjaHoraria(franjaHoraria);
        reserva.setFechaReserva(LocalDateTime.now().plusDays(1));

        when(aulaRepository.existsById(1L)).thenReturn(true);
        when(franjaHorariaRepository.existsById(1L)).thenReturn(true);
        when(profesorRepository.existsById(1L)).thenReturn(true);
        when(reservaRepository.existsByAulaIdAndFranjaHorariaIdAndFechaReservaBetween(
                eq(1L), eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            reservaService.create(reserva);
        });

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertTrue(ex.getReason().contains("ya está ocupada"));
        verify(reservaRepository, never()).save(any());
    }
}
