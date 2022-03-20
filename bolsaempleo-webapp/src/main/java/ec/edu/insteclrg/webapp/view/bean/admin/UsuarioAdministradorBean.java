package ec.edu.insteclrg.webapp.view.bean.admin;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ec.edu.insteclrg.common.enums.UserRole;
import ec.edu.insteclrg.domain.Authority;
import ec.edu.insteclrg.domain.Ciudad;
import ec.edu.insteclrg.domain.Provincia;
import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.admin.DatosAdmin;
import ec.edu.insteclrg.dto.PasswordDTO;
import ec.edu.insteclrg.service.admin.DatosAdminService;
import ec.edu.insteclrg.service.crud.CiudadService;
import ec.edu.insteclrg.service.crud.ProvinciaService;
import ec.edu.insteclrg.service.crud.UserService;
import ec.edu.insteclrg.webapp.enums.MensajeError;
import ec.edu.insteclrg.webapp.enums.MensajeOk;
import ec.edu.insteclrg.webapp.enums.MensajesTipo;
import ec.edu.insteclrg.webapp.utils.Mensajes;
import ec.edu.insteclrg.webapp.utils.Utils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("view")
public class UsuarioAdministradorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Provincia> provincias;
	private Provincia selectedProvincia;
	private List<Ciudad> ciudades;
	private Ciudad selectedCiudad;

	private String password;
	private String repitePassword;

	private List<DatosAdmin> elements;
	private DatosAdmin selectedElement;
	private List<DatosAdmin> selectedElements;

	private User user;

	private String passwordRepite;
	private PasswordDTO passwordDto;
	@Autowired
	HttpServletRequest httpServletRequest;

	@Autowired
	private UserService userService;
	@Autowired
	private DatosAdminService adminService;
	@Autowired
	private ProvinciaService provinciaService;
	@Autowired
	private CiudadService ciudadService;
	@Autowired
	private DatosAdminService entityService;

	@PostConstruct
	public void init() {
		this.elements = entityService.buscarTodo(new DatosAdmin());

		this.ciudades = new ArrayList<Ciudad>();
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
		this.selectedElement = new DatosAdmin();
		user = new User();
	}

	public void saveElement() {
		if (this.selectedElement.getId() == 0L) {
			user.setUsername(selectedElement.getCedula());
			user.setPassword(password);
			user.setEnabled(true);
			Authority aut = new Authority();
			aut.setId(UserRole.ROLE_ADMIN.getId());
			aut.setAuthority(UserRole.ROLE_ADMIN.toString());
			selectedElement.setCiudad(selectedCiudad);
			LocalDate localDate = LocalDate.now();
			selectedElement.setFechaRegistro(localDate);
			Set<Authority> auts = new HashSet<Authority>();
			auts.add(aut);

			user.setAuthority(auts);
			try {
				if (userService.guardar(user) != null) {
					selectedElement.setUser(user);
					if (adminService.guardar(selectedElement) != null) {
						resetEntity();
						Mensajes.addMsg(MensajesTipo.INFORMACION, "Registro agregado");
					} else {
						Mensajes.addMsg(MensajesTipo.ERROR, MensajeError.NO_GUARDADO.toString());
					}
				} else {
					Mensajes.addMsg(MensajesTipo.ERROR, MensajeError.NO_GUARDADO.toString());
				}
			} catch (Exception e) {
				Mensajes.addMsg(MensajesTipo.ERROR, e.getMessage());
			}
		} else {

			user.setUsername(this.selectedElement.getCedula());
			Optional<User> existeUser = userService.buscar(user);
			if (!existeUser.isEmpty()) {
				try {
					userService.actualizarPassword(existeUser.get().getUsername(), passwordDto);
					Mensajes.addMsg(MensajesTipo.INFORMACION, MensajeOk.ACTUALIZADO_OK.toString());

					SecurityContextHolder.clearContext();
					httpServletRequest.getSession().invalidate();
					Utils.redirectToPage("/successpassword.xhtml");
				} catch (Exception ex) {
					Mensajes.addMsg(MensajesTipo.ERROR, ex.getMessage());
				}
			}

			entityService.actualizar(selectedElement);
			Mensajes.addMsg(MensajesTipo.INFORMACION, "Registro actualizado");
		}

		PrimeFaces.current().executeScript("PF('manageElementDialog').hide()");
		PrimeFaces.current().ajax().update("frm:growl", "frm:dt-elements");

	}

	private void resetEntity() {
		selectedElement = new DatosAdmin();
		this.password = "";
		this.repitePassword = "";
	}

	public void onProvinciaChange() {
		if (selectedProvincia != null)
			ciudades = ciudadService.buscarPorProvincia(selectedProvincia);
		else
			ciudades.clear();
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

}
