package mteps.consultas.entity;


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



@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_consultas", procedureName = "mteps_denuncias.f_obtener_consultas", resultClasses = {
		FuncionObtenerConsultas.class }, parameters = {
				@StoredProcedureParameter(name = "p_json_pp", type = String.class, mode = ParameterMode.IN)})
@Entity
public class FuncionObtenerConsultas {
	@Id
	public Integer idConsulta;
	public Integer idPersona;
	public Integer idPreFiniquito; // APLICAR PROD
	public String nombrePersona;
	public String domicilio;
	public String nroDocumento;
	public String telefono;
	public String correo;
	public Integer idTipoDocumento;
	public String tipoDocumento;
	public Integer idLugarExpedicion;
	public String lugarExpedicion;
	public String complemento;
	public Integer genero;
	public Integer edad;
	public Integer idEmpleador;
	public Boolean empresaExterna;
	public String nit;
	public String razonSocial;
	public String matriculaComercio;
	public String correoElectronico;
	public String departamento;
	public String direccion;
	public String tipoinstitucion; // APLICAR PROD
	public String descactividaddeclarada;// APLICAR PROD
	public String telefonoemp;// APLICAR PROD
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
	public List<EntPreguntas> preguntas;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public Prefiniquito prefiniquito;
	

	
}
