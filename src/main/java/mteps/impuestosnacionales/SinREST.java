package mteps.impuestosnacionales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mteps.config.Resultado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/sin")
public class SinREST {
    @Autowired
    SinCORE sincore;

    @RequestMapping(path = "/status", method = RequestMethod.GET)
    public Resultado verificaEstadoSIN()
            throws JsonProcessingException {
        return sincore.validaEstadoServicioSIN();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public Resultado authSIN(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
        return sincore.loginSIN(vObjEntradaDatos);
    }
}