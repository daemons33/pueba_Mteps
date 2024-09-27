package mteps.planpago.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class consultaEstado {

    public String estado;
    public String departamento;
   
}