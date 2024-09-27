package mteps.sistpoa.Models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Usuario implements Serializable {

    public Integer id_usuario;
    public Integer cod_unidad_ref;
    public String desc_unidad_ref;
    public String nombre;
    public String login;
    public String email;
    public Integer id_estado;
    public String estado;
    public String roles;
    public Date fecha_cre;
    public Date fecha_mod;
    public String imagen_firma;
}
