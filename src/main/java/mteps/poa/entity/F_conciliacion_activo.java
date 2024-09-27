package mteps.poa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@NamedStoredProcedureQuery(name = "mteps_plan.f_conciliacion_activo", procedureName = "mteps_plan.f_conciliacion_activo", resultClasses = {
		F_conciliacion_activo.class })
@Entity
public class F_conciliacion_activo {

	@Id
	public Integer id_doc_conciliacion;
	public String nombre;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	public Date fecha_corte;
}
