package mteps.tramites.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name="mteps_tramites.p_gestion_tramite",
procedureName = "mteps_tramites.p_gestion_tramite",
resultClasses = {GestionTramite.class},
parameters = {
		@StoredProcedureParameter(name = "p_json", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "correcto", type = Boolean.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "notificacion", type = String.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "codigoresultado", type = Integer.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "datoadicional", type = Integer.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "correo", type = Boolean.class, mode = ParameterMode.INOUT)
})

@Entity
public class GestionTramite {
	@Id
	public Boolean correcto;
	public String notificacion;
	public Integer codigoresultado;
	public Integer datoadicional;
	public Boolean correo;
	
}
