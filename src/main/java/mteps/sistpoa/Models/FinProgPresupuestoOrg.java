package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;

@Data
public class FinProgPresupuestoOrg implements Serializable {

  public static final long serialVersionUID = 1L;
  public Integer id_prog_presupuesto_org;
  public Integer id_prog_presupuesto;
  public Integer id_unidad_organizacional;
  public Integer id_estado;

}
