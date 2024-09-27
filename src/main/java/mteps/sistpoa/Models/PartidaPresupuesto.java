package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PartidaPresupuesto implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_partida_presupuesto;
    public Integer id_partida_presupuesto_sup;
    public String cod_partida;
    public String partida;
    public Double valor_presupuesto;
    public String sigla;
    public String ruta;

}
