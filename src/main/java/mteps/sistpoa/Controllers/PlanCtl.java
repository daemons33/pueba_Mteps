package mteps.sistpoa.Controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.PlanMap;
import mteps.sistpoa.Models.*;
import mteps.sistpoa.Pojos.PResultado001;

@RestController
@RequestMapping("/planes")
public class PlanCtl {

    @Autowired
    PlanMap planMap;


    @GetMapping("/all")
    public List<Plan> getAll() {
        return planMap.findAll();
    }

    @GetMapping(value = "/listaprocesos/{id_plan}/{id_proceso}")
    public List<Plan> getListaPlanProceso(@PathVariable Integer id_plan, @PathVariable Integer id_proceso) {
        return planMap.listaPlanProceso(id_plan, id_proceso);
    }

    @GetMapping(value = "/listagestiones")
    public List<Gestion> getGestion() {
    	List<Gestion> ss = planMap.listaGestion();
        return ss;
    }

    @GetMapping(value = "/estructuraplan")
    public List<Estructura> getEstructuraPlan() {
        return planMap.estructuraPlan();
    }

    @GetMapping(value = "/listatipoplanes")
    public List<TipoPlan> getTipoPlanes() {
        Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        return planMap.listaTipoPlan(id_usuario);
    }

    @GetMapping(value = "/verificaplansuperior/{id_tipo_plan}/{id_gestion}")
    public List<Plan> getVerificaPlanSuperior(@PathVariable Integer id_plan, @PathVariable Integer id_gestion) {
        Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        return planMap.verificaPlanSuperior(id_usuario, id_plan, id_gestion);
    }

    @GetMapping(value = "/listaplan/{id_tipo_plan}/{id_gestion}")
    public List<ProcesoPlan> getListaPlan(@PathVariable Integer id_tipo_plan, @PathVariable Integer id_gestion) {
        Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO        
        return planMap.listaPlan(id_usuario,id_tipo_plan,id_gestion);
    }

    @PostMapping(value = "/insertaplan")
    public List<Plan> adicionar(@Validated @RequestBody Plan dato, BindingResult resultado) {
        System.out.println("fecha=" + dato.toString());
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return planMap.insertaPlan(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/procesosplan/{id_plan}")
    public List<Plan> procesoPlan(@PathVariable Integer id_plan) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        return planMap.procesosPlan(id_plan);
    }

    @PutMapping(value = "/actualizaplan/{id_plan}")
    public List<PResultado001> modificarPlan(@PathVariable Integer id_plan, @RequestBody Plan dato, BindingResult resultado) {
        dato.setId_plan(id_plan);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return planMap.actualizaPlan(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/actualizaproceso/{id_proceso}")
    public List<PResultado001> modificarProceso(@PathVariable(value = "id_proceso") Integer id_proceso, @Validated @RequestBody Proceso dato, BindingResult resultado) {
        dato.setId_proceso(id_proceso);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return planMap.actualizaProceso(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping(value = "/actualizaprocesoref/{id_proceso}")
    public List<PResultado001> modificarProcesoRef(@PathVariable(value = "id_proceso") Integer id_proceso, @Validated @RequestBody Proceso dato, BindingResult resultado) {
        dato.setId_proceso(id_proceso);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return planMap.actualizaProcesoRef(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/actualizaestadoplanprc/{id_plan}")
    public List<PResultado001> modificarEstadoPlanProceso(@PathVariable(value = "id_plan") Integer id_plan, @Validated @RequestBody Plan dato, BindingResult resultado) {
        dato.setId_plan(id_plan);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return planMap.actualizaEstadoPlanProceso(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/reformulacionpoa/{id_plan}")
    public List<PResultado001> getFormulacionPoa(@PathVariable Integer id_plan) {
        return planMap.reformulacionPoa(id_plan);
    }

    @PutMapping(value = "/estadoeditarplan/{id_plan}")
    public List<PResultado001> getEstadoEditarPlan(@PathVariable Integer id_plan,@Validated @RequestBody Plan dato) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        return planMap.estadoEditarPlan(id_plan,dato.getEditar_poa(), dato.getNro_reformulacion());
    }

    @PutMapping(value = "/evaluacioneditarplan/{id_plan}")
    public List<PResultado001> getEvaluacionEditarPlan(@PathVariable Integer id_plan,@Validated @RequestBody Plan dato) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        dato.setId_plan(id_plan);
        return planMap.evaluacionEditarPlan(dato);
    }

    /*

    @GetMapping(value = "/procesosPlan}")
    public List<Plan> postInsertaPlan(@PathVariable Integer id_tipo_plan, @PathVariable Integer id_gestion) {
        Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        return planMap.verificaPlanSuperior(id_usuario, id_tipo_plan, id_gestion);
    }

    ResponseEntity<?> dato(@PathVariable Integer id_plan, @PathVariable Integer id_proceso ) {
        //Cursos dato = null;
        dato = planMap.listaPlanProceso(id_plan, id_proceso);
        if (dato == null) {
            throw new NoSuchElementException("No existe dato");
        }
        return new ResponseEntity<Cursos>(dato, HttpStatus.OK);
    }*/


}
