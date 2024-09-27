package mteps.sistpoa.Pojos;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class PMatriz implements Serializable {

    public static final long serialVersionUID = 1L;

    public Integer id_proceso;
    public Integer id_tipo_identificador;
    public String sector;
    public String denominacion;
    public Integer id_indicador_meta;
    public Integer id_indicador;
    public Integer id_plan_meta;
    public Integer mes_inicio;
    public Integer anio_inicio;
    public Integer mes_fin;
    public Integer anio_fin;
    public Integer valor_programado_num;
    public Integer valor_ejecutado_num;

    public Integer interval_valor;

    public Date inicio;
    public Date fin;

    public Double valor_programado;
    public Double valor_ejecutado;

    public Double gasto_corriente;
    public Double inversion_publica;

    public Integer id_sigec;
    public String cite_sigec;

    public Double cantidad;
    public Double costo;
}
