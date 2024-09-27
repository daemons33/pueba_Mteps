package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;

@Data
public class FinProgPresupuesto implements Serializable {

  public static final long serialVersionUID = 1L;
  public Integer id_prog_presupuesto;
  public Integer id_prog_presupuesto_superior;
  public String codigo;
  public String ruta;
  public String descripcion;
  public Double ponderacion;

}
