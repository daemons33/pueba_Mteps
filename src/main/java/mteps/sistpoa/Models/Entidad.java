package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Entidad implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_entidad;
    public String sigla;
    public String nombre;
    public String mision;
    public String vision;
    public Integer id_estado;

    public Integer codigo_resp;
    public String mensaje_resp;

}