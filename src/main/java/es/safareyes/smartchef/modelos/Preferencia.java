package es.safareyes.smartchef.modelos;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "preferencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Preferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Builder.Default
    @OneToMany(mappedBy = "preferencia", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioPreferencia> usuariosPreferencia = new ArrayList<>();
}
