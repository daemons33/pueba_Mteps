package mteps.tramites.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
public class ListaTramites {
	@Id
	public Integer idTramite;
	public String codigoTramite ;
	public Integer idClasificadorTramite;
	public String tramite ;
	public Double montoTotalMulta;
	public String transaccion ;
	public String estado ;
	public String nombreUsuarioCreacion ;
	public Integer usuarioCreacion;
	public String nombreUsuarioModificacion;
	public Integer usuarioModificacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaCreacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaModificacion;
	public Integer idEmpresa ;
	public Integer idPersona;
	public Integer idTipoDocumento; 
	public String nroDocumento;
	public Integer lugarExpedicion;
	public String nombrePersona;
	public String domicilio;
	public String relacionLaboral;
	public String telefono;
	public String correo;
	public String periodoPlanilla ;
	public Integer gestionPlanilla;
	public Boolean minera;
	public Integer diasRetraso;
	public Double montoTotalPlanilla;
	public Integer nroTrabajadores;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaPagoPrima;
	public String detalleFiniquito ;
	public String resolucionAdministrativa ;
	public String relacionEntidad ;
	public Integer nroFojas ;
	public String observacion ;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public List<ListaRequisitos> jsonRequisitos;
	public String nhrTramiteRelacionado;
	public String nurSigec;
	public String nit;
	public String matriculaComercio;
	public Integer idPago;
	public Double costo;
	public String codigoManual;	
	public String razonSocial;	
	public Boolean personaNatural;
	public Integer idTipoDocumentoEmp;
	public String nroDocumentoEmp;
	public Integer lugarExpedicionEmp; 
	public String nombrePersonaEmp;
	public String domicilioEmp;
	public String telefonoEmp;
	public String correoEmp;
	public String codigoVisado;
	
	// DESARROLLO
	
	public Integer idSucursal;
	public Integer nroHojasFoleadas;
	public Integer idResAdministrativa;
	public String citeResolucionAdministrativa;
	public String descripcionSucursal;
	public Integer nroHojasLeg;
	public String documentoLeg;
	public String nroBoletaErr;
	public Date fechaBoletaErr;
	public Double montoErr;
	public String motivoErr;
	public String c21_erroneos;
	public String c31_erroneos;
	public String descResultErr;
	public Boolean devueltoErr;
	public String imputacion;
	public Integer idImputacion;	
	public String nombrePlanillaMulta; 
	public String codigoMulta;
	public Date fechaFinDeclaracion;
	public Double mntTipoCambio;
	public Integer paisVisado;
	public String nombrePaisVisado;
	
	
	public Integer getIdSucursal() {
		return idSucursal;
	}
	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}
	public Integer getNroHojasFoleadas() {
		return nroHojasFoleadas;
	}
	public void setNroHojasFoleadas(Integer nroHojasFoleadas) {
		this.nroHojasFoleadas = nroHojasFoleadas;
	}
	public Integer getIdResAdministrativa() {
		return idResAdministrativa;
	}
	public void setIdResAdministrativa(Integer idResAdministrativa) {
		this.idResAdministrativa = idResAdministrativa;
	}
	public String getCiteResolucionAdministrativa() {
		return citeResolucionAdministrativa;
	}
	public void setCiteResolucionAdministrativa(String citeResolucionAdministrativa) {
		this.citeResolucionAdministrativa = citeResolucionAdministrativa;
	}
	public String getDescripcionSucursal() {
		return descripcionSucursal;
	}
	public void setDescripcionSucursal(String descripcionSucursal) {
		this.descripcionSucursal = descripcionSucursal;
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
	public String getMotivoErr() {
		return motivoErr;
	}
	public void setMotivoErr(String motivoErr) {
		this.motivoErr = motivoErr;
	}
	public String getC21_erroneos() {
		return c21_erroneos;
	}
	public void setC21_erroneos(String c21_erroneos) {
		this.c21_erroneos = c21_erroneos;
	}
	public String getC31_erroneos() {
		return c31_erroneos;
	}
	public void setC31_erroneos(String c31_erroneos) {
		this.c31_erroneos = c31_erroneos;
	}
	public String getDescResultErr() {
		return descResultErr;
	}
	public void setDescResultErr(String descResultErr) {
		this.descResultErr = descResultErr;
	}
	public Boolean getDevueltoErr() {
		return devueltoErr;
	}
	public void setDevueltoErr(Boolean devueltoErr) {
		this.devueltoErr = devueltoErr;
	}
	public String getImputacion() {
		return imputacion;
	}
	public void setImputacion(String imputacion) {
		this.imputacion = imputacion;
	}
	public Integer getIdImputacion() {
		return idImputacion;
	}
	public void setIdImputacion(Integer idImputacion) {
		this.idImputacion = idImputacion;
	}
	public String getNombrePlanillaMulta() {
		return nombrePlanillaMulta;
	}
	public void setNombrePlanillaMulta(String nombrePlanillaMulta) {
		this.nombrePlanillaMulta = nombrePlanillaMulta;
	}
	public String getCodigoMulta() {
		return codigoMulta;
	}
	public void setCodigoMulta(String codigoMulta) {
		this.codigoMulta = codigoMulta;
	}
	public Date getFechaFinDeclaracion() {
		return fechaFinDeclaracion;
	}
	public void setFechaFinDeclaracion(Date fechaFinDeclaracion) {
		this.fechaFinDeclaracion = fechaFinDeclaracion;
	}
	public Double getMntTipoCambio() {
		return mntTipoCambio;
	}
	public void setMntTipoCambio(Double mntTipoCambio) {
		this.mntTipoCambio = mntTipoCambio;
	}
	public Integer getPaisVisado() {
		return paisVisado;
	}
	public void setPaisVisado(Integer paisVisado) {
		this.paisVisado = paisVisado;
	}
	
	public String getCodigoManual() {
		return codigoManual;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public void setCodigoManual(String codigoManual) {
		this.codigoManual = codigoManual;
	}
	public Integer getIdPago() {
		return idPago;
	}
	public void setIdPago(Integer idPago) {
		this.idPago = idPago;
	}
	
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}
	public String getNroDocumento() {
		return nroDocumento;
	}
	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	public Integer getLugarExpedicion() {
		return lugarExpedicion;
	}
	public void setLugarExpedicion(Integer lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getRelacionLaboral() {
		return relacionLaboral;
	}
	public void setRelacionLaboral(String relacionLaboral) {
		this.relacionLaboral = relacionLaboral;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Integer getIdClasificadorTramite() {
		return idClasificadorTramite;
	}
	public void setIdClasificadorTramite(Integer idClasificadorTramite) {
		this.idClasificadorTramite = idClasificadorTramite;
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
	public Boolean getPersonaNatural() {
		return personaNatural;
	}
	public void setPersonaNatural(Boolean personaNatural) {
		this.personaNatural = personaNatural;
	}
	public Integer getIdTipoDocumentoEmp() {
		return idTipoDocumentoEmp;
	}
	public void setIdTipoDocumentoEmp(Integer idTipoDocumentoEmp) {
		this.idTipoDocumentoEmp = idTipoDocumentoEmp;
	}
	public String getNroDocumentoEmp() {
		return nroDocumentoEmp;
	}
	public void setNroDocumentoEmp(String nroDocumentoEmp) {
		this.nroDocumentoEmp = nroDocumentoEmp;
	}
	public Integer getLugarExpedicionEmp() {
		return lugarExpedicionEmp;
	}
	public void setLugarExpedicionEmp(Integer lugarExpedicionEmp) {
		this.lugarExpedicionEmp = lugarExpedicionEmp;
	}
	public String getNombrePersonaEmp() {
		return nombrePersonaEmp;
	}
	public void setNombrePersonaEmp(String nombrePersonaEmp) {
		this.nombrePersonaEmp = nombrePersonaEmp;
	}
	public String getDomicilioEmp() {
		return domicilioEmp;
	}
	public void setDomicilioEmp(String domicilioEmp) {
		this.domicilioEmp = domicilioEmp;
	}
	public String getTelefonoEmp() {
		return telefonoEmp;
	}
	public void setTelefonoEmp(String telefonoEmp) {
		this.telefonoEmp = telefonoEmp;
	}
	public String getCorreoEmp() {
		return correoEmp;
	}
	public void setCorreoEmp(String correoEmp) {
		this.correoEmp = correoEmp;
	}
	public String getCodigoVisado() {
		return codigoVisado;
	}
	public void setCodigoVisado(String codigoVisado) {
		this.codigoVisado = codigoVisado;
	}

	
		
	
}
