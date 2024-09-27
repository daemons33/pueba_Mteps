package mteps.poa.entity;

import java.util.ArrayList;
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

@NamedStoredProcedureQuery(name = "mteps_plan.f_reporte_certificacion_poa", procedureName = "mteps_plan.f_reporte_certificacion_poa", resultClasses = {
		F_reporte_certificacion_poa.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_solicitud", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_reporte_certificacion_poa {
	@Id
	public Integer id_solicitud;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public Solicitud solicitud;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public Formulacion formulacion;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Detalle_solicitud> detalle_solicitud;
	
	public static class DescripcionProceso{
	    public int apertura;
	    public int accionCotroPlazo;
	    public int operacion;
	    public int actividad;
	    public int tarea;
	    public int programa;
	}
	public static class Tarea{
	    public Object id_accion_poa_ape;
	    public int id_apertura_organizacion;
	    public int id_plan;
	    public int id_proceso_superior;
	    public int id_proceso;
	    public Object id_apertura;
	    public Object id_estado;
	    public String sigla;
	    public String nombre;
	    public String descripcion;
	}




	public static class Solicitud{
	    public int id_solicitud;
	    public int id_proceso;
	    public String codigo;
		@Temporal(TemporalType.TIMESTAMP)
		@JsonFormat(shape=JsonFormat.Shape.STRING, timezone="GMT-4")
	    public Date fecha_solicitud;
	    public ArrayList<Object> descripcion_procesos;
	    public String estado_solicitud;
	    public int id_estado;
	    public Integer usr_cre;
	    public String host_cre;
		@Temporal(TemporalType.TIMESTAMP)
		@JsonFormat(shape=JsonFormat.Shape.STRING, timezone="GMT-4")
	    public Date fecha_cre;
	    public String usr_mod;
	    public String host_mod;
	    public Date fecha_mod;
	    public String bd_usr;
	    public String bd_host;
	    public Date bd_timestamp;
	    public int id_usuario;
	    public String observacion;
		@Temporal(TemporalType.TIMESTAMP)
		@JsonFormat(shape=JsonFormat.Shape.STRING,timezone="GMT-4")
	    public Date fecha_aprobacion;
	    public String justificacion;
	    public String estado_anterior;
	    public String transaccion;
	    public String estado;
	    public Integer usr_firma;
	    @Temporal(TemporalType.TIMESTAMP)
		@JsonFormat(shape=JsonFormat.Shape.STRING,timezone="GMT-4")
	    public Date fecha_revertido;
	    public Integer id_usuario_revierte;
	    public Integer id_usuario_aprueba;
	    public String cod_cert_manual;
	}
	
	public static class Formulacion{
	    public String actividad_presupuestaria;
	    public String programa_presupuestario;
	    public String acp_sigla;
	    public String acp;
	    public String op_sigla;
	    public String op;
	    public String act_sigla;
	    public String act;
	    public String te_sigla;
	    public String te;
		public String getActividad_presupuestaria() {
			return actividad_presupuestaria;
		}
		public void setActividad_presupuestaria(String actividad_presupuestaria) {
			this.actividad_presupuestaria = actividad_presupuestaria;
		}
		public String getPrograma_presupuestario() {
			return programa_presupuestario;
		}
		public void setPrograma_presupuestario(String programa_presupuestario) {
			this.programa_presupuestario = programa_presupuestario;
		}
		public String getAcp_sigla() {
			return acp_sigla;
		}
		public void setAcp_sigla(String acp_sigla) {
			this.acp_sigla = acp_sigla;
		}
		public String getAcp() {
			return acp;
		}
		public void setAcp(String acp) {
			this.acp = acp;
		}
		public String getOp_sigla() {
			return op_sigla;
		}
		public void setOp_sigla(String op_sigla) {
			this.op_sigla = op_sigla;
		}
		public String getOp() {
			return op;
		}
		public void setOp(String op) {
			this.op = op;
		}
		public String getAct_sigla() {
			return act_sigla;
		}
		public void setAct_sigla(String act_sigla) {
			this.act_sigla = act_sigla;
		}
		public String getAct() {
			return act;
		}
		public void setAct(String act) {
			this.act = act;
		}
		public String getTe_sigla() {
			return te_sigla;
		}
		public void setTe_sigla(String te_sigla) {
			this.te_sigla = te_sigla;
		}
		public String getTe() {
			return te;
		}
		public void setTe(String te) {
			this.te = te;
		}
	    
	}
	
	public static class Detalle_solicitud{
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
	    public Date fecha_cre;
	    public Object usr_mod;
	    public Object host_mod;
	    public Object fecha_mod;
	    public String bd_usr;
	    public String bd_host;
	    public Date bd_timestamp;
	    public String partida;
	    public String fuente;
	    public Object id_partida_presupuesto;
	    public int id_memoria_calculo;
	    public int monto_revertido;
	    public Date fecha_revertido;
	    public String sigla;
	    
	    
	    
	    
	    
	    
		public int getMonto_revertido() {
			return monto_revertido;
		}
		public void setMonto_revertido(int monto_revertido) {
			this.monto_revertido = monto_revertido;
		}
		public Date getFecha_revertido() {
			return fecha_revertido;
		}
		public void setFecha_revertido(Date fecha_revertido) {
			this.fecha_revertido = fecha_revertido;
		}
		public String getSigla() {
			return sigla;
		}
		public void setSigla(String sigla) {
			this.sigla = sigla;
		}
		public int getId_detalle() {
			return id_detalle;
		}
		public void setId_detalle(int id_detalle) {
			this.id_detalle = id_detalle;
		}
		public int getId_solicitud() {
			return id_solicitud;
		}
		public void setId_solicitud(int id_solicitud) {
			this.id_solicitud = id_solicitud;
		}
		public String getCodigo() {
			return codigo;
		}
		
		public Object getId_partida_presupuesto() {
			return id_partida_presupuesto;
		}
		public void setId_partida_presupuesto(Object id_partida_presupuesto) {
			this.id_partida_presupuesto = id_partida_presupuesto;
		}
		public void setId_memoria_calculo(int id_memoria_calculo) {
			this.id_memoria_calculo = id_memoria_calculo;
		}
		public Integer getId_memoria_calculo() {
			return id_memoria_calculo;
		}
		public void setId_memoria_calculo(Integer id_memoria_calculo) {
			this.id_memoria_calculo = id_memoria_calculo;
		}
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public String getDetalle_descripcion() {
			return detalle_descripcion;
		}
		public void setDetalle_descripcion(String detalle_descripcion) {
			this.detalle_descripcion = detalle_descripcion;
		}
		
		public double getCantidad() {
			return cantidad;
		}
		public void setCantidad(double cantidad) {
			this.cantidad = cantidad;
		}
		public double getPrecio_referencial() {
			return precio_referencial;
		}
		public void setPrecio_referencial(double precio_referencial) {
			this.precio_referencial = precio_referencial;
		}
		public double getTotal_precio_referencial() {
			return total_precio_referencial;
		}
		public void setTotal_precio_referencial(double total_precio_referencial) {
			this.total_precio_referencial = total_precio_referencial;
		}
		public double getSaldo_certificacion() {
			return saldo_certificacion;
		}
		public void setSaldo_certificacion(double saldo_certificacion) {
			this.saldo_certificacion = saldo_certificacion;
		}
		public int getId_estado() {
			return id_estado;
		}
		public void setId_estado(int id_estado) {
			this.id_estado = id_estado;
		}
		public int getUsr_cre() {
			return usr_cre;
		}
		public void setUsr_cre(int usr_cre) {
			this.usr_cre = usr_cre;
		}
		public String getHost_cre() {
			return host_cre;
		}
		public void setHost_cre(String host_cre) {
			this.host_cre = host_cre;
		}
		public Date getFecha_cre() {
			return fecha_cre;
		}
		public void setFecha_cre(Date fecha_cre) {
			this.fecha_cre = fecha_cre;
		}
		public Object getUsr_mod() {
			return usr_mod;
		}
		public void setUsr_mod(Object usr_mod) {
			this.usr_mod = usr_mod;
		}
		public Object getHost_mod() {
			return host_mod;
		}
		public void setHost_mod(Object host_mod) {
			this.host_mod = host_mod;
		}
		public Object getFecha_mod() {
			return fecha_mod;
		}
		public void setFecha_mod(Object fecha_mod) {
			this.fecha_mod = fecha_mod;
		}
		public String getBd_usr() {
			return bd_usr;
		}
		public void setBd_usr(String bd_usr) {
			this.bd_usr = bd_usr;
		}
		public String getBd_host() {
			return bd_host;
		}
		public void setBd_host(String bd_host) {
			this.bd_host = bd_host;
		}
		public Date getBd_timestamp() {
			return bd_timestamp;
		}
		public void setBd_timestamp(Date bd_timestamp) {
			this.bd_timestamp = bd_timestamp;
		}
		public String getPartida() {
			return partida;
		}
		public void setPartida(String partida) {
			this.partida = partida;
		}
		public String getFuente() {
			return fuente;
		}
		public void setFuente(String fuente) {
			this.fuente = fuente;
		}
	    
	    
	}

	
	
}
