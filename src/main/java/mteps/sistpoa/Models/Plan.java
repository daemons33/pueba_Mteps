package mteps.sistpoa.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

public @Data class Plan implements Serializable {

	public static final long serialVersionUID = 1L;
    //private Integer nivel_plan;
	public Integer id_plan;
	public Integer id_plan_superior;
	public Integer id_tipo_plan;
	public Integer id_unidad_organizacional;
	public Integer id_gestion;
	public String sigla;
	public String nombre;
	public Integer id_estado;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date inicio_gestion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date fin_gestion;
    public String rango_gestion;
    public Integer id_usuario;
    public Integer id_proceso;

    public String mensaje_resp;
    public Integer codigo_resp;
    public String mensaje_estado;
    public String host;
    public Date fecha_sistema;
    public Integer editar_poa;

    public Plan plan;
    public List<Proceso> proceso;
    public TipoPlan tipoPlan;
    public UnidadOrganizacional unidadOrganizacional;
    public Integer id_plan_a;

    public String evaluacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-BO", timezone = "America/La_Paz")
    public Date fecha_evaluacion;
    public Integer nro_reformulacion;

    

    /*
    PARA AUDITORIA DE SISTEMAS
    private Integer usr_cre;
    private String host_cre;
    private Date fecha_cre;
    private Integer usr_mod;
    private Date fecha_mod;
    private String bd_usr;
    private String bd_host;
    private Date bd_timestamp;
    */

}