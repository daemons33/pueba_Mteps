package mteps.consultas.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntPreguntas {
	@Id
	public Integer id_pregunta;
	public Integer id_consulta;
	public Integer id_tipo_consulta;
	public String pregunta;
	public String respuesta;
}
