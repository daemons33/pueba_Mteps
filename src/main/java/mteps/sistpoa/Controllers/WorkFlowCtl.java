package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mteps.sistpoa.Mappers.WorkFlowMap;
import mteps.sistpoa.Models.Plan;
import mteps.sistpoa.Models.WfRolEstado;

import java.util.List;

@RestController
@RequestMapping("/wf")
public class WorkFlowCtl {

    @Autowired
    WorkFlowMap workFlowMap;

    @GetMapping(value = "/verificaflujoplan/{id_tipo_plan}/{id_plan}")
    public List<WfRolEstado> getVerificaFlujoPlan(@PathVariable Integer id_tipo_plan, @PathVariable Integer id_plan) {
        Integer id_usuario = 1; //valor estatico
        return workFlowMap.verificaFlujoPlan(id_tipo_plan,id_plan,id_usuario);
    }

}
