package mteps.sigec.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity

public class InfoCite {
	@Id
	public Integer id;
	public String cite;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm:ss",timezone="GMT-4")
	public Date fechaRegistro;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCite() {
		return cite;
	}
	public void setCite(String cite) {
		this.cite = cite;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
}
