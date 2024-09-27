package mteps.poa.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;



@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_indicador", procedureName = "mteps_plan.f_lista_indicador", resultClasses = {
		F_lista_indicador.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_proceso", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_indicador implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	public Integer id_indicador;
    public Integer id_tipo_identificador;
    public String nombre;
    public String sector;
    public String denominacion;
    public String valor_text;
    public String unidad;
    public String formula;
    public String lb_valor_text;
    public String fuente_indicador;
    public String responsable_indicador;
    public String meta_plan_text;
    public Double ponderacion;
    public Double programado;
    public Double ejecucion;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public List<Double> valor_programado;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public List<Double> valor_ejecutado;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public List<Double> gestion;
    public String evaluacion_indicador;
    
}
