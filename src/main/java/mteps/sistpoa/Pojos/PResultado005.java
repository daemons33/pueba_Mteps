package mteps.sistpoa.Pojos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PResultado005 implements Serializable {

    public Integer interval_valor;
    public Date inicio;
    public Date fin;
    public Double valor_programado;
    public Double valor_ejecutado;
    public Integer id_sigec;
    public String cite_sigec;

}
