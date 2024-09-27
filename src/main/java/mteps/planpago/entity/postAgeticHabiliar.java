package mteps.planpago.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class postAgeticHabiliar {
	@Id	
	 public Integer gestion;
	 public String periodo;
	 public String nit;
	 public String matriculaComercio;
	 public String tipoPlanilla;
	 public String modalidad;
	 @Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	 public Date fechaHabilitacion;
	 public Integer codigoSucursal;
	
}
