package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class TipoLineaBase implements Serializable {

    public Integer id_tipo_linea_base;
    public Integer nivel_plan;
    public String sigla;
    public String nombre;
    public Integer gestion_inicio;
    public Integer gestion_fin;
    public Integer id_estado;

}
