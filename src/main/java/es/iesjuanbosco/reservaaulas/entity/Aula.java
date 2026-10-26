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

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private Boolean tieneOrdenadores;

    @JsonIgnore
    @OneToMany(mappedBy = "aula", cascade = CascadeType.REMOVE)
    private List<Reserva> reservas;

}
