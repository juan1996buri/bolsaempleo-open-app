package ec.edu.insteclrg.service.crud;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.insteclrg.domain.TipoContrato;

import ec.edu.insteclrg.persistence.TipoContratoRepository;
import ec.edu.insteclrg.service.GenericCRUDServiceImpl;

@Service
public class TipoContratoServicio extends GenericCRUDServiceImpl<TipoContrato, Long> {

	@Autowired
	private TipoContratoRepository entityRepository;

	@Override
	public Optional<TipoContrato> buscar(TipoContrato entity) {
		return entityRepository.findById(entity.getId());
	}

}
