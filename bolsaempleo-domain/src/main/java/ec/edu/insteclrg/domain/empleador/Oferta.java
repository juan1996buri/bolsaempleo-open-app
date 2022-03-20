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
import ec.edu.insteclrg.domain.NivelInstruccion;
import ec.edu.insteclrg.domain.TipoContrato;
import ec.edu.insteclrg.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Oferta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "tipocontrato_id")
	private TipoContrato tipoContrato;
	
    @ManyToOne
	@JoinColumn(name = "ciudad_id")
	private Ciudad ciudad;
	

	@Column
	private LocalDate fechaPublicacion;
	
	@Column(nullable = false) // no obligatorio
	private String remuneracion;
	
	@Column(nullable = false)
	private String tituloOferta;

	@Column(nullable = false)
	private LocalDate fechaLimiteAplicacion;
	
	// descripcion de la plaza
	
	
	/*@Column(nullable = false)
	private String estado;*/
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] banner;
	
	
}
