package mteps.poa.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;

@NamedStoredProcedureQuery(name = "mteps_plan.f_ejecucion_seguimiento_fisico_presupuestario", procedureName = "mteps_plan.f_ejecucion_seguimiento_fisico_presupuestario", resultClasses = {
		F_ejecucion_seguimiento_fisico_presupuestario.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_plan", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_fecha_inicio", type = Date.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_fecha_final", type = Date.class, mode = ParameterMode.IN)})
@Entity
public class F_ejecucion_seguimiento_fisico_presupuestario {
	
	@Id
	public Integer id_proceso;
	public Integer id_apertura_organizacion;
	public Integer id_apertura;
	public String dominio_prog;
	public String sigla_prog;
	public String nombre_prog;
	public String dominio_act;
	public String sigla_act;
	public String nombre_act;
	public String te;
	public String te_sigla;
	public String te_descripcion;
	public Integer gestion;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<progFisica> programado_fisico;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Clasificador> fuente_f;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Clasificador> organismo_f;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Partidas> detalle;
	
	public static class Partidas{
	    public int id_partida_presupuesto;
	    public String cod_partida;
	    public String partida;
	    public double presupuesto_vigente;
	    public double presupuesto_mes;	    
	    public double preventivo;
	    public double saldo;
	}
	
	public static class Clasificador{
		 public int id_clasificador;
		 public String clasificador;
	}
	public static class progFisica{
		public String denominacion;
	    public Integer id_tipo_identificador;
		public Double programado;
	    public Double ejecutado;
	    
	}

}
