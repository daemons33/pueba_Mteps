package mteps.sistpoa.Pojos;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class PMeta implements Serializable {

    public static final long serialVersionUID = 1L;

    public Integer interval_valor;
    public Date inicio;
    public Date fin;
    public Double valor_programado;
    public Double valor_ejecutado;
    public Double gasto_corriente;
    public Double inversion_publica;

    public Integer id_entidad;
    public Integer codigo_resp;
    public String mensaje_resp;


    public PMeta(Integer interval_valor, Date inicio, Date fin, Double valor_programado, Double valor_ejecutado){
         this.interval_valor = interval_valor;
         this.inicio = inicio;
         this.fin = fin;
         this.valor_programado = valor_programado;
         this.valor_ejecutado = valor_ejecutado;
    }

    public PMeta(Integer interval_valor, Double inversion_publica, Double gasto_corriente, Date inicio, Date fin) {
        this.interval_valor = interval_valor;
        this.inicio = inicio;
        this.fin = fin;
        this.inversion_publica = inversion_publica;
        this.gasto_corriente = gasto_corriente;
    }

}
