package mteps.planpago.entity;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class datosReporte {
public double sumaMulta = 0;
public double sumaAcuenta = 0;
public double sumaSaldo = 0;
public double sumaPorPagar = 0;
@Temporal(TemporalType.DATE)
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
public Date ultimoPago;
}
