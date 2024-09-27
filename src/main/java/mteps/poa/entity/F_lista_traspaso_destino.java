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
@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_traspaso_destino", procedureName = "mteps_plan.f_lista_traspaso_destino", resultClasses = {
		F_lista_traspaso_destino.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_destino", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_traspaso_destino {
	
	@Id
	public Integer id_plan ;
	public String unidad_funcional ;
	public String sigla;
	public String  aperturaprogramatica;
	public String codact; 
	public String actividad;
	public String codte;
	public String tareaespecifica;
	public String cod_partida; 
	public String partida; 
	public String fuentefinanciamiento;
	public Integer id_destino ;
	public Integer id_reformulacion ; 
	public Integer id_memoria_calculo;
	public String descripcion;
	public double cantidad;
	public double precio_unitario; 
	public double importe_total;
	public double saldo_memoria;
	public Integer usr_cre; 
	public Integer fid_origen;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_cre;
	public String observacion;
}
