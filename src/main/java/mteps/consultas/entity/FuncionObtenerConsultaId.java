package mteps.consultas.entity;

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

import mteps.inspectoria.entity.EPreFiniquito;
@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_consulta_id", procedureName = "mteps_denuncias.f_obtener_consulta_id", resultClasses = {
		FuncionObtenerConsultaId.class }, parameters = {
				@StoredProcedureParameter(name = "v_id", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class FuncionObtenerConsultaId {
	@Id
	public Integer idConsulta;
	public Integer idPersona;
	public String nombrePersona;
	public Integer idEmpleador;
	public String codigoConsulta;
	public String estado;
	public String transaccion;
	public Integer idSectorTrabajador;
	public String sectorTrabajador;
	public Integer idEstadoLaboral;
	public String estadoLaboral;
	public Integer usuarioCreacion;
	public String nombreUsuarioCreacion;
	public Integer usuarioModificacion;
	public String nombreUsuarioModificacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaCreacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaModificacion;
	public String observacion;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public EPreFiniquito preFiniquito;
	public String docPreFiniquito;
	public Boolean empresaExterna;
	public String nit;
	public String razonSocial;
	public String matriculaComercio;
}
