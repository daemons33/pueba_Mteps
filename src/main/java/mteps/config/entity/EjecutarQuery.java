package mteps.config.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;


@NamedStoredProcedureQuery(name="workflow.ejecutar_query",
procedureName = "workflow.ejecutar_query",
resultClasses = {EjecutarQuery.class},
parameters = {
		@StoredProcedureParameter(name = "query", type = String.class, mode = ParameterMode.IN)
})

@Entity
public class EjecutarQuery {
	@Id
	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	public Object var;

}
