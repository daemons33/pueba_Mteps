package mteps.sistpoa.Pojos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PResultado004 implements Serializable {

    public Integer interval_valor;
    public Date inicio;
    public Date fin;
    public Double inversion_publica;
    public Double gasto_corriente;

}
