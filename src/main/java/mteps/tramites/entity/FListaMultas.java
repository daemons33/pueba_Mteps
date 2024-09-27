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

@NamedStoredProcedureQuery(name="mteps_tramites.f_lista_multas",
procedureName = "mteps_tramites.f_lista_multas",
resultClasses = {FListaMultas.class},
parameters = {
		@StoredProcedureParameter(name = "i_login", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class FListaMultas {
	@Id
	public Integer id_multa; 
	public String codigo;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_calculo;
	public Integer id_tipo_planilla;
	public String descripcion;
	public String mes ;
	public String gestion;
	public Integer dias;
	public Double monto;
	public Double multa;
	public Boolean es_minera;
	public String estado;
	public Integer id_empleador;
	public Boolean empresa_externa;
	public String nit;
	public String matricula;
	public String razon_social;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_fin_declaracion;
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_pago_prima;
	public String correo;
	
	public Integer id_tramite; 
	public String codigo_tramite;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_tramite;
	
	public String nombre_unidad;
	public Integer id_mes;
	public Integer id_gestion;
	
	
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public Integer getId_mes() {
		return id_mes;
	}
	public void setId_mes(Integer id_mes) {
		this.id_mes = id_mes;
	}
	public Integer getId_gestion() {
		return id_gestion;
	}
	public void setId_gestion(Integer id_gestion) {
		this.id_gestion = id_gestion;
	}
	public Integer getId_tipo_planilla() {
		return id_tipo_planilla;
	}
	public void setId_tipo_planilla(Integer id_tipo_planilla) {
		this.id_tipo_planilla = id_tipo_planilla;
	}
	public String getNombre_unidad() {
		return nombre_unidad;
	}
	public void setNombre_unidad(String nombre_unidad) {
		this.nombre_unidad = nombre_unidad;
	}
	public Integer getId_tramite() {
		return id_tramite;
	}
	public void setId_tramite(Integer id_tramite) {
		this.id_tramite = id_tramite;
	}
	public String getCodigo_tramite() {
		return codigo_tramite;
	}
	public void setCodigo_tramite(String codigo_tramite) {
		this.codigo_tramite = codigo_tramite;
	}
	public Date getFecha_tramite() {
		return fecha_tramite;
	}
	public void setFecha_tramite(Date fecha_tramite) {
		this.fecha_tramite = fecha_tramite;
	}
	public Date getFecha_pago_prima() {
		return fecha_pago_prima;
	}
	public void setFecha_pago_prima(Date fecha_pago_prima) {
		this.fecha_pago_prima = fecha_pago_prima;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Date getFecha_fin_declaracion() {
		return fecha_fin_declaracion;
	}
	public void setFecha_fin_declaracion(Date fecha_fin_declaracion) {
		this.fecha_fin_declaracion = fecha_fin_declaracion;
	}
	public Integer getId_multa() {
		return id_multa;
	}
	public void setId_multa(Integer id_multa) {
		this.id_multa = id_multa;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Date getFecha_calculo() {
		return fecha_calculo;
	}
	public void setFecha_calculo(Date fecha_calculo) {
		this.fecha_calculo = fecha_calculo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	
	public Integer getDias() {
		return dias;
	}
	public void setDias(Integer dias) {
		this.dias = dias;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Double getMulta() {
		return multa;
	}
	public void setMulta(Double multa) {
		this.multa = multa;
	}
	public Boolean getEs_minera() {
		return es_minera;
	}
	public void setEs_minera(Boolean es_minera) {
		this.es_minera = es_minera;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getId_empleador() {
		return id_empleador;
	}
	public void setId_empleador(Integer id_empleador) {
		this.id_empleador = id_empleador;
	}
	public Boolean getEmpresa_externa() {
		return empresa_externa;
	}
	public void setEmpresa_externa(Boolean empresa_externa) {
		this.empresa_externa = empresa_externa;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getRazon_social() {
		return razon_social;
	}
	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}
	
	
}
