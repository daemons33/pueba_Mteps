package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class UnidadOrganizacional implements Serializable {
    
    public Integer id_unidad_organizacional;
    public Integer id_entidad;
    public String sigla;
    public String nombre;
    public Integer id_estado;
    
    public Entidad entidad;

}
