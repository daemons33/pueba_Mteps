package mteps.sistpoa.Excepciones;

import org.springframework.validation.BindingResult;

public class DatosInvalidosExcepciones extends RuntimeException {

    private final transient BindingResult resultado;

    public DatosInvalidosExcepciones(BindingResult resultado) {
        super();
        this.resultado = resultado;
    }

    public DatosInvalidosExcepciones(String mensaje, BindingResult resultado) {
        super(mensaje);
        this.resultado = resultado;
    }

    public BindingResult getResult() {
        return resultado;
    }

}
