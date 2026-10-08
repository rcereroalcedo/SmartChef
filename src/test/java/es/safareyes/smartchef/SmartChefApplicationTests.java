package es.safareyes.smartchef;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import es.safareyes.smartchef.modelos.Dificultad;
import es.safareyes.smartchef.repositorios.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ConsultasGetTest {

    @Autowired IRecetaRepository recetaRepository;
    @Autowired IPasoRepository pasoRepository;
    @Autowired IFotoRepository fotoRepository;
    @Autowired IRecetaIngredienteRepository recetaIngredienteRepository;
    @Autowired IIngredienteRepository ingredienteRepository;
    @Autowired IDespensaRepository despensaRepository;
    @Autowired IUsuarioRepository usuarioRepository;

    private Long idReceta(String nombre) {
        return recetaRepository.findAll().stream()
                .filter(r -> r.getNombre().equals(nombre))
                .findFirst().orElseThrow().getId();
    }

//    // GET /recetas
//    @Test
//    void catalogoSinFiltros() {
//        var pagina = recetaRepository.buscarCatalogo(
//                "", null, null, null, false, false, PageRequest.of(0, 10));
//        pagina.forEach(System.out::println);
//        assertTrue(pagina.getTotalElements() >= 2);
//    }
//
//    @Test
//    void catalogoFiltroNombreCaloriasYVegetariana() {
//        var pagina = recetaRepository.buscarCatalogo(
//                "pasta", null, 600, null, true, false, PageRequest.of(0, 10));
//        pagina.forEach(System.out::println);
//        assertEquals(1, pagina.getTotalElements());
//        assertEquals("Pasta al tomate", pagina.getContent().get(0).get("nombre"));
//    }
//
//    @Test
//    void catalogoFiltroDificultad() {
//        var pagina = recetaRepository.buscarCatalogo(
//                "", Dificultad.MEDIA, null, null, false, false, PageRequest.of(0, 10));
//        pagina.forEach(System.out::println);
//        assertEquals(1, pagina.getTotalElements());
//        assertEquals("Pollo asado", pagina.getContent().get(0).get("nombre"));
//    }
//
//    @Test
//    void catalogoFiltroSinGluten() {
//        var pagina = recetaRepository.buscarCatalogo(
//                "", null, null, null, false, true, PageRequest.of(0, 10));
//        pagina.forEach(System.out::println);
//        assertEquals(1, pagina.getTotalElements());
//        assertEquals("Pollo asado", pagina.getContent().get(0).get("nombre"));
//    }
//
//    // GET /recetas/{id}
//    @Test
//    void detalleReceta() {
//        Long id = idReceta("Pasta al tomate");
//
//        var cabecera = recetaRepository.findCabecera(id);
//        System.out.println(cabecera);
//        assertTrue(cabecera.isPresent());
//
//        var pasos = pasoRepository.findPasosDeReceta(id);
//        System.out.println(pasos);
//        assertEquals(2, pasos.size());
//        assertEquals(1, pasos.get(0).get("numero"));
//
//        var fotos = fotoRepository.findFotosDeReceta(id);
//        System.out.println(fotos);
//        assertEquals(1, fotos.size());
//
//        var ingredientes = recetaIngredienteRepository.findIngredientesDeReceta(id);
//        System.out.println(ingredientes);
//        assertEquals(2, ingredientes.size());
//
//        BigDecimal coste = recetaIngredienteRepository.calcularCoste(id);
//        System.out.println("Coste: " + coste);
//        assertEquals(0, new BigDecimal("1.30").compareTo(coste));
//    }
//
//    @Test
//    void detalleMarcasDieteticas() {
//        Long pasta = idReceta("Pasta al tomate");
//        Long pollo = idReceta("Pollo asado");
//
//        // Pasta: vegetariana (0 ingredientes no vegetarianos) pero con gluten
//        assertEquals(0, recetaIngredienteRepository.countByRecetaIdAndIngredienteVegetarianoFalse(pasta));
//        assertTrue(recetaIngredienteRepository.countByRecetaIdAndIngredienteSinGlutenFalse(pasta) > 0);
//
//        // Pollo: no vegetariana pero sin gluten
//        assertTrue(recetaIngredienteRepository.countByRecetaIdAndIngredienteVegetarianoFalse(pollo) > 0);
//        assertEquals(0, recetaIngredienteRepository.countByRecetaIdAndIngredienteSinGlutenFalse(pollo));
//    }
//
//    @Test
//    void detalleRecetaInexistente() {
//        assertTrue(recetaRepository.findCabecera(-1L).isEmpty());
//    }

    // GET /ingredientes?texto=
    @Test
    void catalogoIngredientes() {
        var pagina = ingredienteRepository.findByNombreContainingIgnoreCase("TO", PageRequest.of(0, 10));
        pagina.forEach(i -> System.out.println(i.getNombre()));
        assertEquals(1, pagina.getTotalElements()); // Tomate
    }

    @Test
    void catalogoIngredientesSinTextoDevuelveTodos() {
        var pagina = ingredienteRepository.findByNombreContainingIgnoreCase("", PageRequest.of(0, 10));
        assertTrue(pagina.getTotalElements() >= 3);
    }

    // GET /despensa
    @Test
    void despensaOrdenadaPorCaducidad() {
        Long usuarioId = usuarioRepository.findAll().stream()
                .filter(u -> u.getNombre().equals("ana"))
                .findFirst().orElseThrow().getId();

        var items = despensaRepository.findDespensaOrdenada(usuarioId);
        items.forEach(System.out::println);

        assertEquals(3, items.size());
        assertEquals("Pollo", items.get(0).get("nombre"));  // caduca el 9/10
        assertEquals("Tomate", items.get(1).get("nombre")); // caduca el 12/10
        assertEquals("Pasta", items.get(2).get("nombre"));  // sin fecha, al final
    }
}
