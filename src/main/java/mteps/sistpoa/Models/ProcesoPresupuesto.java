package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProcesoPresupuesto implements Serializable{
    
  public Integer id_proceso_presupuesto;
  public Integer id_presupuesto;
  public Long planificado; //numeric(19,2),
  public Long ejecutado; //numeric(19,2),
  public Integer id_estado;

  public Presupuesto presupuesto;
  
}
