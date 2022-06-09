package ec.edu.insteclrg.service.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.insteclrg.domain.admin.DatosAdmin;
import ec.edu.insteclrg.persistence.admin.DatosAdminRepository;
import ec.edu.insteclrg.service.GenericCRUDServiceImpl;

@Service
public class DatosAdminService extends GenericCRUDServiceImpl<DatosAdmin, Long> {

	@Autowired
	private DatosAdminRepository entityRepository;

	@Override
	public Optional<DatosAdmin> buscar(DatosAdmin entity) {
		return entityRepository.findById(entity.getId());
	}

	public Optional<DatosAdmin> buscarPorCedula(String cedula) {
		//Sort.by(Sort.Direction.ASC
		return entityRepository.findByCedula(cedula);
	}

}
