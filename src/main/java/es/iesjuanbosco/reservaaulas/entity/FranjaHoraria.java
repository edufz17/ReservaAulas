package es.iesjuanbosco.reservaaulas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FranjaHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;

    @JsonIgnore
    @OneToMany(mappedBy = "franjaHoraria", cascade = CascadeType.REMOVE)
    private List<Reserva> reserva;


}
