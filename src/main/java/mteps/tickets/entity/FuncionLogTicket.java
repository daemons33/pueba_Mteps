package mteps.tickets.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;


@NamedStoredProcedureQuery(name="mteps_tickets.f_obtener_log_ticket",
procedureName = "mteps_tickets.f_obtener_log_ticket",
resultClasses = {FuncionLogTicket.class},
parameters = {
		@StoredProcedureParameter(name = "v_id_ticket", type = Integer.class, mode = ParameterMode.IN)
})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionLogTicket {
	@Id
	public Integer idTkLog;
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb", name = "jsonData")
	public PojoLogTicket jsonData;


	public Integer getIdTkLog() {
		return idTkLog;
	}

	public void setIdTkLog(Integer idTkLog) {
		this.idTkLog = idTkLog;
	}

	public PojoLogTicket getJsonData() {
		return jsonData;
	}

	public void setJsonData(PojoLogTicket jsonData) {
		this.jsonData = jsonData;
	}

	
}
