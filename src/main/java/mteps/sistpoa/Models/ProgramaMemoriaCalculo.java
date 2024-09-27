package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProgramaMemoriaCalculo implements Serializable {

    public Integer id_programa_mem_calculo;
    public Integer id_memoria_calculo;
    public String desc_mes_programa;
    public Integer mes_programa;
    public Date inicio;
    public Date fin;
    public Double cantidad;
    public Double costo;

}
