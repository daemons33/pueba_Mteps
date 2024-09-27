package mteps.tramites.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name="mteps_tramites.f_obtener_depositos",
procedureName = "mteps_tramites.f_obtener_depositos",
resultClasses = {ObtieneDepositos.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_pago", type = Integer.class, mode = ParameterMode.IN)
})
@Entity
public class ObtieneDepositos {
	@Id
	public Integer id_deposito;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_creacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_modificacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_deposito ;
	public String nro_deposito ;
	public Integer usuario_creacion ;
	public Integer usuario_modificacion ;
	public Double monto;
	public String transaccion; 
	public String observacion ;
	public String estado ;
	public Integer id_pago ;
	public Integer id_tipo_pago ;
	public String concepto ;
	public Integer idcuentafiscal ;
	public String sigla_cuenta ;
	public String cuenta_recaudadora ;
	public String sigla_tipo ;
	public String tipo_pago;
}
