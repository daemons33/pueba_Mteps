package mteps.config;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConfigEntity {

	
	public Boolean correcto;
	public String notificacion;
	@Id
	public Integer codigoresultado;
	public Integer datoadicional;
	
	
}
