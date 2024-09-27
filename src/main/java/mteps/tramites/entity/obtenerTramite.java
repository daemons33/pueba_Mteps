package mteps.tramites.entity;

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

@NamedStoredProcedureQuery(name="mteps_tramites.f_obtener_tramite_id",
procedureName = "mteps_tramites.f_obtener_tramite_id",
resultClasses = {obtenerTramite.class},
parameters = {
		@StoredProcedureParameter(name = "id", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
public class obtenerTramite {
	@Id
	public Integer idTramite;
	public String codigoTramite ;
	public Integer idClasificadorTramite;
	public String tramite;
	public Double montoTotalMulta;
	public String transaccion;
	public String estado;
	public String nombreUsuarioCreacion;
	public Integer usuarioCreacion;
	public String nombreUsuarioModificacion;
	public Integer usuarioModificacion;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss")
	public Date fechaCreacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaModificacion;
	public Integer idEmpresa;
	public Integer idPersona;
	public String nombrePersona;
	public String periodoPlanilla;
	public Integer gestionPlanilla;
	public Boolean minera;
	public Integer diasRetraso ;
	public Double montoTotalPlanilla;
	public Integer nroTrabajadores ;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaPagoPrima;
	public String detalleFiniquito;
	public String resolucionAdministrativa;
	public String relacionEntidad;
	public Integer nroFojas ;
	public String observacion;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public List<ListaRequisitos> jsonRequisitos;
	public String fechaDeposito;
	public String nroDeposito;
	public Double montoDeposito ;
	public String estadoDeposito;
	public String nhrTramiteRelacionado;
	public String nurSigec;
	public String nit;
	public String matriculaComercio;
	public String codigoManual;
	public String codigoVisado;
	
	
	public Integer nroHojasLeg;
	public String documentoLeg;
	public Boolean personaNatural;
	public String nroBoletaErr;
	public Date fechaBoletaErr;
	public Double montoErr;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss")
	public Date fechaResAdm;
	public String razonSocial;
	public String descripcionSucursal;
	public String citeRa;
	
	
	
	public String getCiteRa() {
		return citeRa;
	}
	public void setCiteRa(String citeRa) {
		this.citeRa = citeRa;
	}
	public String getDescripcionSucursal() {
		return descripcionSucursal;
	}
	public void setDescripcionSucursal(String descripcionSucursal) {
		this.descripcionSucursal = descripcionSucursal;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public Date getFechaResAdm() {
		return fechaResAdm;
	}
	public void setFechaResAdm(Date fechaResAdm) {
		this.fechaResAdm = fechaResAdm;
	}
	public String getCodigoVisado() {
		return codigoVisado;
	}
	public void setCodigoVisado(String codigoVisado) {
		this.codigoVisado = codigoVisado;
	}
	public String getCodigoManual() {
		return codigoManual;
	}
	public void setCodigoManual(String codigoManual) {
		this.codigoManual = codigoManual;
	}
	public Integer getIdTramite() {
		return idTramite;
	}
	public void setIdTramite(Integer idTramite) {
		this.idTramite = idTramite;
	}
	public String getCodigoTramite() {
		return codigoTramite;
	}
	public void setCodigoTramite(String codigoTramite) {
		this.codigoTramite = codigoTramite;
	}
	public Integer getIdClasificadorTramite() {
		return idClasificadorTramite;
	}
	public void setIdClasificadorTramite(Integer idClasificadorTramite) {
		this.idClasificadorTramite = idClasificadorTramite;
	}
	public String getTramite() {
		return tramite;
	}
	public void setTramite(String tramite) {
		this.tramite = tramite;
	}
	public Double getMontoTotalMulta() {
		return montoTotalMulta;
	}
	public void setMontoTotalMulta(Double montoTotalMulta) {
		this.montoTotalMulta = montoTotalMulta;
	}
	public String getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombreUsuarioCreacion() {
		return nombreUsuarioCreacion;
	}
	public void setNombreUsuarioCreacion(String nombreUsuarioCreacion) {
		this.nombreUsuarioCreacion = nombreUsuarioCreacion;
	}
	public Integer getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(Integer usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public String getNombreUsuarioModificacion() {
		return nombreUsuarioModificacion;
	}
	public void setNombreUsuarioModificacion(String nombreUsuarioModificacion) {
		this.nombreUsuarioModificacion = nombreUsuarioModificacion;
	}
	public Integer getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(Integer usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public Integer getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}
	public String getNombrePersona() {
		return nombrePersona;
	}
	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}
	public String getPeriodoPlanilla() {
		return periodoPlanilla;
	}
	public void setPeriodoPlanilla(String periodoPlanilla) {
		this.periodoPlanilla = periodoPlanilla;
	}
	public Integer getGestionPlanilla() {
		return gestionPlanilla;
	}
	public void setGestionPlanilla(Integer gestionPlanilla) {
		this.gestionPlanilla = gestionPlanilla;
	}
	public Boolean getMinera() {
		return minera;
	}
	public void setMinera(Boolean minera) {
		this.minera = minera;
	}
	public Integer getDiasRetraso() {
		return diasRetraso;
	}
	public void setDiasRetraso(Integer diasRetraso) {
		this.diasRetraso = diasRetraso;
	}
	public Double getMontoTotalPlanilla() {
		return montoTotalPlanilla;
	}
	public void setMontoTotalPlanilla(Double montoTotalPlanilla) {
		this.montoTotalPlanilla = montoTotalPlanilla;
	}
	public Integer getNroTrabajadores() {
		return nroTrabajadores;
	}
	public void setNroTrabajadores(Integer nroTrabajadores) {
		this.nroTrabajadores = nroTrabajadores;
	}
	public Date getFechaPagoPrima() {
		return fechaPagoPrima;
	}
	public void setFechaPagoPrima(Date fechaPagoPrima) {
		this.fechaPagoPrima = fechaPagoPrima;
	}
	public String getDetalleFiniquito() {
		return detalleFiniquito;
	}
	public void setDetalleFiniquito(String detalleFiniquito) {
		this.detalleFiniquito = detalleFiniquito;
	}
	public String getResolucionAdministrativa() {
		return resolucionAdministrativa;
	}
	public void setResolucionAdministrativa(String resolucionAdministrativa) {
		this.resolucionAdministrativa = resolucionAdministrativa;
	}
	public String getRelacionEntidad() {
		return relacionEntidad;
	}
	public void setRelacionEntidad(String relacionEntidad) {
		this.relacionEntidad = relacionEntidad;
	}
	public Integer getNroFojas() {
		return nroFojas;
	}
	public void setNroFojas(Integer nroFojas) {
		this.nroFojas = nroFojas;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public List<ListaRequisitos> getJsonRequisitos() {
		return jsonRequisitos;
	}
	public void setJsonRequisitos(List<ListaRequisitos> jsonRequisitos) {
		this.jsonRequisitos = jsonRequisitos;
	}

	public String getFechaDeposito() {
		return fechaDeposito;
	}
	public void setFechaDeposito(String fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}
	public String getNroDeposito() {
		return nroDeposito;
	}
	public void setNroDeposito(String nroDeposito) {
		this.nroDeposito = nroDeposito;
	}
	public Double getMontoDeposito() {
		return montoDeposito;
	}
	public void setMontoDeposito(Double montoDeposito) {
		this.montoDeposito = montoDeposito;
	}
	public String getEstadoDeposito() {
		return estadoDeposito;
	}
	public void setEstadoDeposito(String estadoDeposito) {
		this.estadoDeposito = estadoDeposito;
	}
	public String getNhrTramiteRelacionado() {
		return nhrTramiteRelacionado;
	}
	public void setNhrTramiteRelacionado(String nhrTramiteRelacionado) {
		this.nhrTramiteRelacionado = nhrTramiteRelacionado;
	}
	public String getNurSigec() {
		return nurSigec;
	}
	public void setNurSigec(String nurSigec) {
		this.nurSigec = nurSigec;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getMatriculaComercio() {
		return matriculaComercio;
	}
	public void setMatriculaComercio(String matriculaComercio) {
		this.matriculaComercio = matriculaComercio;
	}
	
	
	public Integer getNroHojasLeg() {
		return nroHojasLeg;
	}
	public void setNroHojasLeg(Integer nroHojasLeg) {
		this.nroHojasLeg = nroHojasLeg;
	}
	public String getDocumentoLeg() {
		return documentoLeg;
	}
	public void setDocumentoLeg(String documentoLeg) {
		this.documentoLeg = documentoLeg;
	}
	public Boolean getPersonaNatural() {
		return personaNatural;
	}
	public void setPersonaNatural(Boolean personaNatural) {
		this.personaNatural = personaNatural;
	}
	public String getNroBoletaErr() {
		return nroBoletaErr;
	}
	public void setNroBoletaErr(String nroBoletaErr) {
		this.nroBoletaErr = nroBoletaErr;
	}
	public Date getFechaBoletaErr() {
		return fechaBoletaErr;
	}
	public void setFechaBoletaErr(Date fechaBoletaErr) {
		this.fechaBoletaErr = fechaBoletaErr;
	}
	public Double getMontoErr() {
		return montoErr;
	}
	public void setMontoErr(Double montoErr) {
		this.montoErr = montoErr;
	}
		
	
	
}
