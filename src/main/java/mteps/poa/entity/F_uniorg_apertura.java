package mteps.poa.entity;

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


@NamedStoredProcedureQuery(name = "mteps_plan.f_obtener_programa_apertura", procedureName = "mteps_plan.f_obtener_programa_apertura", resultClasses = {
		F_uniorg_apertura.class },parameters = {
				@StoredProcedureParameter(name = "v_id_organizacion", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_id_gestion", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_login", type = String.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_tipo", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_uniorg_apertura {
	
	@Id
	public Integer id_clasificador;
	public String dominio;
	public String sigla;
	public String nombre;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Apertura> aperturas;
	
	public static class Apertura{
	    public int id_apertura_organizacion;
	    public int id_clasificador;
	    public String dominio;
	    public String sigla;
	    public String nombre;
	    public int id_plan;
	    public String estado;
	    public Integer ejecucion_acc;
	    public Integer ejecucion_op;
	    public Integer ejecucion_act;
	    public Integer ejecucion_te;
	    public Integer periodo;
	    @Temporal(TemporalType.TIMESTAMP)
	    @JsonFormat(shape=JsonFormat.Shape.STRING, timezone="GMT-4")
	    public Date fec_ini_ejecucion;
	    @Temporal(TemporalType.TIMESTAMP)
	    @JsonFormat(shape=JsonFormat.Shape.STRING, timezone="GMT-4")
	    public Date	fec_fin_ejecucion;
	    
	    
	}
}
