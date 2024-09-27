package mteps.sistpoa.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PlanMeta implements Serializable {

    public Integer id_plan_meta;
    public Integer id_plan;
    public Integer id_tipo_proceso;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date gestion_inicio_meta;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date gestion_fin_meta;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date gestion_linea_base;
    public Integer id_seguimiento_interval;
    public Integer seguimiento_interval;
    public Integer id_estado;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_linea_base;
    public Integer id_usuario;

    public String host;
    public Date fecha_sistema;

    public Integer codigo_resp;
    public String mensaje_resp;

}
