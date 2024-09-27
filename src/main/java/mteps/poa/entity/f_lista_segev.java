package mteps.poa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_segev", procedureName = "mteps_plan.f_lista_segev", resultClasses = {
		f_lista_segev.class }, parameters = {
				@StoredProcedureParameter(name = "i_gestion", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_estado", type = String.class, mode = ParameterMode.IN)})
@Entity
public class f_lista_segev {
	@Id
	public Integer id_seguimiento;
	public String nombre_seguimiento;
	public String mes_ini;
	public String mes_fin;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_ini;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_fin;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_ini_habilitado;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_fin_habilitado;
	public String estado;
	public Integer id_creado_por;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_creado;
	public Integer id_modificado_por;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_modificado;
	public String nombre_usr_creado;
}
