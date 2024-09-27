package mteps.planpago.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name="workflow.f_obtener_json_menu_por_usuario",
procedureName = "workflow.f_obtener_json_menu_por_usuario",
parameters = {
		@StoredProcedureParameter(name = "p_login", type = String.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class obtenerJsonMenuPorUsuario {
@Id
public Integer Id;
}
