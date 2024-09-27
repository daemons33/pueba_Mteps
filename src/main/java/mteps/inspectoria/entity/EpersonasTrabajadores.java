package mteps.inspectoria.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EpersonasTrabajadores {
	@Id
	public String id;
	public String cargo;
	public String nroDocumento;
	public String nombreCompleto;
}
