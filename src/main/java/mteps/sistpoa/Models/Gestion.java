package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Gestion implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_gestion;
    public Integer valor_gestion;
    public Integer id_estado;

}
