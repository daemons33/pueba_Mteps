package mteps.tramites.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name="mteps_tramites.f_buscar_tramite",
procedureName = "mteps_tramites.f_buscar_tramite",
resultClasses = {FBuscarTramite.class},
parameters = {
		@StoredProcedureParameter(name = "v_codigo", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_tramite", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_estado", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_responsable", type = String.class, mode = ParameterMode.IN),		
		@StoredProcedureParameter(name = "v_nit", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_hr", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_fecha_ini", type = Date.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_fecha_fin", type = Date.class, mode = ParameterMode.IN)
})

@Entity
public class FBuscarTramite {
	@Id
	public Integer id_tramite;
	public String codigo_tramite;
	public String tipo_tramite;
	public String estado;
	public String nombre_asignado;
	public String cargo;
	public String unidad_funcional;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_creacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_modificacion;
	public Integer id_empresa;
	public String nit;
	public String hr;

}
