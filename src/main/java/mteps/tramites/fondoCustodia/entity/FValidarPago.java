package mteps.tramites.fondoCustodia.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;


@NamedStoredProcedureQuery(name="mteps_tramites.f_validar_pago",
procedureName = "mteps_tramites.f_validar_pago",
resultClasses = {FValidarPago.class},
parameters = {
		@StoredProcedureParameter(name = "v_tipo", type = Integer.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_nro_movimiento", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_fecha_movimiento", type = String.class, mode = ParameterMode.IN),
		@StoredProcedureParameter(name = "v_glosa", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class FValidarPago {

	@Id
	public Integer idMovimiento;
	public Integer idEstado;
	public Integer idFc;
	public Double monto;
	public Boolean uninet;
	public String nroMovimiento;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy",timezone = "GMT-4")
	public Date fechaMovimiento;
	public String detalleUninet;
	public Integer movimiento;
	public Boolean sigep;
	public String operacionSigep;
	public String detalleSigep;
	
	public String cuentaTransferenciaSigep;
	public Integer idCuentaBancaria;
	public Boolean repositorio;
	public Integer idRepositorioDocumento;
	
}
