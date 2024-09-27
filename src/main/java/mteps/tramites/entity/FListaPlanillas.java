package mteps.tramites.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;

@NamedStoredProcedureQuery(name="parametro.f_lista_planillas",
procedureName = "parametro.f_lista_planillas",
resultClasses = {FListaPlanillas.class})
@Entity
public class FListaPlanillas {
	@Id
	public Integer id_clasificador;
	public String nombre;

	
}
