package mteps.poa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatosValidacionConciliacion {

	@JsonProperty("difViaticos")
    public double difViaticos;

    @JsonProperty("difPasajes")
    public double difPasajes;
}
