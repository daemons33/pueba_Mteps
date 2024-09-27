package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class RolUsuarioUnidad implements Serializable {

    public Integer id_rol_usuario_unidad;
    public Integer id_usuario;
    public Integer id_rol;
    public Integer id_unidad_organizacional;
    public Integer id_estado;

    public Rol rol;
    public UnidadOrganizacional unidadOrganizacional;
    public Usuario usuario;
    
}
