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

@NamedStoredProcedureQuery(name = "mteps_plan.f_busqueda_certificacion", procedureName = "mteps_plan.f_busqueda_certificacion", resultClasses = {
		F_busqueda_certificacion.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_plan", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_partida", type = String.class, mode = ParameterMode.IN)})
@Entity
public class F_busqueda_certificacion {
	
	public Integer id_plan;
	public String sigla;
	public String descripcion;
	
	public Integer id_solicitud;
	public Integer id_proceso;
	public String codigo;
	public String fecha_solicitud;
	public String estado_solicitud;
	public Integer id_estado;
	public Integer usr_cre;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_cre;
	public Integer usr_mod;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_mod;
	public Integer id_usuario;
	public String observacion;
	public String fecha_aprobacion;
	public String justificacion;
	public Integer id_memoria_calculo;
	public String partida;
	public String fuente;
	public Double total_precio_referencial;
	public Double monto_revertido;
	public Double monto_valido;
	@Id
	public Integer id_detalle;
}
