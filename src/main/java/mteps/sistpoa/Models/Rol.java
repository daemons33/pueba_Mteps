package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Rol implements Serializable {

    public Integer id_rol;
    public String nombre;
    public String descripcion;
    public Integer id_estado;
}
