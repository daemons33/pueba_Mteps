package mteps.workflow.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;


@NamedStoredProcedureQuery(name="workflow.p_gestion_workflow",
procedureName = "workflow.p_gestion_workflow",
resultClasses = {GestionWorkflow.class},
parameters = {
		@StoredProcedureParameter(name = "p_json", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "correcto", type = Boolean.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "notificacion", type = String.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "codigoresultado", type = Integer.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "datoadicional", type = Integer.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "correo", type = Boolean.class, mode = ParameterMode.INOUT)
})

@Entity
public class GestionWorkflow {
	@Id
	public Boolean correcto;
	public String notificacion;
	public Integer codigoresultado;
	public Integer datoadicional;
	public Boolean correo;
}
