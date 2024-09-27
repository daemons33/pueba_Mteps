package mteps.inspectoria.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class EPreFiniquito {
		  @Id	
		  public Double final_total;
		  public Double multa;
		  public Double total;
		  public Double otros1;
		  public Double otros2;
		  public Double otros3;
		  public String concepto1;
		  public String concepto2;
		  public String concepto3;
		  public Double desahucio;
			@Temporal(TemporalType.DATE)
			@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",timezone = "GMT-4")
		  public Date fechaRetiro;
			@Temporal(TemporalType.DATE)
			@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",timezone = "GMT-4")
		  public Date fechaIngreso;
			public Integer aguinaldoDias;
		    public Integer aguinaldoMeses;
		    public Double bonoAntiguedad;
		    public Double sueldoPromedio;
		    public Integer vacacionesDias;
		    public Integer indemnizacionDias;
		    public Integer tiempoTrabajoDias;
		    public Double aguinaldoDiasMonto;
		    public Integer indemnizacionAnios;
		    public Integer indemnizacionMeses;
		    public Integer tiempoTrabajoAnios;
		    public Integer tiempoTrabajoMeses;
		    public Double aguinaldoMesesMonto;
		    public Double indemnizacionDiasMonto;
		    public Double indemnizacionAniosMonto;
		    public Double indemnizacionMesesMonto;
		    public Double vacacionesDuodecimasMonto;
		
}
