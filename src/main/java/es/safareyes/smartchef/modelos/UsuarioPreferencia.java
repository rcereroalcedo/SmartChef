package es.safareyes.smartchef.modelos;

import es.safareyes.smartchef.modelos.ids.UsuarioPreferenciaId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios_preferencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPreferencia {

    @EmbeddedId
    private UsuarioPreferenciaId id = new UsuarioPreferenciaId();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "preferencia_id")
    private Preferencia preferencia;

}
