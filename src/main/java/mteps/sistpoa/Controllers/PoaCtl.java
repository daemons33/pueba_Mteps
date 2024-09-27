package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.PoaMap;
import mteps.sistpoa.Mappers.ProgramaPresupuestoMap;
import mteps.sistpoa.Models.AccionPoaOrg;
import mteps.sistpoa.Models.FinProgPresupuesto;
import mteps.sistpoa.Models.Plan;
import mteps.sistpoa.Pojos.PPoa;
import mteps.sistpoa.Pojos.PResultado001;
import mteps.sistpoa.Pojos.PResultado006;
import mteps.sistpoa.Pojos.PResultado007;

import java.util.List;

@RestController
@RequestMapping("/poa")
public class PoaCtl {

    @Autowired
    PoaMap poaMap;

    @GetMapping(value = "/listaaccionpoa/{id_plan}")
    public List<PResultado006> getListaAccionPoa(@PathVariable Integer id_plan) {
        return poaMap.listaAccionPoa(id_plan);
    }

    @GetMapping(value = "/listauniorgaccionpoa/{id_proceso}")
    public List<PResultado007> getUniOrgAccionPoa(@PathVariable Integer id_proceso) {
        return poaMap.listaUniorgAccionPoa(id_proceso);
    }

    @PutMapping(value = "/actualizauniorgaccionpoa/{id_proceso}")
    public List<PResultado001> modificarUniOrgAccionPoa(@PathVariable Integer id_proceso, @RequestBody AccionPoaOrg dato, BindingResult resultado) {
        dato.setId_proceso (id_proceso);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return poaMap.actualizaUnionUniOrgAccionPoa(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/listapoaunidadorganizacion/{id_unidad_organizacional}/{id_gestion}")
    public List<PPoa> getListaPoaUnidadOrganizacional(@PathVariable Integer id_unidad_organizacional, @PathVariable Integer id_gestion) {
        return poaMap.listaPoaUnidadOrganizacional(id_unidad_organizacional, id_gestion);
    }

}