package mteps.inspectoria.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class actas implements Comparable<actas> {

	@Id
	public Integer id;
	public String norma;
	public Boolean cumple;
	public String condicion;
	public String observacion;
	public String orientacion;
	public String ordenClasificador;
	public Boolean na;
	@Override
	public int compareTo(actas o) {
		// TODO Auto-generated method stub
		return this.id.compareTo(o.id);
	}
	
}

