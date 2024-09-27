package mteps.inspectoria.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class EntDocumentoSigec {
	@Id
	public Integer id;
	public String cite;
	public String hr;
	public String nombreRemitente;
	public String cargoRemitente;
	public String nombreVia;
	public String cargoVia;
	public String nombreDestinatario;
	public String cargoDestinatario;
	public String mosca;
	public String referencia;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone="GMT-4")
	public Date fechaCreacionSigec  ;
		
}
