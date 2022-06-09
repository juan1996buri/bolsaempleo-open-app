package ec.edu.insteclrg.persistence.candidato;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.candidato.OfertaAplicacion;
import ec.edu.insteclrg.domain.empleador.Oferta;

@Repository
public interface OfertaAplicacionRepository extends JpaRepository<OfertaAplicacion, Long> {
	List<OfertaAplicacion> findByUser(User user);

	List<OfertaAplicacion> findByOferta(Oferta oferta);

}
