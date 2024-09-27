package mteps.sistpoa.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PlanIndicador implements Serializable {

    public static final long serialVersionUID = 1L;

    public Integer id_plan;
    public String sigla_plan;
    public String nombre_plan;
    public String duracion_plan;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date inicio_gestion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date fin_gestion;
    public String estado_plan;
    public Integer nivel_proceso;
    public Integer id_proceso;
    public Integer id_proceso_superior;
    public Integer codigo_orden;
    public String ruta_proceso;
    public String sigla_proceso;
    public String procesos_superiores;
    public String nombre_proceso;
    public String descripcion_proceso;
    public Integer id_indicador;
    public String tipo_identificador;
    public String sector;
    public String denominacion;
    public String unidad;
    public String formula;
    public String lb_valor_text;
    public String meta_plan_text;
    public Double ponderacion;
    public String fuente_indicador;
    public String responsable_indicador;

    /*
    PARA AUDITORIA DE SISTEMAS
    public Integer usr_cre;
    public String host_cre;
    public Date fecha_cre;
    public Integer usr_mod;
    public Date fecha_mod;
    public String bd_usr;
    public String bd_host;
    public Date bd_timestamp;
    */

}