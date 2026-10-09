package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.LineaListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILineaListaCompraRepository extends JpaRepository<LineaListaCompra, Integer> {
}
