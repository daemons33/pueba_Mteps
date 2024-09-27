package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccionPoaOrg implements Serializable{

    public static final long serialVersionUID = 1L;
    public Integer id_accion_poa_org;
    public Integer id_proceso;
    public Integer id_unidad_organizacional;
    public Integer id_estado;

    public String uniorg_accion_poa;
    public Integer id_usuario;
    public String host;
    public Date fecha_sistema;

}
