package ec.edu.insteclrg.webapp.view.bean.empleador;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ec.edu.insteclrg.domain.Ciudad;
import ec.edu.insteclrg.domain.Provincia;
import ec.edu.insteclrg.domain.candidato.DatosCandidato;
import ec.edu.insteclrg.domain.empleador.DatosEmpleador;
import ec.edu.insteclrg.service.candidato.DatosCandidatoService;
import ec.edu.insteclrg.service.crud.CiudadService;
import ec.edu.insteclrg.service.crud.ProvinciaService;
import ec.edu.insteclrg.service.crud.UserService;
import ec.edu.insteclrg.service.empleador.DatosEmpleadorService;
import ec.edu.insteclrg.webapp.enums.MensajeError;
import ec.edu.insteclrg.webapp.enums.MensajeOk;
import ec.edu.insteclrg.webapp.enums.MensajesTipo;
import ec.edu.insteclrg.webapp.utils.Constants;
import ec.edu.insteclrg.webapp.utils.Mensajes;
import ec.edu.insteclrg.webapp.utils.Utils;
import ec.edu.insteclrg.webapp.view.bean.LoginBean;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Component
@Scope("view")
public class DatosGeneralesBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Provincia> provincias;
	private Provincia selectedProvincia;

	private List<Ciudad> ciudades;
	private Ciudad selectedCiudad;

	private DatosEmpleador empleador;
	private String fechaNacimiento;
	private String foto;
	private String fotoTmp;
	
	

	@Autowired
	private ProvinciaService provinciaService;

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private DatosEmpleadorService empleadorService;

	@Autowired
	private UserService userService;

	@Autowired
	LoginBean loginBean;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@PostConstruct
	public void init() {
		this.ciudades = new ArrayList<Ciudad>();
		provincias = this.provinciaService.buscarTodo(new Provincia());
		Optional<DatosEmpleador> empleadorOptional = Optional.empty();
		
		empleadorOptional = empleadorService.buscarPorRuc(loginBean.getUser().getUsername());
		
		
		if (!empleadorOptional.isPresent()) {
			Utils.redirectToPage(Constants.URI_WEB_404);
		}
		empleador = empleadorOptional.get();
		
		
		Map<Long, Provincia> provinciasMap = provincias.stream()
				.collect(Collectors.toMap(Provincia::getId, student -> student));
		selectedProvincia = provinciasMap.get(empleador.getCiudad().getProvincia().getId());
		if (!provincias.isEmpty()) {
			this.ciudades = ciudadService.buscarPorProvincia(this.selectedProvincia);
		}
		Map<Long, Ciudad> ciudadesMap = ciudades.stream().collect(Collectors.toMap(Ciudad::getId, ciudad -> ciudad));
		selectedCiudad = ciudadesMap.get(empleador.getCiudad().getId());

		if (empleador.getLogo() != null) {
			foto = Base64.getEncoder().encodeToString(empleador.getLogo());
			fotoTmp = foto;
		}
		
		
		
	}

	public void guardar() {
		byte[] fotoByte = null;
		if (foto != null)
			if (!foto.isEmpty())
				fotoByte = Base64.getDecoder().decode(foto);
		
	
		empleador.setLogo(fotoByte);

		empleador.setCiudad(selectedCiudad);
		try {
			if (empleadorService.actualizar(empleador) != null) {
				Mensajes.addMsg(MensajesTipo.INFORMACION, MensajeOk.ACTUALIZADO_OK.toString()
						+ ". Cierre la sesión e ingrese de nuevo para ver los cambios");
			} else {
				Mensajes.addMsg(MensajesTipo.ERROR, MensajeError.NO_GUARDADO.toString());
			}
		} catch (Exception e) {
			Mensajes.addMsg(MensajesTipo.ERROR, e.getMessage());
		}

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

	

	public void returnDesktop() {
		Utils.redirectToPage("index.xhtml");
	}
}
