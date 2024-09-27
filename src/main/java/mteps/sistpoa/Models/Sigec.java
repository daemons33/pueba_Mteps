package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Sigec implements Serializable{

    public Boolean correcto;
    public String notificacion;
    public Integer codigoResultado;
    public String datoAdicional;

}
