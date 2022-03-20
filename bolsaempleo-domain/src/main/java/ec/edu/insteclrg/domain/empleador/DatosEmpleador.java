package ec.edu.insteclrg.domain.empleador;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import ec.edu.insteclrg.domain.Ciudad;
import ec.edu.insteclrg.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class DatosEmpleador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private String ruc;

	@Column(nullable = false)
	private String nombreEmpresa;

	@Column(nullable = false)
	private String direccionEmpresa;

	@Column(nullable = false)
	private String telefonoEmpresa;

	@Column(nullable = false)
	private String emailEmpresa;

	@ManyToOne
	@JoinColumn(name = "ciudad_id")
	private Ciudad ciudad;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] logo;

	@Column(nullable = false)
	private String nombreRepresentante;

	@Column(nullable = false)
	private String apellidoRepresentante;

	@Column
	private String telefonoRepresentante;

	@Column(nullable = false)
	private String emailRepresentante;
	@Column
	private LocalDate fechaRegistro;
}
