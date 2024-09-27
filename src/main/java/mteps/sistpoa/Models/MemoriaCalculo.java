package mteps.sistpoa.Models;

import java.util.Date;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemoriaCalculo implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_memoria_calculo;
    public Integer id_proceso;
    public Integer id_partida_presupuesto;
    public String descripcion;
    public Double cantidad;
    public String unidad_medida;
    public Double precio_unitario;
    public String justificacion;
    public Double importe_total;
    public Integer id_estado;

    public Integer codigo_resp;
    public String mensaje_resp;

    public String cod_partida;
    public String partida;
    public String programa_mem_calc;
    public Integer id_usuario;
    public String host;
    public Date fecha_sistema;
    
    public Integer id_organismo_financiador;
    public Integer id_fuente_financiamiento;
    public String fuente_financiamiento;
    public Integer id_unidad_organizacional;
    public String act_func_consultoria;
    public String result_consultoria;
    public Integer duracion_consultoria_meses;
    public Integer nro_casos_consultoria;
    public String transaccion;
    public String estado;
    public String observacion;
    public Double saldo;
    public Integer id_tipo_pasaje;
    public Integer id_tipo_viatico;
    public Integer tipo;
    public Integer id_proceso_a;

}
