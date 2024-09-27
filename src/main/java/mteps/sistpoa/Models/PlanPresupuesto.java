package mteps.sistpoa.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PlanPresupuesto implements Serializable {

    public Integer id_plan_presupuesto;
    public Integer id_plan;
    public Integer id_tipo_proceso;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date gestion_inicio_meta;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date gestion_fin_meta;
    public Integer id_seguimiento_interval;
    public Integer seguimiento_interval;
    public Integer id_estado;

}
