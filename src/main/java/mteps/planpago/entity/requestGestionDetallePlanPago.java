package mteps.planpago.entity;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class requestGestionDetallePlanPago {
public double band;
public Integer cpla_id;
public String cdpl_tipo;
public Integer cdpl_nro;
public String cdpl_concepto;

@Temporal(TemporalType.DATE)
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")

public Date cdpl_fec_limite;
public double cdpl_monto;
public double cdpl_acuenta;
public double cdpl_saldo;
@Temporal(TemporalType.DATE)
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
public Date cdpl_fec_comprobante;
public String cdpl_nro_comprobante;
public String cdpl_estado;
@Temporal(TemporalType.DATE)
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
public Date caud_fec_creacion;
@Temporal(TemporalType.DATE)
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
public Date caud_fec_modificacion;
public String cusu_id_creacion;
public String cusu_id_modificacion;
public double getBand() {
	return band;
}
public void setBand(double band) {
	this.band = band;
}
public Integer getCpla_id() {
	return cpla_id;
}
public void setCpla_id(Integer cpla_id) {
	this.cpla_id = cpla_id;
}
public String getCdpl_tipo() {
	return cdpl_tipo;
}
public void setCdpl_tipo(String cdpl_tipo) {
	this.cdpl_tipo = cdpl_tipo;
}
public Integer getCdpl_nro() {
	return cdpl_nro;
}
public void setCdpl_nro(Integer cdpl_nro) {
	this.cdpl_nro = cdpl_nro;
}
public String getCdpl_concepto() {
	return cdpl_concepto;
}
public void setCdpl_concepto(String cdpl_concepto) {
	this.cdpl_concepto = cdpl_concepto;
}

public double getCdpl_monto() {
	return cdpl_monto;
}
public void setCdpl_monto(double cdpl_monto) {
	this.cdpl_monto = cdpl_monto;
}
public double getCdpl_acuenta() {
	return cdpl_acuenta;
}
public void setCdpl_acuenta(double cdpl_acuenta) {
	this.cdpl_acuenta = cdpl_acuenta;
}
public double getCdpl_saldo() {
	return cdpl_saldo;
}
public void setCdpl_saldo(double cdpl_saldo) {
	this.cdpl_saldo = cdpl_saldo;
}

public String getCdpl_nro_comprobante() {
	return cdpl_nro_comprobante;
}
public void setCdpl_nro_comprobante(String cdpl_nro_comprobante) {
	this.cdpl_nro_comprobante = cdpl_nro_comprobante;
}
public String getCdpl_estado() {
	return cdpl_estado;
}
public void setCdpl_estado(String cdpl_estado) {
	this.cdpl_estado = cdpl_estado;
}

public Date getCdpl_fec_limite() {
	return cdpl_fec_limite;
}
public void setCdpl_fec_limite(Date cdpl_fec_limite) {
	this.cdpl_fec_limite = cdpl_fec_limite;
}
public Date getCdpl_fec_comprobante() {
	return cdpl_fec_comprobante;
}
public void setCdpl_fec_comprobante(Date cdpl_fec_comprobante) {
	this.cdpl_fec_comprobante = cdpl_fec_comprobante;
}
public Date getCaud_fec_creacion() {
	return caud_fec_creacion;
}
public void setCaud_fec_creacion(Date caud_fec_creacion) {
	this.caud_fec_creacion = caud_fec_creacion;
}
public Date getCaud_fec_modificacion() {
	return caud_fec_modificacion;
}
public void setCaud_fec_modificacion(Date caud_fec_modificacion) {
	this.caud_fec_modificacion = caud_fec_modificacion;
}
public String getCusu_id_creacion() {
	return cusu_id_creacion;
}
public void setCusu_id_creacion(String cusu_id_creacion) {
	this.cusu_id_creacion = cusu_id_creacion;
}
public String getCusu_id_modificacion() {
	return cusu_id_modificacion;
}
public void setCusu_id_modificacion(String cusu_id_modificacion) {
	this.cusu_id_modificacion = cusu_id_modificacion;
}

}
