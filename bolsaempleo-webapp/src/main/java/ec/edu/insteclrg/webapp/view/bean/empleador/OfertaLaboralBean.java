package ec.edu.insteclrg.webapp.view.bean.empleador;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.Element;

import ec.edu.insteclrg.domain.Ciudad;
import ec.edu.insteclrg.domain.NivelInstruccion;
import ec.edu.insteclrg.domain.Provincia;
import ec.edu.insteclrg.domain.TipoContrato;
import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.candidato.Instruccion;
import ec.edu.insteclrg.domain.empleador.Oferta;
import ec.edu.insteclrg.service.candidato.InstruccionService;
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
public class OfertaLaboralBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Provincia> provincias;
	private Provincia selectedProvincia;

	private List<Ciudad> ciudades;
	private Ciudad selectedCiudad;

	private List<Oferta> elements;
	private Oferta selectedElement;
	private List<Oferta> selectedElements;
	private List<TipoContrato>  contrato;//niveles;

	private User user;
	private TipoContrato selectedNivel;
	
	private String foto;
	private String fotoTmp;
	
	@Autowired
	private ProvinciaService provinciaService;

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private LoginBean loginBean;
	@Autowired
	private OfertaServicio entityService;
	@Autowired
	private TipoContratoServicio tipoContratoServicio;

	@PostConstruct
	public void init() {
		
		
		this.user = loginBean.getUser();
		this.elements = entityService.buscarPorUsuario(user);
		contrato = tipoContratoServicio.buscarTodo(new TipoContrato());
		
		provincias = this.provinciaService.buscarTodo(new Provincia());
		
		if (provincias.size() > 0) {
			selectedProvincia = provincias.get(0);
			this.ciudades = ciudadService.buscarPorProvincia(this.selectedProvincia);
			if (ciudades.size() > 0) {
				selectedCiudad = ciudades.get(0);
			}
		}
		
		
		
		
	}

	public void openNew() {
		this.selectedElement = new Oferta();
		foto=null;
		fotoTmp=null;
	}
	
	

	public void saveElement() {
		
		byte[] fotoByte = null;
		if (foto != null)
			if (!foto.isEmpty())
				fotoByte = Base64.getDecoder().decode(foto);		
		selectedElement.setBanner(fotoByte);
		
		
		if (this.selectedElement.getId() == 0L) {
			
			
			selectedElement.setUser(user);
			this.elements.add(this.selectedElement);
			LocalDate localDate = LocalDate.now();

			selectedElement.setFechaPublicacion(localDate);
			selectedElement.setCiudad(selectedCiudad);
			entityService.guardar(selectedElement);
			Mensajes.addMsg(MensajesTipo.INFORMACION, "Registro agregado");
		} else {
			
			
			selectedElement.setCiudad(selectedCiudad);
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
		
			if(this.selectedElement.getBanner()!=null) {
				foto = Base64.getEncoder().encodeToString(selectedElement.getBanner());
				fotoTmp = foto;
			}else {
				fotoTmp=null;
				foto=null;
			}
		
			
	
		
	
		/*Map<Long, TipoContrato> nivelesMap = contrato.stream()
				.collect(Collectors.toMap(TipoContrato::getId, nivel -> nivel));
		selectedElement.setTipoContrato(nivelesMap.get(selectedElement.getTipoContrato().getId()));*/
	}
	
	
	
	public void onProvinciaChange() {
		if (selectedProvincia != null)
			ciudades = ciudadService.buscarPorProvincia(selectedProvincia);
		else
			ciudades.clear();
	}
	
	public void changePic() {
		foto = fotoTmp;
		// if (foto.isEmpty() || foto == null)
		// foto = SINFOTO;
		// PrimeFaces.current().ajax().update("frm");
	}
}
