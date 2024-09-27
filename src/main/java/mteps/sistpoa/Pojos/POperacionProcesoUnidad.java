package mteps.sistpoa.Pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class POperacionProcesoUnidad implements Serializable {

    public Integer id_operacion_poa_org;
    public Integer id_proceso;
    public Integer id_unidad_organizacional;
    public Integer id_apertura;
    public String sigla_unidad_organizacional;
    public String nombre_unidad_organizacional;
    public String sigla_proceso;
    public String nombre_proceso;
    public String descripcion_proceso;
    public String id_estado;

}
