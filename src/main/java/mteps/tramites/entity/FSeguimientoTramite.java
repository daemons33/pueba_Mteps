package mteps.tramites.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name="mteps_tramites.f_seguimiento_tramite",
procedureName = "mteps_tramites.f_seguimiento_tramite",
resultClasses = {FSeguimientoTramite.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_tramite", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
public class FSeguimientoTramite {
	
	 @Id
	 public Integer id;
	 public String sistema;
	 public String codigo;
	 public String nombre_emisor;
	 public String cargo_emisor;
	 public String de_oficina;
	 public String nombre_receptor;
	 public String cargo_receptor;
	 public String a_oficina;
	 public Timestamp fecha_emision;
	 public Timestamp fecha_recepcion;
	 public String adjuntos;
	 public String accion;
	 public String estado;
	 public String proveido;
	 public String hr;
	 public String getFechaEmisionFormateada() {
		    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy - HH:mm:ss");
		    return this.fecha_emision!=null?Character.toUpperCase(sdf.format(this.fecha_emision).charAt(0)) + sdf.format(this.fecha_emision).substring(1):"" ;
		  }
     public String getFechaRecepcionFormateada() {
		    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy - HH:mm:ss");
		    return this.fecha_recepcion!=null?Character.toUpperCase(sdf.format(this.fecha_recepcion).charAt(0)) + sdf.format(this.fecha_recepcion).substring(1):"";
		  }
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre_emisor() {
		return nombre_emisor;
	}
	public void setNombre_emisor(String nombre_emisor) {
		this.nombre_emisor = nombre_emisor;
	}
	public String getCargo_emisor() {
		return cargo_emisor;
	}
	public void setCargo_emisor(String cargo_emisor) {
		this.cargo_emisor = cargo_emisor;
	}
	public String getDe_oficina() {
		return de_oficina;
	}
	public void setDe_oficina(String de_oficina) {
		this.de_oficina = de_oficina;
	}
	public String getNombre_receptor() {
		return nombre_receptor;
	}
	public void setNombre_receptor(String nombre_receptor) {
		this.nombre_receptor = nombre_receptor;
	}
	public String getCargo_receptor() {
		return cargo_receptor;
	}
	public void setCargo_receptor(String cargo_receptor) {
		this.cargo_receptor = cargo_receptor;
	}
	public String getA_oficina() {
		return a_oficina;
	}
	public void setA_oficina(String a_oficina) {
		this.a_oficina = a_oficina;
	}
	public Timestamp getFecha_emision() {
		return fecha_emision;
	}
	public void setFecha_emision(Timestamp fecha_emision) {
		this.fecha_emision = fecha_emision;
	}
	public Timestamp getFecha_recepcion() {
		return fecha_recepcion;
	}
	public void setFecha_recepcion(Timestamp fecha_recepcion) {
		this.fecha_recepcion = fecha_recepcion;
	}
	public String getAdjuntos() {
		return adjuntos;
	}
	public void setAdjuntos(String adjuntos) {
		this.adjuntos = adjuntos;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getProveido() {
		return proveido;
	}
	public void setProveido(String proveido) {
		this.proveido = proveido;
	}
	
	
	public String getHr() {
		return hr;
	}
	public void setHr(String hr) {
		this.hr = hr;
	}
	 
	 
}
