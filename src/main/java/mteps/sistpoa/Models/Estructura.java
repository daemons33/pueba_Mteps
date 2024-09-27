package mteps.sistpoa.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Estructura implements Serializable{

    public static final long serialVersionUID = 1L;
    public String sigla_plan;
    public String plan;
    public String plan_arbol_acciones;
}
