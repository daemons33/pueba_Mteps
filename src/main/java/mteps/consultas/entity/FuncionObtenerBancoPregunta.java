package mteps.consultas.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_banco", procedureName = "mteps_denuncias.f_obtener_banco", resultClasses = {
		FuncionObtenerBancoPregunta.class }, parameters = {
				@StoredProcedureParameter(name = "v_dato", type = String.class, mode = ParameterMode.IN)})
@Entity
public class FuncionObtenerBancoPregunta {
	@Id
	public Integer id;
	public String pregunta;
}
