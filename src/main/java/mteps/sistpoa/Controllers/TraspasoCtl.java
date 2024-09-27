package mteps.sistpoa.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.TraspasoMap;
import mteps.sistpoa.Models.Traspaso;
import mteps.sistpoa.Pojos.PResultado008;

@RestController
@RequestMapping("/traspaso")
public class TraspasoCtl {
	
	@Autowired
	TraspasoMap TraspasoMap;
	
	 @PostMapping(value = "/insertaTraspasoDestino")
	    public List<PResultado008> adicionarD(@Validated @RequestBody Traspaso dato, BindingResult resultado) {
	        if (resultado.hasErrors()) {
	            throw new DatosInvalidosExcepciones(resultado);
	        }
	        return TraspasoMap.insertaTraspasoDestino(dato);
	        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	 
	 
	 @PostMapping(value = "/insertaTraspasoOrigen")
	    public List<PResultado008> adicionarO(@Validated @RequestBody Traspaso dato, BindingResult resultado) {
	        if (resultado.hasErrors()) {
	            throw new DatosInvalidosExcepciones(resultado);
	        }
	        return TraspasoMap.insertaTraspasoOrigen(dato);
	        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
}
