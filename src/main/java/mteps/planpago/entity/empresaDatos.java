package mteps.planpago.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;


import com.fasterxml.jackson.annotation.JsonInclude;

@NamedStoredProcedureQuery(name="mteps_pagos.f_obtener_datos_empresa",
							procedureName = "mteps_pagos.f_obtener_datos_empresa",
							resultClasses = {empresaDatos.class},
							parameters = {
									@StoredProcedureParameter(name = "p_json_pp", type = String.class, mode = ParameterMode.IN),
									@StoredProcedureParameter(name = "var_nit",type = String.class, mode = ParameterMode.IN),
									@StoredProcedureParameter(name = "var_matricula",type = String.class, mode = ParameterMode.IN)
							})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class empresaDatos {
	
	@Id
	public String codigoempresa;
	public String nit;
	public String matriculacomercio;
	public String razonsocial;
	    	
	public String tipoempresa;
	
	public String tiposociedad;
	public String categorialucro;
	public String cantidadsucursales;
	public String correoelectronico;

	public String getCodigoempresa() {
		return codigoempresa;
	}
	public void setCodigoempresa(String codigoempresa) {
		this.codigoempresa = codigoempresa;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getMatriculacomercio() {
		return matriculacomercio;
	}
	public void setMatriculacomercio(String matriculacomercio) {
		this.matriculacomercio = matriculacomercio;
	}
	public String getRazonsocial() {
		return razonsocial;
	}
	public void setRazonsocial(String razonsocial) {
		this.razonsocial = razonsocial;
	}
	public String getTipoempresa() {
		return tipoempresa;
	}
	public void setTipoempresa(String tipoempresa) {
		this.tipoempresa = tipoempresa;
	}
	public String getTiposociedad() {
		return tiposociedad;
	}
	public void setTiposociedad(String tiposociedad) {
		this.tiposociedad = tiposociedad;
	}
	public String getCategorialucro() {
		return categorialucro;
	}
	public void setCategorialucro(String categorialucro) {
		this.categorialucro = categorialucro;
	}
	public String getCantidadsucursales() {
		return cantidadsucursales;
	}
	public void setCantidadsucursales(String cantidadsucursales) {
		this.cantidadsucursales = cantidadsucursales;
	}
	public String getCorreoelectronico() {
		return correoelectronico;
	}
	public void setCorreoelectronico(String correoelectronico) {
		this.correoelectronico = correoelectronico;
	}
	
	

}
