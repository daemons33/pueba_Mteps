package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.FodaMap;
import mteps.sistpoa.Mappers.ProgramaPresupuestoMap;
import mteps.sistpoa.Models.FinProgPresupuesto;
import mteps.sistpoa.Models.Foda;
import mteps.sistpoa.Models.Indicador;
import mteps.sistpoa.Models.Proceso;

import java.util.List;

@RestController
@RequestMapping("/programapresupuesto")
public class ProgramaPresupuestoCtl {

    @Autowired
    ProgramaPresupuestoMap programaPresupuestoMap;

    @GetMapping(value = "/listaprogpresupuesto/{id_gestion}")
    public List<FinProgPresupuesto> getListaProgPresupuesto(@PathVariable Integer id_gestion) {
        return programaPresupuestoMap.listaProgramaPresupuesto(id_gestion);
    }

}