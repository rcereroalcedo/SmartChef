package es.safareyes.smartchef.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false, length = 300)
    private String texto;

    @Column(nullable = false)
    private Boolean leida;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuarios",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> usuarios = new HashSet<>();
}
