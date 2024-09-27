package mteps.planpago.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import com.fasterxml.jackson.annotation.JsonInclude;


@NamedStoredProcedureQuery(name="workflow.f_obtener_lista_transacciones",
procedureName = "workflow.f_obtener_lista_transacciones",
resultClasses = {listaTransacciones.class},
parameters = {
		@StoredProcedureParameter(name = "p_login", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "p_tabla", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "p_estado", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "p_modulo", type = String.class, mode = ParameterMode.IN)
})

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class listaTransacciones {
	
	public String descripcion_rol;
	public String rol;
	@Id
	public String transaccion;
	public String operacion;
	public String descripcionTransaccion;
	public String img;
	public String getDescripcion_rol() {
		return descripcion_rol;
	}
	public void setDescripcion_rol(String descripcion_rol) {
		this.descripcion_rol = descripcion_rol;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	public String getDescripcionTransaccion() {
		return descripcionTransaccion;
	}
	public void setDescripcionTransaccion(String descripcionTransaccion) {
		this.descripcionTransaccion = descripcionTransaccion;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
}
