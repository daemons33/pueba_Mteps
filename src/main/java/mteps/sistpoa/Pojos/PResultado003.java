package mteps.sistpoa.Pojos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PResultado003 implements Serializable {

    public Integer interval_valor;
    public Date inicio;
    public Date fin;
    public Double valor_programado;
    public Double valor_ejecutado;
    public String etiqueta;
    public String nombre;
   

}
