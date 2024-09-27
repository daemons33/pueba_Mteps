package mteps.sistpoa.Pojos;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PResultado002 implements Serializable {

    public Integer interval_valor;
    public Date inicio;
    public Date fin;
    public Double cantidad;
    public Double costo;
    public String etiqueta;

}
