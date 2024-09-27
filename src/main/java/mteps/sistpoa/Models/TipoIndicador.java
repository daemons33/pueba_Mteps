package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class TipoIndicador implements Serializable {
    
    public Integer id_tipo_identificador;
    public Integer nivel_proceso;
    public String sigla;
    public String nombre;
    public Integer valor_numerico;
    public Integer id_estado;
  
}
