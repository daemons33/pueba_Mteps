package mteps.poa.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@NamedStoredProcedureQuery(name = "mteps_plan.f_lista_ejecucion_fisica", procedureName = "mteps_plan.f_lista_ejecucion_fisica", resultClasses = {
		F_lista_ejecucion_fisica.class }, parameters = {
				@StoredProcedureParameter(name = "i_id_plan", type = Integer.class, mode = ParameterMode.IN),
				@StoredProcedureParameter(name = "i_tipo_proceso", type = Integer.class, mode = ParameterMode.IN)})
@Entity
public class F_lista_ejecucion_fisica {
	
	public Integer id_proceso; 
	public String acp;
	public String op; 
	public String act; 
	public String nombre; 
	public String descripcion; 
	public String tipo; 
	@Id
	public Integer id_indicador;
	public String indicador; 
	public String linea_base; 
	public double meta; 
	public Integer nro_filas;
	public double ponderacion; 
	public String medio_verificacion;
	public double p1; 
	public double p2; 
	public double p3; 
	public double p4; 
	public double p5; 
	public double p6; 
	public double p7; 
	public double p8; 
	public double p9; 
	public double p10; 
	public double p11; 
	public double p12; 
	public double e1; 
	public double e2; 
	public double e3; 
	public double e4; 
	public double e5; 
	public double e6; 
	public double e7; 
	public double e8; 
	public double e9; 
	public double e10; 
	public double e11; 
	public double e12; 
	public double ea1; 
	public double ea2; 
	public double ea3; 
	public double ea4; 
	public double ea5; 
	public double ea6; 
	public double ea7; 
	public double ea8; 
	public double ea9;
	public double ea10; 
	public double ea11; 
	public double ea12; 
	public double eb1; 
	public double eb2; 
	public double eb3; 
	public double eb4; 
	public double eb5; 
	public double eb6; 
	public double eb7; 
	public double eb8; 
	public double eb9; 
	public double eb10; 
	public double eb11; 
	public double eb12;
}
