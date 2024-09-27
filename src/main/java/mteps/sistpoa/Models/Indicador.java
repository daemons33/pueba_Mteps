package mteps.sistpoa.Models;

import java.io.Serializable;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class Indicador implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_indicador;
    public Integer id_proceso;
    public Integer id_tipo_identificador;
    public String descripcion_unidad;

    public String valor_text;
    public String fuente_indicador;
    public Integer id_estado;
    public String nombre;
    public String sector;
    public String denominacion;
    public Double valor_num;
    public String unidad;
    public String formula;
    public Double lb_valor_num;
    public String lb_valor_text;
    public Double meta_anterior;  //est_meta_plan_ant_num
    public Double meta_actual;    //meta_plan_num
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public List<Double> valor_programado;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public List<Double> valor_ejecutado;
    @Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
    public List<Double> gestion;
    public String meta_plan_text;
    public Double ponderacion;
    public Double programado;
    public Double ejecucion;
    
    public String responsable_indicador;
    
   public String seguimiento_meta;  //json
    public String seguimiento_presup; //json
    public String seguimiento_presupuesto; //json
    public Integer id_usuario;
    public String host;
    public Date fecha_sistema;
    public Integer editar_poa;

    public Integer codigo_resp;
    public String mensaje_resp;
    public String evaluacion_indicador;

    public List<IndicadorMeta> indicadorMeta;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", locale = "es-BO", timezone = "America/La_Paz")
    public LocalDateTime id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
    public LocalDateTime fecha_cre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
    public LocalDateTime fecha_mod;

    public Integer nro_reformulacion;
}
