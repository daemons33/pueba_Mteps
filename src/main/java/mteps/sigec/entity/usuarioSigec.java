package mteps.sigec.entity;

import javax.persistence.Id;

public class usuarioSigec {
	@Id
	public Integer id; 
	public Integer superior;
	public Integer idOficina;
	public Integer dependencia;
public String username;
public String password ; 
public String nombre ;
public Integer lastLogin;
public String mosca;
public String cargo;
public String email;
public Integer logins;
public Integer fechaCreacion;
public Integer habilitado;
public Integer nivel;
public String genero;
public Integer prioridad;
public Integer idEntidad;
public Integer supers;
public Integer cedulaIdentidad;
public String expedido;
public String theme;
public Integer citeDespacho;
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Integer getSuperior() {
	return superior;
}
public void setSuperior(Integer superior) {
	this.superior = superior;
}
public Integer getIdOficina() {
	return idOficina;
}
public void setIdOficina(Integer idOficina) {
	this.idOficina = idOficina;
}
public Integer getDependencia() {
	return dependencia;
}
public void setDependencia(Integer dependencia) {
	this.dependencia = dependencia;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public Integer getLastLogin() {
	return lastLogin;
}
public void setLastLogin(Integer lastLogin) {
	this.lastLogin = lastLogin;
}
public String getMosca() {
	return mosca;
}
public void setMosca(String mosca) {
	this.mosca = mosca;
}
public String getCargo() {
	return cargo;
}
public void setCargo(String cargo) {
	this.cargo = cargo;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public Integer getLogins() {
	return logins;
}
public void setLogins(Integer logins) {
	this.logins = logins;
}
public Integer getFechaCreacion() {
	return fechaCreacion;
}
public void setFechaCreacion(Integer fechaCreacion) {
	this.fechaCreacion = fechaCreacion;
}
public Integer getHabilitado() {
	return habilitado;
}
public void setHabilitado(Integer habilitado) {
	this.habilitado = habilitado;
}
public Integer getNivel() {
	return nivel;
}
public void setNivel(Integer nivel) {
	this.nivel = nivel;
}
public String getGenero() {
	return genero;
}
public void setGenero(String genero) {
	this.genero = genero;
}
public Integer getPrioridad() {
	return prioridad;
}
public void setPrioridad(Integer prioridad) {
	this.prioridad = prioridad;
}
public Integer getIdEntidad() {
	return idEntidad;
}
public void setIdEntidad(Integer idEntidad) {
	this.idEntidad = idEntidad;
}
public Integer getSupers() {
	return supers;
}
public void setSupers(Integer supers) {
	this.supers = supers;
}
public Integer getCedulaIdentidad() {
	return cedulaIdentidad;
}
public void setCedulaIdentidad(Integer cedulaIdentidad) {
	this.cedulaIdentidad = cedulaIdentidad;
}
public String getExpedido() {
	return expedido;
}
public void setExpedido(String expedido) {
	this.expedido = expedido;
}
public String getTheme() {
	return theme;
}
public void setTheme(String theme) {
	this.theme = theme;
}
public Integer getCiteDespacho() {
	return citeDespacho;
}
public void setCiteDespacho(Integer citeDespacho) {
	this.citeDespacho = citeDespacho;
}


}
