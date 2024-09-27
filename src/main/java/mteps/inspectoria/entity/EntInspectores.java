package mteps.inspectoria.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntInspectores {
@Id
public Integer id;
public String nombre;
public String cargo;
}
