package mteps.sistpoa.Models;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProcesoPlan implements Serializable {

  public Integer id_plan;
  public String sigla_plan;
  public String nombre_plan;
  public String duracion_plan;
  public String estado_plan;
  public Integer nivel_proceso;
  public Integer codigo_orden;
  public Integer id_proceso;
  public Integer id_proceso_superior;
  public String ruta_proceso;
  public String sigla_proceso;
  public String procesos_superiores;
  public String nombre_proceso;
  public String descripcion_proceso;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
  public Date inicio_gestion;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
  public Date fin_gestion;

  public String evaluacion;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
  public Date fecha_evaluacion;

  public Integer codigo_resp;
  public String mensaje_resp;
  public Integer editar_poa;
  public Integer nro_reformulacion;

  /*public ProcesoPlan(Integer codigo_resp, String mensaje_resp){
       this.codigo_resp = codigo_resp;
       this.mensaje_resp = mensaje_resp;
  }*/

}
