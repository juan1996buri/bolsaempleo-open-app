package ec.edu.insteclrg.domain.candidato;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import ec.edu.insteclrg.domain.User;
import ec.edu.insteclrg.domain.empleador.DatosEmpleador;
import ec.edu.insteclrg.domain.empleador.Oferta;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class OfertaAplicacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private long id;
	@ManyToOne
	@JoinColumn(name = "ofertalaboral_id")
	private Oferta oferta;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;// candidato
	
	@Column
	private LocalDate fechaRegistro;

	
}
