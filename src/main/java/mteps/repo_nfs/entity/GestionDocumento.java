package mteps.repo_nfs.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name="mteps_nfs.p_gestion_repositorio_doc",
procedureName = "mteps_nfs.p_gestion_repositorio_doc",
resultClasses = {GestionDocumento.class},
parameters = {
		@StoredProcedureParameter(name = "p_json", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "correcto", type = Boolean.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "notificacion", type = String.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "codigoresultado", type = Integer.class, mode = ParameterMode.INOUT),
		@StoredProcedureParameter(name = "datoadicional", type = Integer.class, mode = ParameterMode.INOUT)
})

@Entity
public class GestionDocumento {
	@Id
	public Boolean correcto;
	public String notificacion;
	public Integer codigoresultado;
	public Integer datoadicional;
}