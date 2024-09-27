package mteps.tramites.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;

@NamedStoredProcedureQuery(name="mteps_tramites.f_lista_tramites_xp_multa",
procedureName = "mteps_tramites.f_lista_tramites_xp_multa",
resultClasses = {ListaTramitesPorPagar.class},
parameters = {
		
})

@Entity
public class ListaTramitesPorPagar {
@Id
public String codTramite;

public String getCodTramite() {
	return codTramite;
}

public void setCodTramite(String codTramite) {
	this.codTramite = codTramite;
}


}
