package mteps.planpago.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class listaSucursalPlanillaPendiente {
	
 public String codigoSucursal;
 public String nombre;
 public String departamento;
 
public String getCodigoSucursal() {
	return codigoSucursal;
}
public void setCodigoSucursal(String codigoSucursal) {
	this.codigoSucursal = codigoSucursal;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getDepartamento() {
	return departamento;
}
public void setDepartamento(String departamento) {
	this.departamento = departamento;
}


 

}
