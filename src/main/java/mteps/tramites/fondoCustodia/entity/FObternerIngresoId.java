package mteps.tramites.fondoCustodia.entity;

import java.time.LocalDateTime;
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

@NamedStoredProcedureQuery(name="mteps_tramites.f_obtener_fc_ingreso_id",
procedureName = "mteps_tramites.f_obtener_fc_ingreso_id",
resultClasses = {FObternerIngresoId.class},
parameters = {
		@StoredProcedureParameter(name = "v_id", type = Integer.class, mode = ParameterMode.IN)
})

@Entity
public class FObternerIngresoId {
	
	@Id
	public Integer  idFc;
	public Integer  idCausal;
	public String  causal;
	public String  estado;
	public String  transaccion;
	public Integer  usuario_creacion;
	public Integer  usuario_modificacion;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaCreacion;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaModificacion;
	public Integer  idEmpresa;
	public String  nitEmpresa;
	public String  razonSocial;
	public String  matriculaComercio;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaAutorizado;
	public Integer  idTrabajador;	
	public Double montoFc;
	public String  codigoCpt;
	public Integer  idCpt;
	public Integer  idDepositoCta;
	public Integer idTipoContrato;
	public String  tipoContrato;
	public Integer  idTramiteFc;
	public Integer  idTramiteFiniquito;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaFallecimiento;
	public Integer  tipoFallecimiento;
	public String  nroRaAsistencia;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaRaAsistencia;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaAbandonoInicial;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaAbandonoFinal;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaRenuncia;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaMemoDespido;
	public Integer  causaDespido;
	public Date fechaAcuerdoPago;
	public Date fechaEntregaFc;
	public String  jefatura;
	public String  codigoFc;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaDesvinculacion;
	public String nombreUsuarioCreacion;
	public String nombreUsuarioModificacion;
	public String observacion;
	public String documentoDeposito;
	
	public Integer idServicioFc;
	public String codServicioFc;
	public Integer idServicioFiniquito;
	public String codServicioFiniquito;
	public String periodoPlanilla;
	public Integer gestionPlanilla;
	public String docTemporalidad;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Persona persona;
	public String cpt;
	public String codigoOrden;
	public String codigoTransaccion;
	public String urlRedireccion;
	public Integer idPagoFc;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Object repositorio;
	public String nombreTramitador;
	public String docTramitador;
	public String telefonoTramitador;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<ObjectMov> movimientosIngresos;
	public Double montoFiniquito;
	
	public Integer idCyt;
    public String codigocyt;
    public String nombreapoderado;
    public Integer tipodocapoderado;
    public String nrodocapoderado;
    public String testimonioapoderado;
    public String nombreherederos;
    public Integer tipodocheredero;
    public String nrodocheredero;
    public Date fechaacuerdopago2;
    public Date fecharecibidoart22;
    public Date fechareincorporacion;
    public String informeinspectorreinc;
    public Date fechainformereinc;
    public String ctadevolucion;
    public Integer entidadfinanciera;
    public String autoridadoj;
    public String cargooj;
    public String juzgadooj;
    public String documentooj;
    public Date fechaoj;
    public Date fechaInicioSalida;
    public Integer idTipoSalida;
    public Integer idTipoDesembolso;
    public Integer idTipoDevolucion;
    public Integer idCheque;
    public String hojaRuta;
    public String nombreTipoSalida;
    public String nombreTipoDesembolso;
    public String nroCheque;
    public Double montoCheque;
    public Date fechaEmitido;
    public String nombreBeneficiario;
	
	public static class ObjectMov{
		
		
			public Integer id_movimiento;
		    public Integer id_estado;
		    public Integer id_fc;
		    public Double monto;
		    public Boolean uninet;
		    public String nro_movimiento;
		    public Date fecha_movimiento;
		    public String detalle_uninet;
		    public Integer movimiento;
		    public Boolean sigep;
		    public String operacion_sigep;
		    public String detalle_sigep;
		    public String cuenta_transferencia_sigep;
		    public Integer id_cuenta_bancaria;
		    public Date fecha_creacion;
		    public Date fecha_modificacion;
		    public Integer usuario_creacion;
		    public Integer usuario_modificacion;
		    public String transaccion;
		    public String estado;
		    public String agencia;
		    public Double saldo;
			public Integer getId_movimiento() {
				return id_movimiento;
			}
			public void setId_movimiento(Integer id_movimiento) {
				this.id_movimiento = id_movimiento;
			}
			public Integer getId_estado() {
				return id_estado;
			}
			public void setId_estado(Integer id_estado) {
				this.id_estado = id_estado;
			}
			public Integer getId_fc() {
				return id_fc;
			}
			public void setId_fc(Integer id_fc) {
				this.id_fc = id_fc;
			}
			public Double getMonto() {
				return monto;
			}
			public void setMonto(Double monto) {
				this.monto = monto;
			}
			public Boolean getUninet() {
				return uninet;
			}
			public void setUninet(Boolean uninet) {
				this.uninet = uninet;
			}
			public String getNro_movimiento() {
				return nro_movimiento;
			}
			public void setNro_movimiento(String nro_movimiento) {
				this.nro_movimiento = nro_movimiento;
			}
			public Date getFecha_movimiento() {
				return fecha_movimiento;
			}
			public void setFecha_movimiento(Date fecha_movimiento) {
				this.fecha_movimiento = fecha_movimiento;
			}
			public String getDetalle_uninet() {
				return detalle_uninet;
			}
			public void setDetalle_uninet(String detalle_uninet) {
				this.detalle_uninet = detalle_uninet;
			}
			public Integer getMovimiento() {
				return movimiento;
			}
			public void setMovimiento(Integer movimiento) {
				this.movimiento = movimiento;
			}
			public Boolean getSigep() {
				return sigep;
			}
			public void setSigep(Boolean sigep) {
				this.sigep = sigep;
			}
			public String getOperacion_sigep() {
				return operacion_sigep;
			}
			public void setOperacion_sigep(String operacion_sigep) {
				this.operacion_sigep = operacion_sigep;
			}
			public String getDetalle_sigep() {
				return detalle_sigep;
			}
			public void setDetalle_sigep(String detalle_sigep) {
				this.detalle_sigep = detalle_sigep;
			}
			public String getCuenta_transferencia_sigep() {
				return cuenta_transferencia_sigep;
			}
			public void setCuenta_transferencia_sigep(String cuenta_transferencia_sigep) {
				this.cuenta_transferencia_sigep = cuenta_transferencia_sigep;
			}
			public Integer getId_cuenta_bancaria() {
				return id_cuenta_bancaria;
			}
			public void setId_cuenta_bancaria(Integer id_cuenta_bancaria) {
				this.id_cuenta_bancaria = id_cuenta_bancaria;
			}
			public Date getFecha_creacion() {
				return fecha_creacion;
			}
			public void setFecha_creacion(Date fecha_creacion) {
				this.fecha_creacion = fecha_creacion;
			}
			public Date getFecha_modificacion() {
				return fecha_modificacion;
			}
			public void setFecha_modificacion(Date fecha_modificacion) {
				this.fecha_modificacion = fecha_modificacion;
			}
			public Integer getUsuario_creacion() {
				return usuario_creacion;
			}
			public void setUsuario_creacion(Integer usuario_creacion) {
				this.usuario_creacion = usuario_creacion;
			}
			public Integer getUsuario_modificacion() {
				return usuario_modificacion;
			}
			public void setUsuario_modificacion(Integer usuario_modificacion) {
				this.usuario_modificacion = usuario_modificacion;
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
			public String getAgencia() {
				return agencia;
			}
			public void setAgencia(String agencia) {
				this.agencia = agencia;
			}
			public Double getSaldo() {
				return saldo;
			}
			public void setSaldo(Double saldo) {
				this.saldo = saldo;
			}
		    
		    
		    
		    
	    
	}
	
	
	public String getHojaRuta() {
		return hojaRuta;
	}
	public void setHojaRuta(String hojaRuta) {
		this.hojaRuta = hojaRuta;
	}
	public String getNombreBeneficiario() {
		return nombreBeneficiario;
	}
	public void setNombreBeneficiario(String nombreBeneficiario) {
		this.nombreBeneficiario = nombreBeneficiario;
	}
	public static class Persona{
	    public Integer id_persona;
	    public String nombre_completo;
	    public Object paterno;
	    public String materno;
	    public Integer tipo_documento;
	    public String nro_documento;
	    public Object complemento;
	    public Integer lugar_expedicion;
	    public Object nacionalidad;
	    public Object genero;
	    public String fecha_nacimiento;
	    public Object estado_segip;
	    public Object fecha_verificacion;
	    public Object fecha_expiracion;
	    public String estado;
	    public Object observacion_segip;
	    public Object lugar_nacimiento;
	    public Object relacion_entidad;
	    public Object ocupacion;
	    public Object domicilio;
	    public String telefono;
	    public Object correo;
	    public Integer usuario_creacion;
	    public Integer usuario_modificacion;
	    public Date fecha_creacion;
	    public Date fecha_modificacion;
	    public String transaccion;
	    public String observacion;
	    public Object edad;
	    public String nombre_c;
		public Integer getId_persona() {
			return id_persona;
		}
		public void setId_persona(Integer id_persona) {
			this.id_persona = id_persona;
		}
		public String getNombre_completo() {
			return nombre_completo;
		}
		public void setNombre_completo(String nombre_completo) {
			this.nombre_completo = nombre_completo;
		}
		public Object getPaterno() {
			return paterno;
		}
		public void setPaterno(Object paterno) {
			this.paterno = paterno;
		}
		public String getMaterno() {
			return materno;
		}
		public void setMaterno(String materno) {
			this.materno = materno;
		}
		public Integer getTipo_documento() {
			return tipo_documento;
		}
		public void setTipo_documento(Integer tipo_documento) {
			this.tipo_documento = tipo_documento;
		}
		public String getNro_documento() {
			return nro_documento;
		}
		public void setNro_documento(String nro_documento) {
			this.nro_documento = nro_documento;
		}
		public Object getComplemento() {
			return complemento;
		}
		public void setComplemento(Object complemento) {
			this.complemento = complemento;
		}
		public Integer getLugar_expedicion() {
			return lugar_expedicion;
		}
		public void setLugar_expedicion(Integer lugar_expedicion) {
			this.lugar_expedicion = lugar_expedicion;
		}
		public Object getNacionalidad() {
			return nacionalidad;
		}
		public void setNacionalidad(Object nacionalidad) {
			this.nacionalidad = nacionalidad;
		}
		public Object getGenero() {
			return genero;
		}
		public void setGenero(Object genero) {
			this.genero = genero;
		}
		public String getFecha_nacimiento() {
			return fecha_nacimiento;
		}
		public void setFecha_nacimiento(String fecha_nacimiento) {
			this.fecha_nacimiento = fecha_nacimiento;
		}
		public Object getEstado_segip() {
			return estado_segip;
		}
		public void setEstado_segip(Object estado_segip) {
			this.estado_segip = estado_segip;
		}
		public Object getFecha_verificacion() {
			return fecha_verificacion;
		}
		public void setFecha_verificacion(Object fecha_verificacion) {
			this.fecha_verificacion = fecha_verificacion;
		}
		public Object getFecha_expiracion() {
			return fecha_expiracion;
		}
		public void setFecha_expiracion(Object fecha_expiracion) {
			this.fecha_expiracion = fecha_expiracion;
		}
		public String getEstado() {
			return estado;
		}
		public void setEstado(String estado) {
			this.estado = estado;
		}
		public Object getObservacion_segip() {
			return observacion_segip;
		}
		public void setObservacion_segip(Object observacion_segip) {
			this.observacion_segip = observacion_segip;
		}
		public Object getLugar_nacimiento() {
			return lugar_nacimiento;
		}
		public void setLugar_nacimiento(Object lugar_nacimiento) {
			this.lugar_nacimiento = lugar_nacimiento;
		}
		public Object getRelacion_entidad() {
			return relacion_entidad;
		}
		public void setRelacion_entidad(Object relacion_entidad) {
			this.relacion_entidad = relacion_entidad;
		}
		public Object getOcupacion() {
			return ocupacion;
		}
		public void setOcupacion(Object ocupacion) {
			this.ocupacion = ocupacion;
		}
		public Object getDomicilio() {
			return domicilio;
		}
		public void setDomicilio(Object domicilio) {
			this.domicilio = domicilio;
		}
		public String getTelefono() {
			return telefono;
		}
		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}
		public Object getCorreo() {
			return correo;
		}
		public void setCorreo(Object correo) {
			this.correo = correo;
		}
		public Integer getUsuario_creacion() {
			return usuario_creacion;
		}
		public void setUsuario_creacion(Integer usuario_creacion) {
			this.usuario_creacion = usuario_creacion;
		}
		public Integer getUsuario_modificacion() {
			return usuario_modificacion;
		}
		public void setUsuario_modificacion(Integer usuario_modificacion) {
			this.usuario_modificacion = usuario_modificacion;
		}
		public Date getFecha_creacion() {
			return fecha_creacion;
		}
		public void setFecha_creacion(Date fecha_creacion) {
			this.fecha_creacion = fecha_creacion;
		}
		public Date getFecha_modificacion() {
			return fecha_modificacion;
		}
		public void setFecha_modificacion(Date fecha_modificacion) {
			this.fecha_modificacion = fecha_modificacion;
		}
		public String getTransaccion() {
			return transaccion;
		}
		public void setTransaccion(String transaccion) {
			this.transaccion = transaccion;
		}
		public String getObservacion() {
			return observacion;
		}
		public void setObservacion(String observacion) {
			this.observacion = observacion;
		}
		public Object getEdad() {
			return edad;
		}
		public void setEdad(Object edad) {
			this.edad = edad;
		}
		public String getNombre_c() {
			return nombre_c;
		}
		public void setNombre_c(String nombre_c) {
			this.nombre_c = nombre_c;
		}
	    
	    
	}
	
	
	
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public Integer getIdCyt() {
		return idCyt;
	}
	public void setIdCyt(Integer idCyt) {
		this.idCyt = idCyt;
	}
	public String getCodigocyt() {
		return codigocyt;
	}
	public void setCodigocyt(String codigocyt) {
		this.codigocyt = codigocyt;
	}
	public String getNombreapoderado() {
		return nombreapoderado;
	}
	public void setNombreapoderado(String nombreapoderado) {
		this.nombreapoderado = nombreapoderado;
	}
	public Integer getTipodocapoderado() {
		return tipodocapoderado;
	}
	public void setTipodocapoderado(Integer tipodocapoderado) {
		this.tipodocapoderado = tipodocapoderado;
	}
	public String getNrodocapoderado() {
		return nrodocapoderado;
	}
	public void setNrodocapoderado(String nrodocapoderado) {
		this.nrodocapoderado = nrodocapoderado;
	}
	public String getTestimonioapoderado() {
		return testimonioapoderado;
	}
	public void setTestimonioapoderado(String testimonioapoderado) {
		this.testimonioapoderado = testimonioapoderado;
	}
	public String getNombreherederos() {
		return nombreherederos;
	}
	public void setNombreherederos(String nombreherederos) {
		this.nombreherederos = nombreherederos;
	}
	public Integer getTipodocheredero() {
		return tipodocheredero;
	}
	public void setTipodocheredero(Integer tipodocheredero) {
		this.tipodocheredero = tipodocheredero;
	}
	public String getNrodocheredero() {
		return nrodocheredero;
	}
	public void setNrodocheredero(String nrodocheredero) {
		this.nrodocheredero = nrodocheredero;
	}
	public Date getFechaacuerdopago2() {
		return fechaacuerdopago2;
	}
	public void setFechaacuerdopago2(Date fechaacuerdopago2) {
		this.fechaacuerdopago2 = fechaacuerdopago2;
	}
	public Date getFecharecibidoart22() {
		return fecharecibidoart22;
	}
	public void setFecharecibidoart22(Date fecharecibidoart22) {
		this.fecharecibidoart22 = fecharecibidoart22;
	}
	public Date getFechareincorporacion() {
		return fechareincorporacion;
	}
	public void setFechareincorporacion(Date fechareincorporacion) {
		this.fechareincorporacion = fechareincorporacion;
	}
	public String getInformeinspectorreinc() {
		return informeinspectorreinc;
	}
	public void setInformeinspectorreinc(String informeinspectorreinc) {
		this.informeinspectorreinc = informeinspectorreinc;
	}
	public Date getFechainformereinc() {
		return fechainformereinc;
	}
	public void setFechainformereinc(Date fechainformereinc) {
		this.fechainformereinc = fechainformereinc;
	}
	public String getCtadevolucion() {
		return ctadevolucion;
	}
	public void setCtadevolucion(String ctadevolucion) {
		this.ctadevolucion = ctadevolucion;
	}
	public Integer getEntidadfinanciera() {
		return entidadfinanciera;
	}
	public void setEntidadfinanciera(Integer entidadfinanciera) {
		this.entidadfinanciera = entidadfinanciera;
	}
	public String getAutoridadoj() {
		return autoridadoj;
	}
	public void setAutoridadoj(String autoridadoj) {
		this.autoridadoj = autoridadoj;
	}
	public String getCargooj() {
		return cargooj;
	}
	public void setCargooj(String cargooj) {
		this.cargooj = cargooj;
	}
	public String getJuzgadooj() {
		return juzgadooj;
	}
	public void setJuzgadooj(String juzgadooj) {
		this.juzgadooj = juzgadooj;
	}
	public String getDocumentooj() {
		return documentooj;
	}
	public void setDocumentooj(String documentooj) {
		this.documentooj = documentooj;
	}
	public Date getFechaoj() {
		return fechaoj;
	}
	public void setFechaoj(Date fechaoj) {
		this.fechaoj = fechaoj;
	}
	public Date getFechaInicioSalida() {
		return fechaInicioSalida;
	}
	public void setFechaInicioSalida(Date fechaInicioSalida) {
		this.fechaInicioSalida = fechaInicioSalida;
	}
	public Integer getIdTipoSalida() {
		return idTipoSalida;
	}
	public void setIdTipoSalida(Integer idTipoSalida) {
		this.idTipoSalida = idTipoSalida;
	}
	public Integer getIdTipoDesembolso() {
		return idTipoDesembolso;
	}
	public void setIdTipoDesembolso(Integer idTipoDesembolso) {
		this.idTipoDesembolso = idTipoDesembolso;
	}
	public Integer getIdTipoDevolucion() {
		return idTipoDevolucion;
	}
	public void setIdTipoDevolucion(Integer idTipoDevolucion) {
		this.idTipoDevolucion = idTipoDevolucion;
	}
	public Integer getIdCheque() {
		return idCheque;
	}
	public void setIdCheque(Integer idCheque) {
		this.idCheque = idCheque;
	}
	
	public List<ObjectMov> getMovimientosIngresos() {
		return movimientosIngresos;
	}
	public void setMovimientosIngresos(List<ObjectMov> movimientosIngresos) {
		this.movimientosIngresos = movimientosIngresos;
	}
	public Double getMontoFiniquito() {
		return montoFiniquito;
	}
	public void setMontoFiniquito(Double montoFiniquito) {
		this.montoFiniquito = montoFiniquito;
	}
	public String getNombreTramitador() {
		return nombreTramitador;
	}
	public void setNombreTramitador(String nombreTramitador) {
		this.nombreTramitador = nombreTramitador;
	}
	public String getDocTramitador() {
		return docTramitador;
	}
	public void setDocTramitador(String docTramitador) {
		this.docTramitador = docTramitador;
	}
	public String getTelefonoTramitador() {
		return telefonoTramitador;
	}
	public void setTelefonoTramitador(String telefonoTramitador) {
		this.telefonoTramitador = telefonoTramitador;
	}
	
	public Integer getIdFc() {
		return idFc;
	}
	public void setIdFc(Integer idFc) {
		this.idFc = idFc;
	}
	public Integer getIdCausal() {
		return idCausal;
	}
	public void setIdCausal(Integer idCausal) {
		this.idCausal = idCausal;
	}
	public String getCausal() {
		return causal;
	}
	public void setCausal(String causal) {
		this.causal = causal;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	public Integer getUsuario_creacion() {
		return usuario_creacion;
	}
	public void setUsuario_creacion(Integer usuario_creacion) {
		this.usuario_creacion = usuario_creacion;
	}
	public Integer getUsuario_modificacion() {
		return usuario_modificacion;
	}
	public void setUsuario_modificacion(Integer usuario_modificacion) {
		this.usuario_modificacion = usuario_modificacion;
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
	public String getNitEmpresa() {
		return nitEmpresa;
	}
	public void setNitEmpresa(String nitEmpresa) {
		this.nitEmpresa = nitEmpresa;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getMatriculaComercio() {
		return matriculaComercio;
	}
	public void setMatriculaComercio(String matriculaComercio) {
		this.matriculaComercio = matriculaComercio;
	}
	public Date getFechaAutorizado() {
		return fechaAutorizado;
	}
	public void setFechaAutorizado(Date fechaAutorizado) {
		this.fechaAutorizado = fechaAutorizado;
	}
	public Integer getIdTrabajador() {
		return idTrabajador;
	}
	public void setIdTrabajador(Integer idTrabajador) {
		this.idTrabajador = idTrabajador;
	}
	public Double getMontoFc() {
		return montoFc;
	}
	public void setMontoFc(Double montoFc) {
		this.montoFc = montoFc;
	}
	public String getCodigoCpt() {
		return codigoCpt;
	}
	public void setCodigoCpt(String codigoCpt) {
		this.codigoCpt = codigoCpt;
	}
	public Integer getIdCpt() {
		return idCpt;
	}
	public void setIdCpt(Integer idCpt) {
		this.idCpt = idCpt;
	}
	public Integer getIdDepositoCta() {
		return idDepositoCta;
	}
	public void setIdDepositoCta(Integer idDepositoCta) {
		this.idDepositoCta = idDepositoCta;
	}
	public Integer getIdTipoContrato() {
		return idTipoContrato;
	}
	public void setIdTipoContrato(Integer idTipoContrato) {
		this.idTipoContrato = idTipoContrato;
	}
	public String getTipoContrato() {
		return tipoContrato;
	}
	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}
	public Integer getIdTramiteFc() {
		return idTramiteFc;
	}
	public void setIdTramiteFc(Integer idTramiteFc) {
		this.idTramiteFc = idTramiteFc;
	}
	public Integer getIdTramiteFiniquito() {
		return idTramiteFiniquito;
	}
	public void setIdTramiteFiniquito(Integer idTramiteFiniquito) {
		this.idTramiteFiniquito = idTramiteFiniquito;
	}
	public Date getFechaFallecimiento() {
		return fechaFallecimiento;
	}
	public void setFechaFallecimiento(Date fechaFallecimiento) {
		this.fechaFallecimiento = fechaFallecimiento;
	}
	public Integer getTipoFallecimiento() {
		return tipoFallecimiento;
	}
	public void setTipoFallecimiento(Integer tipoFallecimiento) {
		this.tipoFallecimiento = tipoFallecimiento;
	}
	public String getNroRaAsistencia() {
		return nroRaAsistencia;
	}
	public void setNroRaAsistencia(String nroRaAsistencia) {
		this.nroRaAsistencia = nroRaAsistencia;
	}
	public Date getFechaRaAsistencia() {
		return fechaRaAsistencia;
	}
	public void setFechaRaAsistencia(Date fechaRaAsistencia) {
		this.fechaRaAsistencia = fechaRaAsistencia;
	}
	public Date getFechaAbandonoInicial() {
		return fechaAbandonoInicial;
	}
	public void setFechaAbandonoInicial(Date fechaAbandonoInicial) {
		this.fechaAbandonoInicial = fechaAbandonoInicial;
	}
	public Date getFechaAbandonoFinal() {
		return fechaAbandonoFinal;
	}
	public void setFechaAbandonoFinal(Date fechaAbandonoFinal) {
		this.fechaAbandonoFinal = fechaAbandonoFinal;
	}
	public Date getFechaRenuncia() {
		return fechaRenuncia;
	}
	public void setFechaRenuncia(Date fechaRenuncia) {
		this.fechaRenuncia = fechaRenuncia;
	}
	public Date getFechaMemoDespido() {
		return fechaMemoDespido;
	}
	public void setFechaMemoDespido(Date fechaMemoDespido) {
		this.fechaMemoDespido = fechaMemoDespido;
	}
	public Integer getCausaDespido() {
		return causaDespido;
	}
	public void setCausaDespido(Integer causaDespido) {
		this.causaDespido = causaDespido;
	}
	public Date getFechaAcuerdoPago() {
		return fechaAcuerdoPago;
	}
	public void setFechaAcuerdoPago(Date fechaAcuerdoPago) {
		this.fechaAcuerdoPago = fechaAcuerdoPago;
	}
	public Date getFechaEntregaFc() {
		return fechaEntregaFc;
	}
	public void setFechaEntregaFc(Date fechaEntregaFc) {
		this.fechaEntregaFc = fechaEntregaFc;
	}
	public String getJefatura() {
		return jefatura;
	}
	public void setJefatura(String jefatura) {
		this.jefatura = jefatura;
	}
	public String getCodigoFc() {
		return codigoFc;
	}
	public void setCodigoFc(String codigoFc) {
		this.codigoFc = codigoFc;
	}
	public Date getFechaDesvinculacion() {
		return fechaDesvinculacion;
	}
	public void setFechaDesvinculacion(Date fechaDesvinculacion) {
		this.fechaDesvinculacion = fechaDesvinculacion;
	}
	public String getNombreUsuarioCreacion() {
		return nombreUsuarioCreacion;
	}
	public void setNombreUsuarioCreacion(String nombreUsuarioCreacion) {
		this.nombreUsuarioCreacion = nombreUsuarioCreacion;
	}
	public String getNombreUsuarioModificacion() {
		return nombreUsuarioModificacion;
	}
	public void setNombreUsuarioModificacion(String nombreUsuarioModificacion) {
		this.nombreUsuarioModificacion = nombreUsuarioModificacion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getDocumentoDeposito() {
		return documentoDeposito;
	}
	public void setDocumentoDeposito(String documentoDeposito) {
		this.documentoDeposito = documentoDeposito;
	}
	public Integer getIdServicioFc() {
		return idServicioFc;
	}
	public void setIdServicioFc(Integer idServicioFc) {
		this.idServicioFc = idServicioFc;
	}
	public String getCodServicioFc() {
		return codServicioFc;
	}
	public void setCodServicioFc(String codServicioFc) {
		this.codServicioFc = codServicioFc;
	}
	public Integer getIdServicioFiniquito() {
		return idServicioFiniquito;
	}
	public void setIdServicioFiniquito(Integer idServicioFiniquito) {
		this.idServicioFiniquito = idServicioFiniquito;
	}
	public String getCodServicioFiniquito() {
		return codServicioFiniquito;
	}
	public void setCodServicioFiniquito(String codServicioFiniquito) {
		this.codServicioFiniquito = codServicioFiniquito;
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
	public String getDocTemporalidad() {
		return docTemporalidad;
	}
	public void setDocTemporalidad(String docTemporalidad) {
		this.docTemporalidad = docTemporalidad;
	}
	
	public String getCpt() {
		return cpt;
	}
	public void setCpt(String cpt) {
		this.cpt = cpt;
	}
	public String getCodigoOrden() {
		return codigoOrden;
	}
	public void setCodigoOrden(String codigoOrden) {
		this.codigoOrden = codigoOrden;
	}
	public String getCodigoTransaccion() {
		return codigoTransaccion;
	}
	public void setCodigoTransaccion(String codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}
	public String getUrlRedireccion() {
		return urlRedireccion;
	}
	public void setUrlRedireccion(String urlRedireccion) {
		this.urlRedireccion = urlRedireccion;
	}
	public Integer getIdPagoFc() {
		return idPagoFc;
	}
	public void setIdPagoFc(Integer idPagoFc) {
		this.idPagoFc = idPagoFc;
	}
	public Object getRepositorio() {
		return repositorio;
	}
	public void setRepositorio(Object repositorio) {
		this.repositorio = repositorio;
	}
	public String getNombreTipoSalida() {
		return nombreTipoSalida;
	}
	public void setNombreTipoSalida(String nombreTipoSalida) {
		this.nombreTipoSalida = nombreTipoSalida;
	}
	public String getNombreTipoDesembolso() {
		return nombreTipoDesembolso;
	}
	public void setNombreTipoDesembolso(String nombreTipoDesembolso) {
		this.nombreTipoDesembolso = nombreTipoDesembolso;
	}
	
	public Double getMontoCheque() {
		return montoCheque;
	}
	public void setMontoCheque(Double montoCheque) {
		this.montoCheque = montoCheque;
	}
	public Date getFechaEmitido() {
		return fechaEmitido;
	}
	public void setFechaEmitido(Date fechaEmitido) {
		this.fechaEmitido = fechaEmitido;
	}
	public String getNroCheque() {
		return nroCheque;
	}
	public void setNroCheque(String nroCheque) {
		this.nroCheque = nroCheque;
	}

	

}
