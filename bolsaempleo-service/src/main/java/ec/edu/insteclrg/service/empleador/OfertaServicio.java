package ec.edu.insteclrg.service.empleador;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.empleador.Oferta;

import ec.edu.insteclrg.persistence.empleador.OfertaRepository;
import ec.edu.insteclrg.service.GenericCRUDServiceImpl;

@Service
public class OfertaServicio extends GenericCRUDServiceImpl<Oferta, Long> {

	@Autowired
	private OfertaRepository entityRepository;

	public List<Oferta> buscarPorUsuario(User user) {
		return entityRepository.findByUser(user);
	}

	@Override
	public Optional<Oferta> buscar(Oferta entity) {
		return entityRepository.findById(entity.getId());
	}

	public Optional<Oferta> buscarPorId(long id) {
		return entityRepository.findById(id);
	}

		
	
	

}
