package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "parametro.f_obtener_id_clasificador", procedureName = "parametro.f_obtener_id_clasificador", resultClasses = {
		F_obtener_id_clasificador.class }, parameters = {
				@StoredProcedureParameter(name = "descr", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "grupo", type = String.class, mode = ParameterMode.IN)})
@Entity
public class F_obtener_id_clasificador {
	@Id
	public Integer id;
}
