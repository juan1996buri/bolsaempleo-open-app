package ec.edu.insteclrg.persistence.candidato;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.candidato.Instruccion;

@Repository
public interface InstruccionRepository extends JpaRepository<Instruccion, Long> {

	List<Instruccion> findByUser(User user);
	
}
