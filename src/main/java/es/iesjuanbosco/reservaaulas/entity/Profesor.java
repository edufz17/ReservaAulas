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
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String apellidos;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 15, nullable = false)
    private String telefono;

    @JsonIgnore
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.REMOVE)
    private List<Reserva> reservas;

}
