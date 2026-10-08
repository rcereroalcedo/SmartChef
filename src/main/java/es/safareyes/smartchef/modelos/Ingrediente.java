package es.safareyes.smartchef.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ingredientes")
@Getter @Setter @NoArgsConstructor
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Boolean vegetariano = false;

    @Column(name = "sin_gluten", nullable = false)
    private Boolean sinGluten = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cantidad", nullable = false, length = 15)
    private TipoCantidad tipoCantidad;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal precio;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal calorias;
}

