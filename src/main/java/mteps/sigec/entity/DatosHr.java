package mteps.sigec.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DatosHr {
@Id
String login;
Integer idDestinatario;
Integer gestion;
String nombreTramite;
String proveido;
Integer fojas;
String codigoTramite;
public String getLogin() {
	return login;
}
public void setLogin(String login) {
	this.login = login;
}
public Integer getIdDestinatario() {
	return idDestinatario;
}
public void setIdDestinatario(Integer idDestinatario) {
	this.idDestinatario = idDestinatario;
}
public Integer getGestion() {
	return gestion;
}
public void setGestion(Integer gestion) {
	this.gestion = gestion;
}

public String getNombreTramite() {
	return nombreTramite;
}
public void setNombreTramite(String nombreTramite) {
	this.nombreTramite = nombreTramite;
}

public String getProveido() {
	return proveido;
}
public void setProveido(String proveido) {
	this.proveido = proveido;
}
public Integer getFojas() {
	return fojas;
}
public void setFojas(Integer fojas) {
	this.fojas = fojas;
}
public String getCodigoTramite() {
	return codigoTramite;
}
public void setCodigoTramite(String codigoTramite) {
	this.codigoTramite = codigoTramite;
}

}
