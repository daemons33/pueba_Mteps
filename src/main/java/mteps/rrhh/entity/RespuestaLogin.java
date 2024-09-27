package mteps.rrhh.entity;

import javax.persistence.Id;

public class RespuestaLogin {
	@Id
	public String usuario;
	public String email;
	public String nombreCompleto;
	public String departamento;
	public String puesto;
	public String token;
}
