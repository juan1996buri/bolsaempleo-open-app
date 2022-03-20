package ec.edu.insteclrg.webapp.view.bean.admin;

import java.io.Serializable;
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
import ec.edu.insteclrg.service.crud.CiudadService;
import ec.edu.insteclrg.service.crud.ProvinciaService;
import ec.edu.insteclrg.service.crud.TipoContratoServicio;
import ec.edu.insteclrg.webapp.enums.MensajesTipo;
import ec.edu.insteclrg.webapp.utils.Mensajes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("view")
public class CiudadesDatosAdminBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Ciudad> elements;
	private Ciudad selectedElement;
	private List<Ciudad> selectedElements;
	
	private List<Provincia> listProvincias;
	
	@Autowired
	private ProvinciaService provinciaService;;
	
	@Autowired
	private CiudadService entityService;
	
	

	@PostConstruct
	public void init() {
		this.elements = entityService.buscarTodo(new Ciudad());
		listProvincias = provinciaService.buscarTodo(new Provincia());
	}

	public void openNew() {
		this.selectedElement = new Ciudad();
		this.selectedElement.setId(0L);
	}

	public void saveElement() {
		
		if (this.selectedElement.getId() == 0L) {
			
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
		System.err.println("resp"+this.selectedElement.getProvincia().getId());
		System.err.println("resp"+this.selectedElement.getId());
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
	
	

}
