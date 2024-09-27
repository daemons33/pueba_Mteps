package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;

@Data
public class AperturaOrganizacion implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_apertura_organizacion;
    public Integer id_apertura;
    public Integer id_apertura_superior;
    public Integer id_estado;
}