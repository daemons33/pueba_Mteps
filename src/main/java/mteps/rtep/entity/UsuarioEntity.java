package mteps.rtep.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UsuarioEntity {

	@Id
	public Long idUsuario;
    public String login;
    public Boolean estado;
    public Integer usuarioCreacion;
    public Integer usuarioModificacion;
    public Timestamp fechaCreacion;
    public Timestamp fechaModificacion;
    public String observacion;
    public String correoElectronico;
    public Boolean ciudadaniaDigital;
    public String clave;
}
