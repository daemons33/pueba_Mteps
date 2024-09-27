package mteps.poa.entity;

import java.util.List;

public class DatoEntradaGestionConciliacion {
	public String estado;
	public String fecha_corte;
	public String gestion_seguimiento;
	public Integer id_doc_conciliacion;
	public String login;
	public String nombre;
	public String transaccion;
	public String gestion;
	public List<datos> file;
	
	public static class datos{
	    public int nro;
	    public String entidad;
	    public String da;
	    public String ue;
	    public String cat_prg;
	    public String fte;
	    public String org;
	    public String objeto;
	    public String descripcion;
	    public double pres_inicial;
	    public double mod_aprobadas;
	    public double pres_vigente;
	    public double preventivo;
	}
	
}
