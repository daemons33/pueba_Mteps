package mteps.sistpoa.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.PlanMap;
import mteps.sistpoa.Mappers.ProcesoMap;
import mteps.sistpoa.Models.Proceso;
import mteps.sistpoa.Models.TipoProceso;

@RestController
@RequestMapping("/procesos")

public class ProcesoCtl {
    
    @Autowired
    PlanMap planMap;

    @Autowired
    ProcesoMap procesoMap;

    @GetMapping(value = "/listatipoproceso/{id_plan}")
    public List<TipoProceso> getListaTipoProceso(@PathVariable Integer id_plan) {
        return procesoMap.listaTipoProceso(id_plan);
    }

    @GetMapping(value = "/verificaprocesosuperior/{id_tipo_proceso}/{id_tipo_proceso_sup}/{id_plan}")
    public List<Proceso> getVerificaPlanSuperior(@PathVariable Integer id_tipo_proceso,@PathVariable Integer id_tipo_proceso_sup ,@PathVariable Integer id_plan) {
        return procesoMap.verificaProcesoSuperior(id_tipo_proceso, id_tipo_proceso_sup, id_plan);
    }

    @PostMapping(value = "/insertaproceso")
    public List<Proceso> adicionar(@Validated @RequestBody Proceso dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return procesoMap.insertaProceso(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PostMapping(value = "/insertaprocesoref")
    public List<Proceso> adicionarRef(@Validated @RequestBody Proceso dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return procesoMap.insertaProcesoRef(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
