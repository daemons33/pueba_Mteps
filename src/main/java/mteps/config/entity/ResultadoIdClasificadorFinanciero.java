package mteps.config.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "public.f_obtener_id_clasificador", procedureName = "public.f_obtener_id_clasificador", resultClasses = {
		ResultadoIdClasificadorFinanciero.class }, parameters = {
				@StoredProcedureParameter(name = "v_id", type = Integer.class, mode = ParameterMode.IN)})
@Entity

public class ResultadoIdClasificadorFinanciero {
	@Id
	public Integer id;
	public Integer codigo;
	public String categoria;
	public String descripcion;
}
