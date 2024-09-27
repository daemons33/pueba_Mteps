package mteps.tramites.fondoCustodia.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name="mteps_tramites.f_lista_trm_visados",
procedureName = "mteps_tramites.f_lista_trm_visados",
resultClasses = {FListaTrmVisados.class},
parameters = {
		@StoredProcedureParameter(name = "v_fecha_ini", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_fecha_fin", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_codigo_tram", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_codigo_visado", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_nit", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_razon_social", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_nombre_trabajador", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_ci", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class FListaTrmVisados {
	
	
	public Integer idTramVisado;
	@Id
	public String codRegistro;
	public Integer idClasificadorTramite;
	public String nombreTramite;
	public Integer idEmpresa;
	public String nit;
	public String matriculaComercio;
	public String razonSocial;
	public String representanteLegal;
	public Integer idPersona;
	public String nombreTrabajador;
	public String nroDocumento;
	public String codVisado;
	public String observacion;
	public String transaccion;
	public String estado;
	public Integer usuarioCreacion;
	public String nombreUsuarioCreacion;
	public Integer usuarioModificacion;
	public String nombreUsuarioModificacion;
	public Date fechaCreacion;
	public Date fechaModificacion;
	 

}
