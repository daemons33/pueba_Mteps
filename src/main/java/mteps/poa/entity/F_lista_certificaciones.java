package mteps.poa.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_certificaciones", procedureName = "mteps_plan.f_lista_certificaciones", resultClasses = {
		F_lista_certificaciones.class }, parameters = {
				@StoredProcedureParameter(name = "v_estado", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_login", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_gestion", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_certificaciones {
	
	@Id
	public Integer id_solicitud; 
	public Integer id_proceso; 
	public String codigo; 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
	public Date fecha_solicitud; 
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public String descripcion_procesos; 
	public String estado_solicitud;
	public Integer id_usuario;
	public String observacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
	public Date fecha_cre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
	public Date fecha_aprobacion;
	public String justificacion; 
	public String estado_anterior; 
	public String transaccion; 
	public String estado;
	public String nombreUsuario;
	public String org_unidad_funcional;
	public String org_unidad_funcional2;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
	public Date fecha_revertido;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
	public Date fecha_mod;
	public String nom_usr_aprueba;
	public String nom_usr_revierte;
	public String nom_usr_mod;
	public String cod_manual;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public Object  detalle;
	public Double total;
	
	public static class Detalle{
	    public int id_detalle;
	    public int id_solicitud;
	    public String codigo;
	    public String detalle_descripcion;
	    public double cantidad;
	    public double precio_referencial;
	    public double total_precio_referencial;
	    public double saldo_certificacion;
	    public int id_estado;
	    public int usr_cre;
	    public String host_cre;
	    @Temporal(TemporalType.TIMESTAMP)
		@JsonFormat(shape=JsonFormat.Shape.STRING, timezone="GMT-4")
	    public Date fecha_cre;
	    public Integer usr_mod;
	    public String host_mod;
	    public Date fecha_mod;
	    public String bd_usr;
	    public String bd_host;
	    public Date bd_timestamp;
	    public String partida;
	    public String fuente;
	    public Integer id_partida_presupuesto;
	    public int id_memoria_calculo;
	    public int monto_revertido;
	    public Date fecha_revertido;
	}


	
}
