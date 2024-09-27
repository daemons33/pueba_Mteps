package mteps.sistpoa.Models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Foda implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_foda;
    public Integer id_gestion;
    public String fortaleza;
    public String oportunidad;
    public String debilidad;
    public String amenaza;
    public Integer id_estado;
    public Integer gestion;

    public String host;
    public Date fecha_sistema;
    public Integer codigo_resp;
    public String mensaje_resp;
    public Integer id_usuario;
}
