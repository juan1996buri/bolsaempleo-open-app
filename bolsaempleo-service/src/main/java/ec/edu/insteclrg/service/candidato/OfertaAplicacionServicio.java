package ec.edu.insteclrg.service.candidato;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.candidato.OfertaAplicacion;
import ec.edu.insteclrg.domain.empleador.Oferta;
import ec.edu.insteclrg.persistence.candidato.OfertaAplicacionRepository;
import ec.edu.insteclrg.service.GenericCRUDServiceImpl;

@Service
public class OfertaAplicacionServicio extends GenericCRUDServiceImpl<OfertaAplicacion, Long> {
	
	@Autowired
	private OfertaAplicacionRepository repository;

	@Override
	public Optional<OfertaAplicacion> buscar(OfertaAplicacion entity) {		
		return repository.findById(entity.getId());
	}
	public List<OfertaAplicacion> buscarPorUsuario(User user){
		return repository.findByUser(user);
	}
	
	public List<OfertaAplicacion> buscarPorOferta(Oferta oferta){
		return repository.findByOferta(oferta);
	}

}
