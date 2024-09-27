package mteps.sistpoa.Excepciones;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mensajes {

	public int estado;
	public String mensaje;
	public LocalDate tiempo;
	public List<String> errores;

    Mensajes(String mensaje) {
        this.mensaje = mensaje;
    }
    
}
