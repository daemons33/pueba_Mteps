package mteps.poa.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;

@NamedStoredProcedureQuery(name = "mteps_plan.f_reporte_form_presupuesto_gasto", procedureName = "mteps_plan.f_reporte_form_presupuesto_gasto", resultClasses = {
		F_reporte_form_presupuesto_gasto.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_plan", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_id_estado", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_reporte_form_presupuesto_gasto {
	@Id	
	public Integer id_org;
	public String org_unidad_funcional;
	public Integer id_apertura_organizacion;
	public Integer id_apertura;
	public String dominio_act_presupuestario;
	public String sigla_act_presupuestario;
	public String nombre_act_presupuestario;
	public String dominio_prog_presupuestario;
	public String sigla_prog_presupuestario;
	public String nombre_prog_presupuestario;
	public Integer id_plan;
	public Integer id_plan_superior;
	public Integer id_tipo_plan;
	public String sigla_plan;
	public String nombre_plan;
	public Date inicio_gestion;
	public Date fin_gestion;
	public Integer valor_gestion;
	public String transaccion;
	public String estado;
	public String observacion;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<Partidas> partidas;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public Totales totales;
	
	public static class MemoriasCalculo{
	    public int id_memoria_calculo;
	    public String descripcion;
	    public String unidad_medida;
	    public double cantidad;
	    public double precio_unitario;
	    public String justificacion;
	    public double importe_total;
	    public String sigla_fuente_financiamiento;
	    public String fuente_financiamiento;
	    public String sigla_organismo_financiador;
	    public String organismo_financiador;
	    public String act_func_consultoria;
	    public String result_consultoria;
	    public int duracion_consultoria_meses;
	    public int nro_casos_consultoria;
	    public ArrayList<Programacion> programacion;
	    public double total_cantidad;
	    public double total_costo;
	}

	public static class Partida{
	    public int id_partida_presupuesto;
	    public int id_moneda;
	    public String sigla_moneda;
	    public String nombre_moneda;
	    public String cod_partida;
	    public String nombre_partida;
	    public ArrayList<Clasificador> fuentes_financiamiento;
	    public ArrayList<Clasificador> organismo_financiador;
	    public ArrayList<MemoriasCalculo> memorias_calculo;
	    public double fuente_financiamiento_tgn;
	    public double fuente_financiamiento_tgn_otros;
	    public double fuente_financiamiento_tgn_total;
	}
	public static class Clasificador{
	    public int id_clasificador;
	    public String clasificador;	    
	}
	public static class Programacion{
	    public int id_programa_mem_calc;
	    public String inicio;
	    public String fin;
	    public double cantidad;
	    public double costo;
	}

	public static class Partidas{
	    public int id_partida_presupuesto;
	    public int id_moneda;
	    public String sigla_moneda;
	    public String nombre_moneda;
	    public String cod_partida;
	    public String nombre_partida;
	    public double fuente_financiamiento_tgn;
	    public double fuente_financiamiento_tgn_otros;
	    public double fuente_financiamiento_tgn_total;
	    public ArrayList<Partida> partidas;
	}



	
	public static class Totales{
	    public double total_fuente_financiamiento_tgn;
	    public double total_fuente_financiamiento_tgn_otros;
	    public double total_fuente_financiamiento_tgn_total;
	}
	
	
}
