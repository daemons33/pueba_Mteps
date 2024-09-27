package mteps.rrhh.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;


import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name = "mteps_rrhh.f_obtener_unidades_organizacionales", procedureName = "mteps_rrhh.f_obtener_unidades_organizacionales", resultClasses = {
		FuncionObtenerUnidades.class })
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionObtenerUnidades {
	@Id
	public Integer idUnidad;
	public String unidadFuncional;
	public Integer idUnidadDepende;
}
