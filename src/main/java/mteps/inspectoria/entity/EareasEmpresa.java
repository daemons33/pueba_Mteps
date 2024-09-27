package mteps.inspectoria.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EareasEmpresa {
	@Id
	public String id;
	public String area;
	public Boolean paralizacion;
}
