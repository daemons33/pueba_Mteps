package mteps.sistpoa.Pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PPoa implements Serializable {


    public Integer id_plan;
    public Integer id_plan_superior;
    public Integer id_tipo_plan;
    public String sigla_tipo;
    public String nombre_tipo;
    public Integer id_unidad_organizacional;
    public String sigla_plan;
    public String nombre_plan;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date inicio_gestion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date fin_gestion;
    public Integer id_gestion;
    public Integer valor_gestion;
    public Integer id_plan_a;
    public Integer editar_poa;
    public String evaluacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_evaluacion;
    public Integer nro_reformulacion;

}
