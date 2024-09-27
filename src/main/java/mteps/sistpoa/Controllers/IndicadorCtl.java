package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.poa.PoaCore;
import mteps.poa.entity.F_lista_indicador;
import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.EntidadMap;
import mteps.sistpoa.Mappers.IndicadorMap;
import mteps.sistpoa.Models.*;
import mteps.sistpoa.Pojos.PMatriz;
import mteps.sistpoa.Pojos.PResultado001;

import java.util.List;

@RestController
@RequestMapping("/indicadores")
public class IndicadorCtl {

    @Autowired
    IndicadorMap indicadorMap;
    
    @Autowired
   	PoaCore poaCore;
    
    @GetMapping("/listatipoindicador")
    public List<TipoIndicador> getListaTipoIndicador() {
        return indicadorMap.listaTipoIndicador();
    }

    @PostMapping(value = "/insertaindicador")
    public List<Indicador> adicionar(@Validated @RequestBody Indicador dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return indicadorMap.insertaIndicador(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    
    @PostMapping(value = "/insertaindicadorref")
    public List<Indicador> adicionarRef(@Validated @RequestBody Indicador dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return indicadorMap.insertaIndicadorRef(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @GetMapping("/listaindicadors/{id_proceso}")
//    public List<Indicador> getListaIndicador(@PathVariable Integer id_proceso) {
//    	List<Indicador> res = indicadorMap.listaIndicador(id_proceso);
//    	return res;
//    }
    // se reemplaza   	
	@GetMapping("/listaindicador/{id_proceso}")
    public List<F_lista_indicador>  getListaIndicador(@PathVariable Integer id_proceso) {
    	  	
        return poaCore.getListaIndicador(id_proceso);
    }
    

    @GetMapping("/listaindicadorcortoplazo/{id_proceso}")
    public List<Indicador> getListaIndicadorCortoPlazo(@PathVariable Integer id_proceso) {
        return indicadorMap.listaIndicadorCortoPlazo(id_proceso);
    }

    @GetMapping("/listaseguimientometa/{id_indicador}")
    public List<IndicadorMeta> getListaSegMeta(@PathVariable Integer id_indicador) {
        return indicadorMap.listaSegMeta(id_indicador);
    }
    
    @GetMapping("/listaseguimientometaref/{id_indicador}")
    public List<IndicadorMeta> getListaSegMetaRef(@PathVariable Integer id_indicador) {
        return indicadorMap.listaSegMetaRef(id_indicador);
    }

    @GetMapping("/listaseguimientopresupuesto/{id_indicador}")
    public List<IndicadorPresupuesto> getListaSegPresupuesto(@PathVariable Integer id_indicador) {
        return indicadorMap.listaSegPresupuesto(id_indicador);
    }

    @PostMapping(value = "/insertaejecucionadjtarea")
    public List<PResultado001> adicionarEjecAdjTareaEsp(@Validated @RequestBody RespaldoEjecucionMeta dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return indicadorMap.insertaEjcAdjTareaEsp(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listaplanindicador/{id_tipo_plan}/{id_gestion}")
    public List<PlanIndicador> getListaPlanIndicador(@PathVariable Integer id_tipo_plan,@PathVariable Integer id_gestion) {
        Integer id_usuario = 1;  //statico dali
        return indicadorMap.listaPlanIndicador(id_usuario,id_tipo_plan, id_gestion);
    }

    @PutMapping(value = "/actualizaindicador/{id_indicador}")
    public List<PResultado001> modificaIndicador(@PathVariable Integer id_indicador, @Validated @RequestBody Indicador dato, BindingResult resultado) {
        dato.setId_indicador(id_indicador);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return indicadorMap.modificarIndicador(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping(value = "/actualizaindicadorref/{id_indicador}")
    public List<PResultado001> modificaIndicadorRef(@PathVariable Integer id_indicador, @Validated @RequestBody Indicador dato, BindingResult resultado) {
        dato.setId_indicador(id_indicador);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return indicadorMap.modificarIndicadorRef(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/actualizaindicador1/{id_indicador}")
    public List<PResultado001> modificaIndicador1(@PathVariable Integer id_indicador, @Validated @RequestBody Indicador dato, BindingResult resultado) {
        dato.setId_indicador(id_indicador);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return indicadorMap.modificarIndicador1(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/actualizaindicadorprogejec/{id_indicador}")
    public List<PResultado001> modificaIndicadorProgEjec(@PathVariable Integer id_indicador, @Validated @RequestBody Indicador dato, BindingResult resultado) {
        dato.setId_indicador(id_indicador);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return indicadorMap.modificarIndicadorProgEjec(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/evaluacionindicador/{id_indicador}")
    public List<PResultado001> getEstadoEditarIndicador(@PathVariable Integer id_indicador,@Validated @RequestBody Indicador dato) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        return indicadorMap.evaluacionIndicador(id_indicador,dato.getEvaluacion_indicador());
    }

    @PutMapping(value = "/medidasindicadormeta/{id_indicador_meta}")
    public List<PResultado001> setMedidasIndicadorMeta(@PathVariable Integer id_indicador_meta,@Validated @RequestBody IndicadorMeta dato) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        dato.setId_indicador_meta(id_indicador_meta);
        return indicadorMap.medidaIndicadorMeta(dato);
    }

    @GetMapping(value = "/borrarindicador/{id_indicador}")
    public List<PResultado001> setBorrarIndicador(@PathVariable Integer id_indicador) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        //dato.setId_indicador_meta(id_indicador_meta);
        return indicadorMap.borrarIndicador(id_indicador);
    }
    
    @GetMapping(value = "/borrarindicadorref/{id_indicador}")
    public List<PResultado001> setBorrarIndicadorRef(@PathVariable Integer id_indicador) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        //dato.setId_indicador_meta(id_indicador_meta);
        return indicadorMap.borrarIndicadorRef(id_indicador);
    }

    @GetMapping(value = "/borrarproceso/{id_proceso}")
    public List<PResultado001> setBorrarProceso(@PathVariable Integer id_proceso) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        //dato.setId_indicador_meta(id_indicador_meta);
        return indicadorMap.borrarProceso(id_proceso);
    }
    
    
    @GetMapping(value = "/borrarprocesoref/{id_proceso}")
    public List<PResultado001> setBorrarProcesoRef(@PathVariable Integer id_proceso) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        //dato.setId_indicador_meta(id_indicador_meta);
        return indicadorMap.borrarProcesoRef(id_proceso);
    }
    
    
    @GetMapping(value = "/listaindicadormatriz/{id_proceso}")
    public List<PMatriz> getListaIndicadorMatriz(@PathVariable Integer id_proceso) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        //dato.setId_indicador_meta(id_indicador_meta);
        return indicadorMap.listadoIndicadorMatriz(id_proceso);
    }
}
