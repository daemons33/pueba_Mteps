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

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_doc_conciliacion", procedureName = "mteps_plan.f_lista_doc_conciliacion", resultClasses = {
		F_lista_doc_conciliacion.class }, parameters = {
				@StoredProcedureParameter(name = "i_gestion", type = String.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_doc_conciliacion {
	@Id
	public Integer id_doc_conciliacion;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_corte;
	public String file;
	public String estado; 
	public String transaccion;
	public String observacion; 
	public Integer id_creado_por; 
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_creado; 
	public Integer id_actualizado_por; 
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_actualizado; 
	public String gestion;
	public String nombre;
	public String creador;

}
