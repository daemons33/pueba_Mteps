package mteps.tramites.fondoCustodia.entity;

import java.math.BigDecimal;
import java.util.Date;

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


//@NamedStoredProcedureQuery(name="mteps_tramites.f_obtener_fc_ingresos",
//procedureName = "mteps_tramites.f_obtener_fc_ingresos",
//resultClasses = {ListaFcIngreso.class},
//parameters = {
//		@StoredProcedureParameter(name = "p_json_pp", type = String.class, mode = ParameterMode.IN)
//})

@Entity
public class ListaFcIngreso {
	@Id
	public Integer  idFc;
	public Integer  idCausal;
	public String  causal;
	public String  estado;
	public String  transaccion;
	public Integer  usuario_creacion;
	public Integer  usuario_modificacion;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaCreacion;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaModificacion;
	public Integer  idEmpresa;
	public String  nitEmpresa;
	public String  razonSocial;
	public String  matriculaComercio;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
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
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaFallecimiento;
	public Integer  tipoFallecimiento;
	public String  nroRaAsistencia;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaRaAsistencia;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaAbandonoInicial;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaAbandonoFinal;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaRenuncia;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaMemoDespido;
	public Integer  causaDespido;
	public Date fechaAcuerdoPago;
	public Date fechaEntregaFc;
	public String  jefatura;
	public String  codigoFc;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
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
	public Object persona;
	public String cpt;
	public String codigoOrden;
	public String codigoTransaccion;
	public String urlRedireccion;
	public Integer idPagoFc;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Object repositorio;
//	@Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone = "GMT-4")
	public Date fechaContratoRefrendado;
	public Double montoFiniquito;
	public String nombreTramitador;
	public String docTramitador;
	public String telefonoTramitador;
	
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Object movimientoCb;
	
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
    public String nombreCuentaBancaria;
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
	public String codigoTramite;
	public String tramite;
	public String nombrePersona;
	public String relacionLaboral;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Object repositorioCheques;
	public String nombretiposalida;
	public String nombredesembolso;
	public String nombredevolucion;

	public Boolean art22;
	public Boolean historico;
	public String hojaRuta;
	public Boolean sinFiniquito;
	
	public String nombreCompleto;
	public String paterno;
	public String materno;
	public Integer tipoDocumento;
	public String nroDocumento;
	public String complemento;
	public Integer lugarExpedicion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaNacimiento;
	public Boolean sgt;
	public Boolean extranjero;
	public String descripcionAdd;
	public String citeInformeLegal;
	public Integer idPagoTram;
	
	public static class MovimientoCB{
		
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
	}
}
