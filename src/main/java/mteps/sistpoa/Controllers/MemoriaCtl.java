package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.MemoriaMap;
import mteps.sistpoa.Mappers.PlanMap;
import mteps.sistpoa.Mappers.ProcesoMap;
import mteps.sistpoa.Models.MemoriaCalculo;
import mteps.sistpoa.Models.Proceso;
import mteps.sistpoa.Models.ProgramaMemoriaCalculo;
import mteps.sistpoa.Models.TipoProceso;
import mteps.sistpoa.Pojos.PResultado001;

import java.util.List;

@RestController
@RequestMapping("/memorias")

public class MemoriaCtl {
    
    @Autowired
    MemoriaMap memoriaMap;

    @GetMapping(value = "/listamemoriacalculo/{id_proceso}")
    public List<MemoriaCalculo> getListaMemoriaCalculo(@PathVariable Integer id_proceso) {
        return memoriaMap.listaMemoriaCalculo(id_proceso);
    }
    
    @GetMapping(value = "/listamemoriacalculoref/{id_proceso}")
    public List<MemoriaCalculo> getListaMemoriaCalculoRef(@PathVariable Integer id_proceso) {
        return memoriaMap.listaMemoriaCalculoRef(id_proceso);
    }

    @GetMapping(value = "/listaseguimientomemoriacalculo/{id_memoria_calculo}")
    public List<ProgramaMemoriaCalculo> getListaSeguimientoMemoriaCalculo(@PathVariable Integer id_memoria_calculo) {
        return memoriaMap.listaseguimientomemoriacalculo(id_memoria_calculo);
    }

    @PostMapping(value = "/insertamemoriacalculo")
    public List<MemoriaCalculo> adicionar(@Validated @RequestBody MemoriaCalculo dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return memoriaMap.insertaMemoriaCalculo(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PostMapping(value = "/insertamemoriacalculoref")
    public List<MemoriaCalculo> adicionarRef(@Validated @RequestBody MemoriaCalculo dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return memoriaMap.insertaMemoriaCalculoRef(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/actualizamemoriacalculo/{id_memoria_calculo}")
    public List<MemoriaCalculo> modificaMemoriaCalculo(@Validated @RequestBody MemoriaCalculo dato, @PathVariable Integer id_memoria_calculo, BindingResult resultado) {
        dato.setId_memoria_calculo(id_memoria_calculo);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return memoriaMap.actualizaMemoriaCalculo(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping(value = "/actualizamemoriacalculoref/{id_memoria_calculo}")
    public List<MemoriaCalculo> modificaMemoriaCalculoRef(@Validated @RequestBody MemoriaCalculo dato, @PathVariable Integer id_memoria_calculo, BindingResult resultado) {
        dato.setId_memoria_calculo(id_memoria_calculo);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return memoriaMap.actualizaMemoriaCalculoRef(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    
    @GetMapping(value = "/borrarMemoria/{id_memoria}")
    public List<PResultado001> setBorrarIndicador(@PathVariable Integer id_memoria) {
        return memoriaMap.borrarMemoriaCalculo(id_memoria);
    }
    
    @GetMapping(value = "/borrarMemoriaRef/{id_memoria}")
    public List<PResultado001> setBorrarIndicadorRef(@PathVariable Integer id_memoria) {
        return memoriaMap.borrarMemoriaCalculoRef(id_memoria);
    }
    
    
    @GetMapping(value = "/reducirMemoriaRef/{id_memoria}")
    public List<PResultado001> reducirMemoriaRef(@PathVariable Integer id_memoria) {
        return memoriaMap.reducirMemoriaRef(id_memoria);
    }
    
    @PutMapping(value = "/actualizaJustObsRef/{id_plan}")
    public List<PResultado001> actualizarJustObsRef(@Validated @RequestBody MemoriaCalculo dato,@PathVariable Integer id_plan) {
    	dato.setId_memoria_calculo(id_plan);
        return memoriaMap.actualizaJustObsRef(dato);
    }

}
