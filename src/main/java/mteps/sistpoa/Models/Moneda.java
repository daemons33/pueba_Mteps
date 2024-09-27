package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Moneda implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_moneda;
    public String sigla;
    public String nombre;
    public Integer id_estado;

}
