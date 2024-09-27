package mteps.planpago.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class requestPeriodoRezagado {
	
	public Number gestion;
	public String periodo;
	public String nit;
	public String MatriculaComercio;
	public String tipoPlanilla;
	public String modalidad;
	public String fechaHabilitacion;
	public Number nroSucursal;
	public String motivo;

}
