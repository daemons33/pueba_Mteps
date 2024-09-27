package mteps.sistpoa.Models;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class PlanAnexo implements Serializable{

   public static final long serialVersionUID = 1L;
   public Integer id_plan_anexo;
   public Integer id_plan;
   public String plan_reporte;
   public String tag_version;
   public String descripcion_version;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
   public Date fecha_generacion;
   public Integer id_estado;
   public Plan plan;
    
}
