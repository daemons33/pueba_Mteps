package mteps.ovt.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class EntidadEmpresa {
	@Id
	public Integer idEmpresa;
	public String nit;
	public String codigoMteps;
	public String matriculaComercio;
	public String estado;
	public String razonSocial;
	public String nombreComercial;
	public String nroPatronal;
	public String departamento;
	public String direccion;
	public String representanteLegal;
	public String tipoDocumento;
	public String nroDocumento;
	public String descActividadDeclarada;
	public String telefono;
	public String correoElectronico;
	

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaConstitucion;
	public String sindicato;
	public String comiteMixto;
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaInicioActividad;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaInscripcion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaDeclaracion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaLimiteActualizacion;
	public String estadoActualizacionRoe;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaIniDdjjPlanillas;
	public String paisOrigen;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaHabilitacionImpuestos;
	public Boolean sinTrabajadores;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaHabilitacionPlanillas;
	public Boolean nitInactivo;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaSincronizacion;
	public String tipoSociedad;
	public String actividad;
	public String categoriaLucro;
	public String tipoEmpresa;
	public Boolean empresaExterna;
	public Boolean getEmpresaExterna() {
		return empresaExterna;
	}
	public void setEmpresaExterna(Boolean empresaExterna) {
		this.empresaExterna = empresaExterna;
	}
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getCodigoMteps() {
		return codigoMteps;
	}
	public void setCodigoMteps(String codigoMteps) {
		this.codigoMteps = codigoMteps;
	}
	public String getMatriculaComercio() {
		return matriculaComercio;
	}
	public void setMatriculaComercio(String matriculaComercio) {
		this.matriculaComercio = matriculaComercio;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getNombreComercial() {
		return nombreComercial;
	}
	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public String getNroPatronal() {
		return nroPatronal;
	}
	public void setNroPatronal(String nroPatronal) {
		this.nroPatronal = nroPatronal;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getRepresentanteLegal() {
		return representanteLegal;
	}
	public void setRepresentanteLegal(String representanteLegal) {
		this.representanteLegal = representanteLegal;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getNroDocumento() {
		return nroDocumento;
	}
	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	public String getDescActividadDeclarada() {
		return descActividadDeclarada;
	}
	public void setDescActividadDeclarada(String descActividadDeclarada) {
		this.descActividadDeclarada = descActividadDeclarada;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public Date getFechaConstitucion() {
		return fechaConstitucion;
	}
	public void setFechaConstitucion(Date fechaConstitucion) {
		this.fechaConstitucion = fechaConstitucion;
	}
	public String getSindicato() {
		return sindicato;
	}
	public void setSindicato(String sindicato) {
		this.sindicato = sindicato;
	}
	public String getComiteMixto() {
		return comiteMixto;
	}
	public void setComiteMixto(String comiteMixto) {
		this.comiteMixto = comiteMixto;
	}
	public Date getFechaInicioActividad() {
		return fechaInicioActividad;
	}
	public void setFechaInicioActividad(Date fechaInicioActividad) {
		this.fechaInicioActividad = fechaInicioActividad;
	}
	public Date getFechaInscripcion() {
		return fechaInscripcion;
	}
	public void setFechaInscripcion(Date fechaInscripcion) {
		this.fechaInscripcion = fechaInscripcion;
	}
	public Date getFechaDeclaracion() {
		return fechaDeclaracion;
	}
	public void setFechaDeclaracion(Date fechaDeclaracion) {
		this.fechaDeclaracion = fechaDeclaracion;
	}
	public Date getFechaLimiteActualizacion() {
		return fechaLimiteActualizacion;
	}
	public void setFechaLimiteActualizacion(Date fechaLimiteActualizacion) {
		this.fechaLimiteActualizacion = fechaLimiteActualizacion;
	}
	public String getEstadoActualizacionRoe() {
		return estadoActualizacionRoe;
	}
	public void setEstadoActualizacionRoe(String estadoActualizacionRoe) {
		this.estadoActualizacionRoe = estadoActualizacionRoe;
	}
	public Date getFechaIniDdjjPlanillas() {
		return fechaIniDdjjPlanillas;
	}
	public void setFechaIniDdjjPlanillas(Date fechaIniDdjjPlanillas) {
		this.fechaIniDdjjPlanillas = fechaIniDdjjPlanillas;
	}
	public String getPaisOrigen() {
		return paisOrigen;
	}
	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}
	public Date getFechaHabilitacionImpuestos() {
		return fechaHabilitacionImpuestos;
	}
	public void setFechaHabilitacionImpuestos(Date fechaHabilitacionImpuestos) {
		this.fechaHabilitacionImpuestos = fechaHabilitacionImpuestos;
	}
	public Boolean getSinTrabajadores() {
		return sinTrabajadores;
	}
	public void setSinTrabajadores(Boolean sinTrabajadores) {
		this.sinTrabajadores = sinTrabajadores;
	}
	public Date getFechaHabilitacionPlanillas() {
		return fechaHabilitacionPlanillas;
	}
	public void setFechaHabilitacionPlanillas(Date fechaHabilitacionPlanillas) {
		this.fechaHabilitacionPlanillas = fechaHabilitacionPlanillas;
	}
	public Boolean getNitInactivo() {
		return nitInactivo;
	}
	public void setNitInactivo(Boolean nitInactivo) {
		this.nitInactivo = nitInactivo;
	}
	public Date getFechaSincronizacion() {
		return fechaSincronizacion;
	}
	public void setFechaSincronizacion(Date fechaSincronizacion) {
		this.fechaSincronizacion = fechaSincronizacion;
	}
	public String getTipoSociedad() {
		return tipoSociedad;
	}
	public void setTipoSociedad(String tipoSociedad) {
		this.tipoSociedad = tipoSociedad;
	}
	public String getActividad() {
		return actividad;
	}
	public void setActividad(String actividad) {
		this.actividad = actividad;
	}
	public String getCategoriaLucro() {
		return categoriaLucro;
	}
	public void setCategoriaLucro(String categoriaLucro) {
		this.categoriaLucro = categoriaLucro;
	}
	public String getTipoEmpresa() {
		return tipoEmpresa;
	}
	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}



}
