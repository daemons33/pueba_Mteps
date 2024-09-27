package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class TipoProceso implements Serializable{

    public Integer id_tipo_proceso;
    public Integer id_tipo_proceso_superior;
    public Integer nivel_plan;
    public Integer nivel_proceso;
    public String sigla;
    public String nombre;
    public Integer id_estado;
}
