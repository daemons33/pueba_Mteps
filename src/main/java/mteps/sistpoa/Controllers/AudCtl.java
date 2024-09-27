package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.AudMap;
import mteps.sistpoa.Mappers.PoaMap;
import mteps.sistpoa.Models.AccionPoaOrg;
import mteps.sistpoa.Models.Indicador;
import mteps.sistpoa.Models.IndicadorMeta;
import mteps.sistpoa.Models.IndicadorPresupuesto;
import mteps.sistpoa.Pojos.PPoa;
import mteps.sistpoa.Pojos.PResultado001;
import mteps.sistpoa.Pojos.PResultado006;
import mteps.sistpoa.Pojos.PResultado007;

import java.util.List;

@RestController
@RequestMapping("/aud")
public class AudCtl {

    
    AudMap audMap;

    @GetMapping(value = "/listaindicador/{id_indicador}")
    public List<Indicador> getListaIndicador(@PathVariable Integer id_indicador) {
        return audMap.listaAudIndicador(id_indicador);
    }

    @GetMapping(value = "/listaindicadormeta/{id_indicador_meta}")
    public List<IndicadorMeta> getListaIndicadorMeta(@PathVariable Integer id_indicador_meta) {
        return audMap.listaAudIndicadorMeta(id_indicador_meta);
    }

    @GetMapping(value = "/listaindicadorpresupuesto/{id_indicador_presupuesto}")
    public List<IndicadorPresupuesto> getListaIndicadorPresupuesto(@PathVariable Integer id_indicador_presupuesto) {
        return audMap.listaAudIndicadorPresupuesto(id_indicador_presupuesto);
    }

    @GetMapping(value = "/listaindicadorconindicadoresmeta/{id_indicador}")
    public List<Indicador> getListaIndicadorconIndicadoresMeta(@PathVariable Integer id_indicador) {
        return audMap.listaAudIndicadorconIndicadoresmetas(id_indicador);
    }

}