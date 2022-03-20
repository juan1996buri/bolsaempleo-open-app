package ec.edu.insteclrg.persistence.empleador;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.candidato.Instruccion;
import ec.edu.insteclrg.domain.empleador.Oferta;
@Repository
public interface OfertaRepository  extends JpaRepository<Oferta, Long> {

	List<Oferta> findByUser(User user);
}
