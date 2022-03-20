package ec.edu.insteclrg.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.insteclrg.domain.NivelInstruccion;
import ec.edu.insteclrg.domain.TipoContrato;
@Repository
public interface TipoContratoRepository extends JpaRepository<TipoContrato, Long> {

}
