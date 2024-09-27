package mteps.tramites.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ListaRequisitos {
	@Id
	public Integer idRequisito;
	public Boolean value;
}
