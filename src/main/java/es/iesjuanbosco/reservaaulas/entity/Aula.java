package es.iesjuanbosco.reservaaulas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int capacidad;
    private boolean ordenadores;

    @JsonIgnore
    @OneToMany(mappedBy = "aula", cascade = CascadeType.REMOVE)
    private List<Reserva> reservas;

}
