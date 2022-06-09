package ec.edu.insteclrg.webapp.view.bean.empleador;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ec.edu.insteclrg.domain.Ciudad;
import ec.edu.insteclrg.domain.Provincia;
import ec.edu.insteclrg.domain.TipoContrato;
import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.candidato.OfertaAplicacion;
import ec.edu.insteclrg.domain.empleador.Oferta;
import ec.edu.insteclrg.service.candidato.OfertaAplicacionServicio;
import ec.edu.insteclrg.service.crud.CiudadService;
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
public class OfertaRecepcionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<OfertaAplicacion> elements;
	private List<OfertaAplicacion> respaldo;
	private OfertaAplicacion selectedElement;
	private List<OfertaAplicacion> selectedElements;

	private User user;

	private Oferta oferta;

	@Autowired
	private ProvinciaService provinciaService;

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private LoginBean loginBean;
	@Autowired
	private OfertaAplicacionServicio entityService;
	@Autowired
	private TipoContratoServicio tipoContratoServicio;

	@PostConstruct
	public void init() {
		
		try {
			this.user = loginBean.getUser();		
			this.elements = new ArrayList<OfertaAplicacion>();
			this.respaldo = entityService.buscarTodo(new OfertaAplicacion());
			for (int i = 0; i < respaldo.size(); i++) {
				if (respaldo.get(i).getOferta().getUser().getUsername().equals(user.getUsername())) {
					this.elements.add(respaldo.get(i));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void openNew() {
		this.selectedElement = new OfertaAplicacion();
	}



	public void aceptarElemento() {
		
		Mensajes.addMsg(MensajesTipo.INFORMACION, "Registro eliminado");
		PrimeFaces.current().ajax().update("frm:growl", "frm:dt-elements");
	}


	public boolean hasSelectedElements() {
		return this.selectedElements != null && !this.selectedElements.isEmpty();
	}

	public void deleteSelectedElements() {
		for (int i = 0; i < this.selectedElements.size(); i++) {
			entityService.eliminar(selectedElements.get(i));
		}
		this.elements.removeAll(this.selectedElements);
		this.selectedElements = null;

		Mensajes.addMsg(MensajesTipo.INFORMACION, "Registros eliminados");
		PrimeFaces.current().ajax().update("frm:growl", "frm:dt-elements");
		PrimeFaces.current().executeScript("PF('dtElementos').clearFilters()");
	}

}
