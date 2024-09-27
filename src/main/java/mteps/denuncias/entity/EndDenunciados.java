package mteps.denuncias.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EndDenunciados {
@Id
public String idD;
public String cargo;
public String unidad;
public String nombreCompleto;
}
