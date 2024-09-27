package mteps.sistpoa.Models;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class Presupuesto  implements Serializable {
    
  public Integer id_presupuesto; 
  public Integer id_unidad_organizacional;
  public Integer id_gestion;
  public Integer id_moneda;
  public Long valor_presupuesto; //numeric(19,2),
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
  public Date fecha_inicio;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
  public Date fecha_fin;
  public Integer id_estado;

  public Gestion gestion;
  public Moneda moneda;
  public UnidadOrganizacional unidadOrganizacional;

}
