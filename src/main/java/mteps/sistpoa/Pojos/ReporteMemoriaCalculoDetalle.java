package mteps.sistpoa.Pojos;

import java.sql.Date;

import javax.persistence.Id;

public class ReporteMemoriaCalculoDetalle {
	
	@Id
	public Integer id_programa_mem_calculo;
	public Integer mes_programa;
	public Date inicio;
	public Date fin;
	public Double cantidad;
	public Double costo; 

}
