package es.safareyes.smartchef.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recetas")
@Getter @Setter @NoArgsConstructor
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Integer raciones;

    @Column(nullable = false)
    private Integer tiempo;

    @Column(nullable = false)
    private Integer calorias;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Dificultad dificultad;

    @Column(columnDefinition = "text")
    private String descripcion;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("numero ASC")
    private List<Paso> pasos = new ArrayList<>();

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("esPortada DESC, id ASC")
    private List<Foto> fotos = new ArrayList<>();

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecetaIngrediente> ingredientes = new ArrayList<>();

    @Column(nullable = false)
    private Boolean activa = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;
}

