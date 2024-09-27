package mteps.poa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_traspaso_origen", procedureName = "mteps_plan.f_lista_traspaso_origen", resultClasses = {
		F_lista_traspaso_origen.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_origen", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_traspaso_origen {
	@Id
	public Integer id_plan;
	public String unidad_funcional;
	public String sigla;
	public String aperturaprogramatica;
	public String codact;
	public String actividad;
	public String codte;
	public String tareaespecifica;
	public String cod_partida;
	public String partida;
	public String fuentefinanciamiento;
	public Integer id_origen;
	public Integer id_reformulacion;
	public Integer id_memoria_calculo;
	public String descripcion;
	public double cantidad;
	public double precio_unitario;
	public double importe_total;
	public double saldo_memoria;
	public Integer usr_cre;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_cre;
	public String observacion;

}
