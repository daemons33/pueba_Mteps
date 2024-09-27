package mteps.sistpoa.Pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class PResultado007 implements Serializable {


    public String sigla;
    public String nombre;
    public Integer id_accion_poa_org;
    public Integer id_estado;
    public Integer id_unidad_organizacional;

}
