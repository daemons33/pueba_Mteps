package mteps.sistpoa.Pojos;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;

@NamedStoredProcedureQuery(name="mteps_plan.f_reporte_memoria_calculo",
procedureName = "mteps_plan.f_reporte_memoria_calculo",
resultClasses = {ReporteMemoriaCalculo.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_org", type = Integer.class, mode = ParameterMode.IN)
})
@Entity
public class ReporteMemoriaCalculo {
	@Id
	public Long id_memoria_calculo;
	public String fuente_financiamiento;
	public String org_fin;
	public String sigla_programa_presupuestario;
	public String nombre_programa_presupuestario;
	public String sigla_actividad_presupuestaria;
	public String nombre_actividad_presupuestaria;
	public String unidad_organizacional;
	public String sigla_plan;
	public String nombre_plan;
	public Date inicio_gestion;
	public Date fin_gestion;
	public String sigla_acp;
	public String nombre_acp;
	public String descripcion_acp;
	public String sigla_op;
	public String nombre_op;
	public String descripcion_op;
	public String sigla_act;
	public String nombre_act;
	public String descripcion_act;
	public String sigla_te;
	public String nombre_te;
	public String descripcion_te;
	public String cod_partida;
	public String partida;
	public String descripcion_memoria_calculo;
	public Double cantidad_memoria_calculo;
	public String unidad_medida_memoria_calculo;
	public Double precio_unitario_memoria_calculo;
	public Double importe_total_memoria_calculo;
	public String justificacion_memoria_calculo;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<ReporteMemoriaCalculoDetalle> detalle_memoria_calculo;
}
