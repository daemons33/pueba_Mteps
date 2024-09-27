package mteps.sistpoa.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class IndicadorMeta implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_indicador_meta;
    public Integer id_plan_meta;
    public Integer id_plan_indicador_meta;
    public Integer id_indicador;
    public Integer interval_valor;
    public Double  valor_programado_num;
    public Double  valor_ejecutado_num;
    public Integer id_sigec;
    public String  cite_sigec;
    public Integer id_estado;
    public String nombre;
    public String desc_intervalo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date inicio;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date fin;

    public String evaluacion;
    public String revision;
    public String observacion;
    public String estado_meta;
    public String medidas_correctivas;
    public String justificacion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", locale = "es-BO", timezone = "America/La_Paz")
    public LocalDateTime id;

    public Integer usr_cre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_cre;

}