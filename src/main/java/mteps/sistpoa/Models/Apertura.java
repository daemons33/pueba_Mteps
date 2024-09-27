package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Apertura implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_apertura;
    public Integer id_apertura_superior;
    public Integer id_gestion;
    public String cod_entidad;
    public String cod_programa;
    public String cod_proyecto;
    public String cod_actividad;
    public String descripcion;
    public Double ponderacion;
    public Integer id_estado;
}