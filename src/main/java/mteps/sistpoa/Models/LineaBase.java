package mteps.sistpoa.Models;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class LineaBase implements Serializable {

  public static final long serialVersionUID = 1L;
  public Integer id_linea_base;
  public Integer id_tipo_linea_base;
  public String sigla;
  public String denominativo;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
  public Date fecha_inicio;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
  public Date fecha_fin;
  public Integer valor_num;
  public String valor_text;
  public Integer id_estado;

  public TipoLineaBase tipoLineaBase;
    
}
