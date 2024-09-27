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

import lombok.Data;


@NamedStoredProcedureQuery(name = "mteps_tramites.f_reporte_tramites", procedureName = "mteps_tramites.f_reporte_tramites", resultClasses = {
		FReporteTramites.class }, parameters = {
				@StoredProcedureParameter(name = "v_login", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_fecha_ini", type = Date.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_fecha_fin", type = Date.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_id_unidad", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_estado", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_id_tipo", type = Integer.class, mode = ParameterMode.IN)})
@Entity
@Data
public class FReporteTramites {

	@Id
	public Integer idTramite;
    public String codigoTramite;
    public Integer idTipoTramite;
    public String tipoTramite;
    public Double montoTotalMulta;
    public String transaccion;
    public String estado;
    public String login;
    public String nombreUsuarioCreacion;
    public Integer idunidad;
    public String unidadFuncional;
    public Integer idDepende;
    public String puesto;
    public String cargo;
    public Integer usuarioCreacion;
    public Integer usuarioModificacion;
    @Temporal(TemporalType.TIMESTAMP)
	 @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone="GMT-4")
	 public Date fechaCreacion;
    public Date fechaModificacion;
    public Integer idEmpresa;
    public String periodoPlanilla;
    public Integer gestionPlanilla;
    public Boolean minera;
    public Integer diasRetraso;
    public Double montoCalculo;
    public Integer nroTrabajadores;
    public Date fechaPagoPrima;
    public String detalleFiniquito;
    public String resolucionAdministrativa;
    public String relacionEntidad;
    public Integer nroFojas;
    public String observacion;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public Object jsonRequisitos;
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
    public String motivoErr;
    public Double montoErr;
    public String razonSocial;
    public String descripcionSucursal;
    public Date fechaRa;
    public String citeRa;
    public Integer idPersona;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public Persona datosPersona;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public List<Pagos> datosPagos;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public CalculoMulta datosCalcMulta;
    
    public static class Persona{
        public Integer id_persona;
        public String nombre_completo;
        public String  paterno;
        public String  materno;
        public Integer tipo_documento;
        public String nro_documento;
        public String  complemento;
        public Integer lugar_expedicion;
        public String  nacionalidad;
        public Integer genero;
        public Date fecha_nacimiento;
        public Integer estado_segip;
        public Date fecha_verificacion;
        public Date fecha_expiracion;
        public String estado;
        public String observacion_segip;
        public String lugar_nacimiento;
        public String relacion_entidad;
        public String ocupacion;
        public String domicilio;
        public String telefono;
        public String correo;
        public Integer usuario_creacion;
        public Integer usuario_modificacion;
        public Date fecha_creacion;
        public Date fecha_modificacion;
        public String transaccion;
        public String observacion;
        public Integer edad;
    }
    
    public static class Pagos{
        public Integer id_pago;
        public Date fecha_creacion;
        public Date fecha_modificacion;
        public String fecha_pago;
        public Integer usuario_creacion;
        public Integer usuario_modificacion;
        public Double costo;
        public String transaccion;
        public String observacion;
        public String estado;
        public Integer id_tramite;
        public List<Depositos> json_agg;
        
        public static class Depositos{
            public Integer id_deposito;
            public Date fecha_creacion;
            public Date fecha_modificacion;
            @Temporal(TemporalType.DATE)
            @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",timezone="GMT-4")
            public Date fecha_deposito;
            public String nro_deposito;
            public Integer usuario_creacion;
            public Integer usuario_modificacion;
            public Double monto;
            public String transaccion;
            public String observacion;
            public String estado;
            public Integer id_pago;
            public Integer id_tipo_pago;
            public String concepto;
            public Integer idcuentafiscal;
        }

    }
    
    public static class CalculoMulta{
        public Integer id_multa;
        public String codigo;
        public Date fecha_calculo;
        public Integer id_empleador;
        public Integer id_tipo_planilla;
        public String mes;
        public Integer gestion;
        public Integer dias_retraso;
        public Integer monto_planilla;
        public Double multa_calculada;
        public Boolean minera;
        public Date fecha_pago_prima;
        public String nit;
        public String matricula_comercio;
        public String transaccion;
        public String estado;
        public Integer usuario_creacion;
        public Integer usuario_modificacion;
        public Date fecha_creacion;
        public Date fecha_modificacion;
        public Boolean empresa_externa;
        public Date fecha_fin_declaracion;
        public String correo;
        public Integer id_tramite;
        public String nombre_unidad;
    }
}
