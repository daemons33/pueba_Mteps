package mteps.poa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_apertura_organizacion", procedureName = "mteps_plan.f_lista_apertura_organizacion", resultClasses = {
        F_apertura_organizacion.class },parameters = {})
@Entity
public class F_apertura_organizacion {
    @Id
    public Integer idAperturaOrganizacion;
    public Integer idApertura;
    public String sigla;
    public String nombre;
    public Integer idUnidadOrganizacional;
    public String unidadFuncional;
    public Integer idEstado;
    public Integer ejecucionAcc;
    public Integer ejecucionOp;
    public Integer ejecucionAct;
    public Integer ejecucionTe;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, timezone="GMT-4")
    public Date fecIniEjecucion;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, timezone="GMT-4")
    public Date fecFinEjecucion;
}
