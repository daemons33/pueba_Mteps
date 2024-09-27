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

@NamedStoredProcedureQuery(name = "mteps_plan.f_reporte_formulacion_poa", procedureName = "mteps_plan.f_reporte_formulacion_poa", resultClasses = {
		F_reporte_formulacion.class }, parameters = {
				@StoredProcedureParameter(name = "v_id_plan", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "v_id_estado", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_reporte_formulacion {
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
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public PemPei pem_pei;
	public Integer id_plan;
	public Integer id_plan_superior;
	public Integer id_tipo_plan;
	public String sigla_plan;
	public String nombre_plan;
	public Date inicio_gestion;
	public Date fin_gestion;
	public Integer valor_gestion;
	public Integer id_estado;
	public String transaccion;
	public String estado;
	public String observacion;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<DetalleFormulacion> detalle_formulacion;
		
		
	public static class PemPei{
		public int id_proceso_eje;
	    public String sigla_eje;
	    public String nombre_eje;
	    public String descripcion_eje;
	    public int id_proceso_meta;
	    public String sigla_meta;
	    public String nombre_meta;
	    public String descripcion_meta;
	    public int id_proceso_resultado;
	    public String sigla_resultado;
	    public String nombre_resultado;
	    public String descripcion_resultado;
	    public int id_proceso_accion_pdes;
	    public String sigla_accion_pdes;
	    public String nombre_accion_pdes;
	    public String descripcion_accion_pdes;
	    public int id_proceso_res_pei;
	    public String sigla_res_pei;
	    public String nombre_res_pei;
	    public String descripcion_res_pei;
	    public int id_proceso_accion_pei;
	    public String sigla_accion_pei;
	    public String nombre_accion_pei;
	    public String descripcion_accion_pei;
	}
	
	public static class Actividade{
	    public int id_proceso;
	    public String sigla;
	    public String nombre;
	    public int id_tipo_proceso;
	    public String descripcion;
	    public ArrayList<ProgramacionIndicadorMetum> programacion_indicador_meta;
	    public ArrayList<TareaEspecifica> tarea_especifica;
	}

	public static class Operacione{
	    public int id_proceso;
	    public String sigla;
	    public String nombre;
	    public int id_tipo_proceso;
	    public String descripcion;
	    public ArrayList<ProgramacionIndicadorMetum> programacion_indicador_meta;
	    public ArrayList<Actividade> actividades;
	}

	public static class ProgramacionIndicadorMetum{
	    public int id_indicador;
	    public int id_tipo_identificador;
	    public String tipo_indicador;
	    public String denominacion;
	    public double ponderacion;
	    public String medio_verificacion;
	    public String unidad;
	    public String formula;
	    public String linea_base;
	    public String meta;
	    public ArrayList<ProgramacionIndicadorMetum1> programacion_indicador_meta;
	   	}
	
	public static class ProgramacionIndicadorMetum1{
	   
	    public int id_indicador_meta;
	    public int interval_valor;
	    public String periodo;
	    public String inicio;
	    public String fin;
	    public double valor_programado_num;
	    public double valor_ejecutado_num;
	    public int id_sigec;
	    public String cite_sigec;
	    public String medio_verificacion;
	    public Double ejecutado_a;
	    public Double ejecutado_b;
	    public Double ejecutado_c;
	    public String estado;
	    public String transaccion;
	}
	

	public static class DetalleFormulacion{
	    public int id_proceso;
	    public String sigla;
	    public String nombre;
	    public int id_tipo_proceso;
	    public String descripcion;
	    public ArrayList<ProgramacionIndicadorMetum> programacion_indicador_meta;
	    public ArrayList<Operacione> operaciones;
	}

	public static class TareaEspecifica{
	    public int id_proceso;
	    public String sigla;
	    public String nombre;
	    public int id_tipo_proceso;
	    public String descripcion;
	    public ArrayList<ProgramacionIndicadorMetum> programacion_indicador_meta;
	    public ArrayList<MemoriaCalculo> memoria_calculo;
	}
	
	public static class MemoriaCalculo{
	    public int id_memoria_calculo;
	    public String descripcion;
	    public double cantidad;
	    public String unidad_medida;
	    public double precio_unitario;
	    public String justificacion;
	    public int id_proceso;
	    public int id_partida_presupuesto;
	    public int id_partida_presupuesto_sup;
	    public String cod_partida;
	    public String partida;
	    public double importe_total;
	    public int id_organismo_financiador;
	    public String organismo_financiador;
	    public int id_fuente_financiamiento;
	    public String fuente_financiamiento;
	    public Object id_unidad_organizacional;
	    public Object act_func_consultoria;
	    public Object result_consultoria;
	    public Object duracion_consultoria_meses;
	    public Object nro_casos_consultoria;
	    public Object transaccion;
	    public String estado;
	    public Object observacion;
	    public double saldo;
	    public Integer id_tipo_pasaje;
	    public Integer id_tipo_viatico;
	    public ArrayList<ProgramaMemoriaCalculo> programa_memoria_calculo;
	}
	public static class ProgramaMemoriaCalculo{
	    public int id_programa_mem_calculo;
	    public String inicio;
	    public String fin;
	    public double cantidad;
	    public double costo;
	}
}

