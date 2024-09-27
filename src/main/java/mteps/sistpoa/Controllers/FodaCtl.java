package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.FodaMap;
import mteps.sistpoa.Models.Foda;
import mteps.sistpoa.Models.Proceso;

import java.util.List;

@RestController
@RequestMapping("/fodas")
public class FodaCtl {

    @Autowired
    FodaMap fodaMap;

    @GetMapping(value = "/listafoda")
    public List<Foda> getListaFoda() {
        return fodaMap.listaFoda();
    }

    @PostMapping(value = "/actualizafoda")
    public List<Proceso> actualiza(@Validated @RequestBody Foda dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return fodaMap.actualizaFoda(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
