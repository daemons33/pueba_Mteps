package mteps.config.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table( schema = "seguridad",	name = "usuarios")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idusuario;
	private String usuario;
	private String clave;
	
	
	public User() {
	}

	public User(String username, String password) {
		this.usuario = username;
		this.clave = password;
	
	}

	public Integer getIdusuario() {
		return idusuario;
	}

	public void setIdusuario(Integer idusuario) {
		this.idusuario = idusuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}
	

}
