package ec.edu.insteclrg.webapp.view.bean.candidato;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ec.edu.insteclrg.domain.NivelInstruccion;
import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.candidato.Instruccion;
import ec.edu.insteclrg.service.candidato.InstruccionService;
import ec.edu.insteclrg.service.crud.NivelInstruccionService;
import ec.edu.insteclrg.webapp.enums.MensajesTipo;
import ec.edu.insteclrg.webapp.utils.Mensajes;
import ec.edu.insteclrg.webapp.view.bean.LoginBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("view")
public class InstruccionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Instruccion> elements;
	private Instruccion selectedElement;
	private List<Instruccion> selectedElements;
	private List<NivelInstruccion> niveles;

	private User user;
	private NivelInstruccion selectedNivel;

	@Autowired
	private LoginBean loginBean;
	@Autowired
	private InstruccionService entityService;
	@Autowired
	private NivelInstruccionService nivelInstruccionService;

	@PostConstruct
	public void init() {
		this.user = loginBean.getUser();
		this.elements = entityService.buscarPorUsuario(user);
		niveles = nivelInstruccionService.buscarTodo(new NivelInstruccion());
	}

	public void openNew() {
		this.selectedElement = new Instruccion();
	}

	public void saveElement() {
		if (this.selectedElement.getId() == 0L) {
			selectedElement.setUser(user);
			this.elements.add(this.selectedElement);
			entityService.guardar(selectedElement);
			Mensajes.addMsg(MensajesTipo.INFORMACION, "Registro agregado");
		} else {
			entityService.actualizar(selectedElement);
			Mensajes.addMsg(MensajesTipo.INFORMACION, "Registro actualizado");
		}

		PrimeFaces.current().executeScript("PF('manageElementDialog').hide()");
		PrimeFaces.current().ajax().update("frm:growl", "frm:dt-elements");
	}

	public void deleteElement() {
		entityService.eliminar(selectedElement);
		this.elements.remove(this.selectedElement);
		this.selectedElement = null;
		Mensajes.addMsg(MensajesTipo.INFORMACION, "Registro eliminado");
		PrimeFaces.current().ajax().update("frm:growl", "frm:dt-elements");
	}

	public String getDeleteButtonMessage() {
		if (hasSelectedElements()) {
			int size = this.selectedElements.size();
			return size > 1 ? size + " registros seleccionados" : "1 registro seleccionado";
		}
		return "Eliminar";
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

	public void loadDialog() {
		Map<Long, NivelInstruccion> nivelesMap = niveles.stream()
				.collect(Collectors.toMap(NivelInstruccion::getId, nivel -> nivel));
		selectedElement.setNivelInstruccion(nivelesMap.get(selectedElement.getNivelInstruccion().getId()));
	}
}