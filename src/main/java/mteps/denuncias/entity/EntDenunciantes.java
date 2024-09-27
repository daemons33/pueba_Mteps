package mteps.denuncias.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntDenunciantes {
	
	@Id
	public String id;
	public String correo;
	public String telefono;
	public String domicilio;
	public String nroDocumento; 
	public String nombreCompleto;
	public String genero;
	public Integer edad;
	public String relacion;
}
