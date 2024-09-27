package mteps.ovt.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntidadDepositos {
	@Id
	public String nroMovimiento;   
	public LocalDateTime fechaMovimiento;
	public String nit;
	public String razonSocial;
    public LocalDateTime fechaPago;
    public LocalDateTime fechaRegistro;
    public String descripcionCorta;
    public Double monto;
}
