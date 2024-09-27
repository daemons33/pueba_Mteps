package mteps.denuncias.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntBeneficiosSociales {
	@Id
	public Integer idBeneficioSocial;
public Integer idClasificadorDenuncia;
public String nombreDenuncia;
public Integer gestion;
public String periodo;
public String detalle;
}
