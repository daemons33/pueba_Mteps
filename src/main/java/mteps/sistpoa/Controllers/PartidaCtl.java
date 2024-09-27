package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.*;
import mteps.sistpoa.Models.*;
import mteps.sistpoa.Pojos.PMeta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import java.util.List;

@RestController
@RequestMapping("/partidas")
public class PartidaCtl {

    @Autowired
    PartidaMap partidaMap;

    @GetMapping(value = "/listapartidapresupuesto")
    public List<PartidaPresupuesto> getListaPartidaPresupuesto() {
        return partidaMap.listaCodPartidaPresup();
    }

    @GetMapping(value = "/listapartidapresupuesto/{id_partida_presupuesto_sup}")
    public List<PartidaPresupuesto> getListaPartidaPresupuesto(@PathVariable Integer id_partida_presupuesto_sup) {
        return partidaMap.listaCodPartidaPresup2(id_partida_presupuesto_sup);
    }

    @PostMapping(value = "/actualizamedioverifica")
    public List<IndicadorMeta> modificar(@Validated @RequestBody IndicadorMeta dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return partidaMap.actualizaMedioVerif(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}


