package mteps.rtep.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;
@NamedStoredProcedureQuery(name = "mteps_rtep.f_obtener_formulario_usuario", procedureName = "mteps_rtep.f_obtener_formulario_usuario", resultClasses = {
		FObtenerFormularioUsuario.class },
parameters = {
		@StoredProcedureParameter(name = "v_id_usuario_formulario", type = Integer.class, mode = ParameterMode.IN)
})
@Entity
public class FObtenerFormularioUsuario {

	@Id
	public Integer idFormulario;
	public String codigo;
	public String nombre;
	public String codigoFormulario;
	public Integer idUsuario;
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public List<Grupo> grupos;
	
	public static class DetalleRespuestum{
	    public int idOpcion;
	    public String nombre;
	}

	public static class Opcione{
	    public int idOpcion;
	    public String nombre;
	    public List<SubOpcion> subOpcion;
	}

	public static class Pregunta{
	    public Integer idPregunta;
	    public String codigo;
	    public String numeracion;
	    public String nombre;
	    public String descripcion;
	    public Integer tipo;
	    public String tipoPregunta;
	    public Boolean obligatorio;
	    public Boolean varios;
	    public Boolean abierto;
	    public Boolean verdaderoFalso;
	    public Boolean georeferencia;
	    public Boolean respuestaOtro;
	    public Integer idClasificadorOtro;
	    public Integer idClasificador;
		@Type(type = "jsonb")
		@Column(columnDefinition = "json")
	    public List<Opcione> opciones;
		@Type(type = "jsonb")
		@Column(columnDefinition = "json")
	    public Respuesta respuestas;
	}

	public static class Respuesta{
	    public Integer idRespuesta;
	    public List<Integer> respuestaClasificador;
	    public String respuestaAbierto;
	    public Boolean respuestaVF;
	    public String respuestaOtro;
	    public Double respuestaLatitud;
	    public Double respuestaLongitud;
	    @Type(type = "jsonb")
		@Column(columnDefinition = "json")
	    public List<DetalleRespuestum> detalleRespuesta;
	}

	public static class Grupo{
	    public Integer idGrupo;
	    public String codigo;
	    public String numeracion;
	    public String nombre;
	    public String descripcion;
	    @Type(type = "jsonb")
		@Column(columnDefinition = "json")
	    public List<Pregunta> preguntas;
	}

	public static class SubOpcion{
	    public Integer idOpcion;
	    public String nombre;
	}
	
	
}
