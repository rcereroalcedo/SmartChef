package es.safareyes.smartchef.repositorios;

import es.safareyes.smartchef.modelos.ListaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IListaCompraRepository extends JpaRepository<ListaCompra, Long> {
}
