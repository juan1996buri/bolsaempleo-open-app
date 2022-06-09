package ec.edu.insteclrg.webapp.view.bean.candidato;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ec.edu.insteclrg.domain.Ciudad;
import ec.edu.insteclrg.domain.NivelInstruccion;
import ec.edu.insteclrg.domain.Provincia;
import ec.edu.insteclrg.domain.TipoContrato;
import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.candidato.Instruccion;
import ec.edu.insteclrg.domain.candidato.OfertaAplicacion;
import ec.edu.insteclrg.domain.empleador.Oferta;
import ec.edu.insteclrg.service.candidato.InstruccionService;
import ec.edu.insteclrg.service.candidato.OfertaAplicacionServicio;
import ec.edu.insteclrg.service.crud.CiudadService;
import ec.edu.insteclrg.service.crud.NivelInstruccionService;
import ec.edu.insteclrg.service.crud.ProvinciaService;
import ec.edu.insteclrg.service.crud.TipoContratoServicio;
import ec.edu.insteclrg.service.empleador.OfertaServicio;
import ec.edu.insteclrg.webapp.enums.MensajesTipo;
import ec.edu.insteclrg.webapp.utils.Mensajes;
import ec.edu.insteclrg.webapp.view.bean.LoginBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("view")
public class OfertaAplicacionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Oferta> elements;
	private Oferta selectedElement;

	private User user;
	private OfertaAplicacion ofertaAplicacion;

	@Autowired
	private LoginBean loginBean;
	@Autowired
	private OfertaServicio entityService;
	@Autowired
	private OfertaAplicacionServicio ofertaAplicacionServicio;

	@PostConstruct
	public void init() {
		this.user = loginBean.getUser();
		//this.elements = entityService.buscarTodo(new Oferta());
		try {
			this.elements = entityService.buscarTodo(new Oferta());
		} catch (Exception e) {
			
		}
		
	}

	public void openNew() {
		this.selectedElement = new Oferta();
	}

	public void enviarSolicitud() {
		this.ofertaAplicacion = new OfertaAplicacion();
		ofertaAplicacion.setOferta(selectedElement);
		ofertaAplicacion.setUser(user);
		LocalDate localDate = LocalDate.now();
		ofertaAplicacion.setFechaRegistro(localDate);
		
		ofertaAplicacionServicio.guardar(ofertaAplicacion);
		Mensajes.addMsg(MensajesTipo.INFORMACION, "Solicitud enviado");
		PrimeFaces.current().ajax().update("frm:growl", "frm:dt-elements");
	}

}
