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

@NamedStoredProcedureQuery(name = "mteps_plan.f_presupuesto_reformulaciones_partida", procedureName = "mteps_plan.f_presupuesto_reformulaciones_partida", resultClasses = {
		F_ejecucion_presupuestaria_partida_reformulacion.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_plan", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_ejecucion_presupuestaria_partida_reformulacion {
	@Id
	public Integer id_plan;
	public String sigla_p; 
	public String nombre_p; 
	public String sigla_a; 
	public String nombre_a;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb") 
	public List<Detalle_plan> detalle_plan ;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Detalle_partidas> detalle_partidas;
	
	public static class Detalle_plan{
	    public int id;
	    public Integer id_plan_a;
	    @Temporal(TemporalType.TIMESTAMP)
		@JsonFormat(shape=JsonFormat.Shape.STRING, timezone="GMT-4")
	    public Date fecha;
	}
	
	public static class Detalle_partidas{
	    public String codigo;
	    public String partida;
	    public List<DetalleF> detalle_f;
	}
	
	public static class DetalleF{
	    public String fuente;
	    public List<DetalleT> detalle_t;
	}

	public static class DetalleT{
	    public Double total;
	}
}
