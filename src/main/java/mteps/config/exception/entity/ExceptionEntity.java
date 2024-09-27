package mteps.config.exception.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "workflow",name = "wf_log_backend")
public class ExceptionEntity {	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    public Long id;
	    public String usuario;
	    public LocalDateTime fecha;
	    public String url;	    
	    @Column(name = "url_origen")
	    public String urlOrigen;
	    @Column(name = "ip_origen")
	    public String ipOrigen;
	    public String metodo;	    
	    public String peticion;
	    public String respuesta;
	    @Column(name = "codigo_estado")
	    public Integer codigoEstado;
	    public Boolean concluido;
	    // getters and setters
	
}
