package mteps.consultas.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class Prefiniquito {
	@Id
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date fechaIngreso;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    public Date fechaRetiro;
    public Integer tiempoTrabajoAnios;
    public Integer tiempoTrabajoMeses;
    public Integer tiempoTrabajoDias;
    public Double sueldoPromedio;
    public Double desahucio;
    public Integer indemnizacionAnios;
    public Double indemnizacionAniosMonto;
    public Integer indemnizacionMeses;
    public Double indemnizacionMesesMonto;
    public Integer indemnizacionDias;
    public Double indemnizacionDiasMonto;
    public Integer aguinaldoMeses;
    public Double aguinaldoMesesMonto;
    public Integer aguinaldoDias;
    public Double aguinaldoDiasMonto;
    public Integer vacacionesGestion;
    public Double vacacionesGestionMonto;
    public Integer vacacionesDuodecimas;
    public Double vacacionesDuodecimasMonto;
    public Double bonoAntiguedad;
    public Double otros1;
    public Double otros2;
    public Double otros3;
    public Double otros4;
    public Double total;
}
