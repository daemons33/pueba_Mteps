package mteps.config.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;



@NamedStoredProcedureQuery(name="public.f_obtener_clasificador",
procedureName = "public.f_obtener_clasificador",
resultClasses = {ResultadoClasificadorFinanciero.class},
parameters = {
		@StoredProcedureParameter(name = "p_clasificador", type = String.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultadoClasificadorFinanciero {
	@Id
	public Integer id;
	public Integer codigo;
	public String categoria;
	public String descripcion;
}
