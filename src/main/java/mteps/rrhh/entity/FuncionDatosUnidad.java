package mteps.rrhh.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name = "mteps_rrhh.f_obtener_unidad_organizacional", procedureName = "mteps_rrhh.f_obtener_unidad_organizacional", resultClasses = {
		FuncionDatosUnidad.class }, parameters = {
				@StoredProcedureParameter(name = "v_id", type = Integer.class, mode = ParameterMode.IN)})
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionDatosUnidad {
	@Id
	public Integer idUnidad;
	public String unidadFuncional;
	public Integer idUnidadDepende;
}
