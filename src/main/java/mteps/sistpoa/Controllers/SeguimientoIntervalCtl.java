package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mteps.sistpoa.Mappers.EntidadMap;
import mteps.sistpoa.Mappers.SeguimientoIntervalMap;
import mteps.sistpoa.Models.Entidad;
import mteps.sistpoa.Models.SeguimientoInterval;

import java.util.List;

@RestController
@RequestMapping("/seguimientointerval")
public class SeguimientoIntervalCtl {
    @Autowired
    SeguimientoIntervalMap seguimientoIntervalMap;

    @GetMapping("/listaseguimiento")
    public List<SeguimientoInterval> getListadoSeguimientoInterval() {
        return seguimientoIntervalMap.listaSeguimientoInterval();
    }
}
