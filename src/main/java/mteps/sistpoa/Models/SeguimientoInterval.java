package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class SeguimientoInterval implements Serializable {
    public Integer id_seguimiento_interval;
    public String nombre;
    public String denominativo;
    public String tipo_valor;
    public Integer id_estado;
}
