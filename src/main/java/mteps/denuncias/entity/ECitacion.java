package mteps.denuncias.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class ECitacion {

	@Id
	public Integer idCitacion;
	public Integer idTipo;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",timezone="GMT-4")
	public Date fecha;
	
	
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss",timezone="GMT-4")
	public Date hora;

//	 APLICAR EN PROD
//	  @Temporal(TemporalType.TIMESTAMP)
//	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss",timezone="GMT-4")
//	public Date hora;
	 
}
