package mteps.tramites.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
@NamedStoredProcedureQuery(name="parametro.f_lista_clasificador",
procedureName = "parametro.f_lista_clasificador",
resultClasses = {FlistaPeriodoMes.class},
parameters = {
		@StoredProcedureParameter(name = "i_dominio", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "i_descripcion", type = String.class, mode = ParameterMode.IN)
})
@Entity
public class FlistaPeriodoMes {
	
	@Id
	public Integer id_clasificador;
	public String nombre;
	public Integer orden;
}
