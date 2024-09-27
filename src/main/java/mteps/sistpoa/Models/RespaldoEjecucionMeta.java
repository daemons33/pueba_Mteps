package mteps.sistpoa.Models;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RespaldoEjecucionMeta implements Serializable {

    public static final long serialVersionUID = 1L;

    public Integer id_respaldo_ejecucion_meta;
    public Integer id_indicador_meta;
    public Integer id_indicador;
    public String nombre;
    public String descripcion;
    public Integer es_sigec;
    public String cite_ruta;
    public Integer id_estado;

    public Double valor_ejecutado_num;
    public Integer id_usuario;
    public String host;
    public Date fecha_sistema;

}
