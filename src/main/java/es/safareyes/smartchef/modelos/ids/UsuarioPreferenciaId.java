package es.safareyes.smartchef.modelos.ids;
import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Data
@Embeddable
public class UsuarioPreferenciaId implements Serializable {

    private Long usuarioId;
    private Long preferenciaId;

}
