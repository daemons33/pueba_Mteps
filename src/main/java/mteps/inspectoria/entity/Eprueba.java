package mteps.inspectoria.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Eprueba {

	@Id
	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	public List<actas>  detalleActa;
	
}
