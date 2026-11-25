package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.EstadoListaCompra;
import es.safareyes.smartchef.modelos.ListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IListaCompraRepository extends JpaRepository<ListaCompra, Long> {

    Optional<ListaCompra> findFirstByUsuarioIdAndEstado(Long usuarioId, EstadoListaCompra estado);
}