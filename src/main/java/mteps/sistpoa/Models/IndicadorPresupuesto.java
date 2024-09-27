package mteps.sistpoa.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class IndicadorPresupuesto implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_indicador_presupuesto;
    public Integer id_plan_indicador_presupuesto;
    public Integer id_indicador;
    public Integer interval_valor;
    public BigDecimal valor_inv_publica;
    public BigDecimal valor_gasto_corriente;
    public Integer id_estado;
    public Integer id_plan_presupuesto;
    public String nombre;
    public String desc_intervalo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date inicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date fin;

}