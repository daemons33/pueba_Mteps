package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Mappers.EntidadMap;
import mteps.sistpoa.Mappers.PlanMap;
import mteps.sistpoa.Models.Entidad;
import mteps.sistpoa.Models.Plan;
import mteps.sistpoa.Models.TipoProceso;
import mteps.sistpoa.Pojos.PLogo;

import java.util.List;

@RestController
@RequestMapping("/entidades")
public class EntidadCtl {

    @Autowired
    EntidadMap entidadMap;

    @GetMapping("/listamisionvision")
    public List<Entidad> getAll() {
        return entidadMap.listaMisioVision();
    }

    @PostMapping(value = "/actualizamisionvision")
    public List<Entidad> postActualizaMisionVision(@Validated @RequestBody Entidad dato, BindingResult resultado) {
        return entidadMap.actualizaMisionVision(dato.getMision(),dato.getVision());
    }

}
