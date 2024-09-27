package mteps.inspectoria.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntDatosGenerarInforme {
	@Id
	public Integer idInspeccion;
	public String login;
	public Integer idUsuarioSigecVia;
	public Integer idUsuarioSigecDestinatario;
	public String referencia;
	public Integer fojas;
	public Boolean hojaRuta;
	public String proveido;
	
}
