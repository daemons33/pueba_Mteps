package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class TipoPlan implements Serializable {
   public Integer id_tipo_plan;
  public Integer nivel_plan;
  public String sigla;
  public String nombre;
  public String categoria;
  public Integer id_estado;
}
