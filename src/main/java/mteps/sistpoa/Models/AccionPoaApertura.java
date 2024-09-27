package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccionPoaApertura implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_accion_poa_ape;
    public Integer id_apertura_organizacion;
    public Integer id_plan;
    public Integer id_proceso_superior;
    public Integer id_proceso;
    public Integer id_apertura;
    public Integer id_estado;
    public String sigla;
    public String nombre;
    public String descripcion;

}