package mteps.tramites.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
@NamedStoredProcedureQuery(name="parametro.f_lista_periodos",
procedureName = "parametro.f_lista_periodos",
resultClasses = {FlistaPeriodos.class})
@Entity
public class FlistaPeriodos {
	@Id
	public Integer id_clasificador;
	public String nombre;
	public Integer orden;
}
