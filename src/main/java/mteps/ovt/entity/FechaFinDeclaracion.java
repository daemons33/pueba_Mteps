package mteps.ovt.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FechaFinDeclaracion {
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaFinDeclaracion;

	public Date getFechaFinDeclaracion() {
		return fechaFinDeclaracion;
	}

	public void setFechaFinDeclaracion(Date fechaFinDeclaracion) {
		this.fechaFinDeclaracion = fechaFinDeclaracion;
	}



}
