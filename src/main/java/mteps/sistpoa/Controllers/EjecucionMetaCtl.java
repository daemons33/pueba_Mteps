package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.EjecucionMetaMap;
import mteps.sistpoa.Mappers.IndicadorMap;
import mteps.sistpoa.Models.*;
import mteps.sistpoa.Pojos.PResultado001;

import java.util.List;

@RestController
@RequestMapping("/ejecucionmeta")
public class EjecucionMetaCtl {

    @Autowired
    EjecucionMetaMap ejecucionMetaMap;

    @PostMapping(value = "/insertaejecucionadjtarea")
    public List<PResultado001> adicionarEjecAdjTareaEsp(@Validated @RequestBody RespaldoEjecucionMeta dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return  ejecucionMetaMap.insertaEjcAdjTareaEsp(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/insertaadjuntoactividad")
    public List<PResultado001> adicionarAdjActividad(@Validated @RequestBody RespaldoEjecucionMeta dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return  ejecucionMetaMap.insertaAdjActividad(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listaadjuntoverificacion/{id_indicador}/{id_indicador_meta}")
    public List<RespaldoEjecucionMeta> getListaAjdVerificacion(@PathVariable Integer id_indicador, @PathVariable Integer id_indicador_meta) {
        return ejecucionMetaMap.listaAdjVerificacion(id_indicador, id_indicador_meta);
    }


}
