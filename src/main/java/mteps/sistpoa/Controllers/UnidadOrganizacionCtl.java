package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mteps.sistpoa.Mappers.UnidadOrganizacionalMap;
import mteps.sistpoa.Models.TipoProceso;
import mteps.sistpoa.Models.UnidadOrganizacional;

import java.util.List;

@RestController
@RequestMapping("/unidadorg")

public class UnidadOrganizacionCtl {
    @Autowired
    UnidadOrganizacionalMap unidadOrganizacionalMap;

    @GetMapping(value = "/listaunidadorganizacional/{id_tipo_plan}/{id_gestion}")
    public List<UnidadOrganizacional> getListaUnidadOrganizacional(@PathVariable Integer id_tipo_plan, @PathVariable Integer id_gestion) {
        Integer id_usuario = 1; //VALOR ESTATICO
        return unidadOrganizacionalMap.listaUnidadOrganizacional (id_tipo_plan,id_gestion,id_usuario);
    }
}
