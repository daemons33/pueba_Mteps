package mteps.poa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;

@NamedStoredProcedureQuery(name = "mteps_plan.f_ejecucion_presupuestaria_actividad_partida", procedureName = "mteps_plan.f_ejecucion_presupuestaria_actividad_partida", resultClasses = {
		F_ejecucion_presupuestaria_actividad_partida.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_plan", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_ejecucion_presupuestaria_actividad_partida {
	@Id
	public Integer id_plan;
	public Integer id_apertura_organizacion;
	public Integer id_apertura;
	public String dominio_prog;
	public String sigla_prog;
	public String nombre_prog;
	public String dominio_act;
	public String sigla_act;
	public String nombre_act;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Ejecucion>  ejecucion;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Total>  total;
	
	
	public static class Detalle{
	    public int id_clasificador;
	    public String sigla;
	    public String nombre;
	    public double presupuesto_inicial;
	    public double modificacion_aprobada;
	    public double presupuesto_vigente;
	    public double preventivo;
	    public double saldo;
	}

	public static class Ejecucion{
	    public int id_partida_presupuesto;
	    public String cod_partida;
	    public String partida;
	    public ArrayList<Detalle> detalle;
	}
	
	public static class Total{
	    public int id_clasificador;
	    public String sigla;
	    public String nombre;
	    public double presupuesto_inicial;
	}
}
