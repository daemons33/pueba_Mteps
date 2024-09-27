package mteps.comitemixto.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;


@NamedStoredProcedureQuery(name="mteps_comites.f_obtener_capacitadoscomites",
procedureName = "mteps_comites.f_obtener_capacitadoscomites",
resultClasses = {ListaComiteMixtos.class},
parameters = {
		@StoredProcedureParameter(name = "p_json_pp", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class ListaComiteMixtos {
	
	@Id
	public Integer id;
	public Date fechaCreacion;
    public String usuarioCreacion;
	public Date fechaModificacion;
    public String usuarioModificacion;
    public String nombre;
    public String apellidos;
    public String ci;
    public Double celular;
    public String correo;
    public String empresa;
    public String cargo;
	public Date fechaCapacitacion;
    public String jefatura;
    public String estado;
    public Boolean origenLs;
}
