package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.EntidadMap;
import mteps.sistpoa.Mappers.PlanMetaMap;
import mteps.sistpoa.Models.Plan;
import mteps.sistpoa.Models.PlanMeta;
import mteps.sistpoa.Pojos.*;

import java.util.List;

@RestController
@RequestMapping("/planmetas")
public class PlanMetaCtl {
    @Autowired
    PlanMetaMap planMetaMap;

    @GetMapping(value = "/verificaseguimientometa/{id_plan}/{id_tipo_proceso}")
    public List<PlanMeta> getVerificaSeguimientoMeta(@PathVariable Integer id_plan, @PathVariable Integer id_tipo_proceso) {
        return planMetaMap.verificaSeguimientoMeta(id_plan, id_tipo_proceso);
    }

    @GetMapping(value = "/verificaseguimientopresupuesto/{id_plan}/{id_tipo_proceso}")
    public List<PlanMeta> getVerificaSeguimientoPresupuesto(@PathVariable Integer id_plan, @PathVariable Integer id_tipo_proceso) {
        return planMetaMap.verificaSeguimientoPresupuesto(id_plan, id_tipo_proceso);
    }

    @PostMapping(value = "/insertaconfmeta")
    public List<PlanMeta> adicionarConfMeta(@Validated @RequestBody PlanMeta dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return planMetaMap.insertaConfMeta(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/insertaconfpresupuesto")
    public List<PlanMeta> adicionarConfPresupuesto(@Validated @RequestBody PlanMeta dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return planMetaMap.insertaConfPresupuesto(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/listaestructurameta/{id_proceso}")
    public List<PResultado003> getListaEstructuraJsonMeta(@PathVariable Integer id_proceso) {
        return planMetaMap.listaEstructuraJsonMeta(id_proceso);
    }
    
    @GetMapping(value = "/listaestructurametaref/{id_proceso}")
    public List<PResultado003> getListaEstructuraJsonMetaRef(@PathVariable Integer id_proceso) {
        return planMetaMap.listaEstructuraJsonMetaRef(id_proceso);
    }
    

    @GetMapping(value = "/listaestructurapresupuesto/{id_proceso}/{id_indicador}")
    public List<PResultado004> getListaEstructuraJsonPresupuesto(@PathVariable Integer id_proceso,@PathVariable Integer id_indicador) {
        return planMetaMap.listaEstructuraJsonPresupuesto(id_proceso,id_indicador);
    }
    
    @GetMapping(value = "/listaestructurapresupuestoref/{id_proceso}")
    public List<PResultado004> getListaEstructuraJsonPresupuestoRef(@PathVariable Integer id_proceso) {
        return planMetaMap.listaEstructuraJsonPresupuestoRef(id_proceso);
    }

    @GetMapping(value = "/listaestructuratesp/{id_proceso}")
    public List<PResultado005> getListaEstructuraJsonTesp(@PathVariable Integer id_proceso) {
        return planMetaMap.listaEstructuraJsonTesp(id_proceso);
    }
    
    @GetMapping(value = "/listaestructuratespref/{id_proceso}")
    public List<PResultado005> getListaEstructuraJsonTespRef(@PathVariable Integer id_proceso) {
        return planMetaMap.listaEstructuraJsonTespRef(id_proceso);
    }

    @GetMapping(value = "/listaestructuramemoriacalculo/{id_proceso}")
    public List<PResultado002> getListaEstructuraJsonMemoria(@PathVariable Integer id_proceso) {
        return planMetaMap.listaEstructuraJsonMemCalc(id_proceso);
    }
    
    @GetMapping(value = "/listaestructuramemoriacalculoref/{id_proceso}")
    public List<PResultado002> getListaEstructuraJsonMemoriaRef(@PathVariable Integer id_proceso) {
        return planMetaMap.listaEstructuraJsonMemCalcRef(id_proceso);
    }

}
