package mteps.planpago.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class postCalculoMulta {
		public Integer gestion;
		public String periodo;
		public Number montoTotalGanado;
		public Number descDerpartamento;
		
}
