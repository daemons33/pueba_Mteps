package mteps.inspectoria.entity;

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

import mteps.rrhh.entity.FuncionObtenerDependientes;

@NamedStoredProcedureQuery(name = "mteps_inspecciones.f_obtener_inspeccion_id", procedureName = "mteps_inspecciones.f_obtener_inspeccion_id", resultClasses = {
		FObtenerInspeccionId.class }, parameters = {
				@StoredProcedureParameter(name = "v_id", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class FObtenerInspeccionId {
	@Id
	public Integer idInspeccion ; 
	public Integer idDenuncia ;
	public Integer idInspeccionPrevia ;
	public Integer idEmpresa ;
	public Boolean empresaExterna;
	public String nit ;
	public String razonSocial ;
	public String matriculaComercio ;
	public String direccionEmpresa ;
	public Integer idTipoInspeccion  ;
	public String nombreTipoInspeccion ;
	public String sigecHr ;
	public String sigecCiteMemo ;
	
	public String direccionInspeccion  ;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<FuncionObtenerDependientes>  inspectores;
	public Integer idTipoDevolucion ;
	public String nombreTipoDevolucion ;
	public String citeSigecInformeDev  ;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaInspeccion  ;
	public Boolean inspeccionRealizada ;
	public Boolean puntosObservados ;
	public Integer nroPuntosObservados ;
	public Integer diasPlazoObservacion ;
	public Integer nroTrabajadoresMulta ;
	public Double sugerenciaMultaBs;
	public Boolean sugerenciaMultaUfv ;
	public Boolean informeCorrecto ;
	public String observacionInforme ;
	public String citeSigecInformeInsp ;
	public Boolean obstruccion ;
	public Integer idTipoObstruccion  ;
	public String nombreTipoObstruccion ;
	public String citeSigecInformeObst ;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaNotificación  ;
	public Integer idUsuarioNotifica  ;
	public String nombreUsuarioNotifica  ; 
	public String citeSigecNotificacion ;
	public Boolean paralizacion ;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EntAreasParalizacion> areasParalizacion;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EntCondicionesParalizacion> condicionesParalizacion;
	public Integer plazoParalizacion ;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EntFotosParalizacion> fotosParalizacion;
	public Integer nroFotosParalizacion ;
	public Integer idClasificacionAccidente ;
	public String nombreClasificacionAccidente ;
	public Boolean instalacionesEmpresaAcc ;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaAcc;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EntTrabajadorAcc> trabajadorAcc;
	public String ubicacionAcc  ;
	public String haciendoTrabAcc  ;
	public String maquinaHerrAcc  ;
	public String operacionAcc ;
	public Boolean personasPresAcc ;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<EntDatosPersAcc> datosPersAcc;
	public String hechosAcc  ;
	public String discapacidadAcc  ;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaDerivacionAreaLegal  ;
	public String nurejJudicializacion  ;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaInicioProcesoJudicializacion;
	public Integer usuarioCreacion ; 
	public String nombreUsuarioCreacion  ; 
	public Integer usuarioModificacion ; 
	public String nombreUsuarioModificacion  ;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaCreacion ;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaModificacion ;
	public String transaccion ;
	public String estado ;
	public String observacion ;
	public String unidadOrganizacional;
	public String citeSigecConminatoria;
	public String codigo;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public EntActa detalleActas;
}


