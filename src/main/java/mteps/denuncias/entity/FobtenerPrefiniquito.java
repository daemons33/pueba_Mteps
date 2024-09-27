package mteps.denuncias.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_denuncias.f_obtener_prefiniquito", procedureName = "mteps_denuncias.f_obtener_prefiniquito", resultClasses = {
		FobtenerPrefiniquito.class }, parameters = {
				@StoredProcedureParameter(name = "i_id", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_tipo", type = String.class, mode = ParameterMode.IN)})
@Entity
public class FobtenerPrefiniquito {
	@Id
	public Integer id_pre_finiquito;
	public String codigo_pre_finiqutio;
	public String tipo_retiro;
	public String estado;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date fecha_inicio;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date fecha_fin;
	public Double sueldo1;
	public Double sueldo2;
	public Double sueldo3;
	public Double dias_vacacion_sin_pagar ;
	public String concepto1;
	public String concepto2;
	public String concepto3 ;
	public Double otros1 ;
	public Double otros2 ;
	public Double otros3 ;
	public Integer id_consulta ;
	public Integer id_denuncia ;
	public Double salario_minimo ;
	public String correo;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_creacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_modificado;
}
