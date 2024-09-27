package mteps.sistpoa.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SolicitudDetalle implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_detalle;
    public Integer id_solicitud;
    public String codigo;
    public String detalle_descripcion;
    public Double cantidad;
    public Double precio_referencial;
    public Double total_precio_referencial;
    public Double saldo_certificacion;
    public Integer id_usuario;
    public Integer id_estado;

    public String partida;
    public String fuente;
    public Integer id_memoria_calculo;
    public double monto_revertido;
}