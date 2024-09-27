package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;


@NamedStoredProcedureQuery(name="mteps_plan.f_obtener_datos_usuario",
procedureName = "mteps_plan.f_obtener_datos_usuario",
resultClasses = {ObtenerDatosUsuario.class},
parameters = {
		@StoredProcedureParameter(name = "p_login", type = String.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObtenerDatosUsuario {
	@Id
	public Integer idUsuario;
	public String nombreCompleto;
	public String perDocIdentidad;
	public String itmCiudad;
	public String carNombre;
	public String pueNombre;
	public Integer idOrg;
	public String unidadFuncional;
	
	
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getPerDocIdentidad() {
		return perDocIdentidad;
	}
	public void setPerDocIdentidad(String perDocIdentidad) {
		this.perDocIdentidad = perDocIdentidad;
	}
	public String getItmCiudad() {
		return itmCiudad;
	}
	public void setItmCiudad(String itmCiudad) {
		this.itmCiudad = itmCiudad;
	}
	public String getCarNombre() {
		return carNombre;
	}
	public void setCarNombre(String carNombre) {
		this.carNombre = carNombre;
	}
	public String getPueNombre() {
		return pueNombre;
	}
	public void setPueNombre(String pueNombre) {
		this.pueNombre = pueNombre;
	}
	public Integer getIdOrg() {
		return idOrg;
	}
	public void setIdOrg(Integer idOrg) {
		this.idOrg = idOrg;
	}
	public String getUnidadFuncional() {
		return unidadFuncional;
	}
	public void setUnidadFuncional(String unidadFuncional) {
		this.unidadFuncional = unidadFuncional;
	}

	
}