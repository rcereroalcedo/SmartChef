package es.safareyes.smartchef.modelos;
import jakarta.persistence.*;
import lombok.*;

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
}
