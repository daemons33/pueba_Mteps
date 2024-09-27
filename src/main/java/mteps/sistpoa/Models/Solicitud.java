package mteps.sistpoa.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.hibernate.annotations.Type;

@Data
public class Solicitud implements Serializable {

    public static final long serialVersionUID = 1L;
    public Integer id_solicitud;
    public Integer id_proceso;
    public String codigo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_solicitud;
    public String descripcion_procesos;
    public String estado_solicitud;
    public Integer id_estado;
    public Integer usr_cre;
    public String host_cre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_cre;
    public Integer usr_mod;
    public String host_mod;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_mod;
    public String bd_usr;
    public String bd_host;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
    public Date bd_timestamp;
    public Integer id_usuario;
    public String observacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_aprobacion;

    public String solicitud_detalle;
    public String justificacion;
    public String estado_anterior;
    public String transaccion;
    public String estado;
    public String nombreUsuario;
    public String org_unidad_funcional;
    public String org_unidad_funcional2;
    public Integer usr_firma;
    public double monto_revertido;
    public String gestion;
}