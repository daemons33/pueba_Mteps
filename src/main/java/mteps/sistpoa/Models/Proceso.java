package mteps.sistpoa.Models;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Proceso implements Serializable {
  public Integer id_proceso;
  public Integer id_proceso_superior;
  public Integer id_plan;
  public Integer id_tipo_proceso;
  public String sigla;
  public String nombre;
  public String descripcion;
  public Integer codigo_orden;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
  public Date fecha_inicio;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
  public Date fecha_fin;
  public Integer id_estado;
  public Integer id_usuario;

  public String ruta;
  public Integer codigo_resp;
  public String mensaje_resp;

  public Proceso proceso;
  public TipoProceso tipoProceso;
  
  public String host;
  public Date fecha_sistema;
  
  public Integer id_gestion;
  
}