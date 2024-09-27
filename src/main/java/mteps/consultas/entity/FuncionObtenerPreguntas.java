package mteps.consultas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_preguntas", procedureName = "mteps_denuncias.f_obtener_preguntas", resultClasses = {
		FuncionObtenerPreguntas.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_consulta", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class FuncionObtenerPreguntas {

	@Id
	public Integer idPregunta;
	public Integer idConsulta; 
	public Integer idTipoConsulta;
	public String tipoConsulta;
	public String pregunta; 
	public String respuesta;
	public String estado;
	public String transaccion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaCreacion;
@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fechaModificacion;
	public String observacion;
	 
}
