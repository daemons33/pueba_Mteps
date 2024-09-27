package mteps.dtickets;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mteps.config.Resultado;
import mteps.impuestosnacionales.SinCORE;
import mteps.tramites.tramiteCORE;

@RestController
@RequestMapping("/dticket")
public class DTicketREST {
	
	@Autowired
	DTicketCORE dticketCore;
	
	@Autowired
	tramiteCORE tramiteCore;
	
	@Autowired
    SinCORE sincore;

	@GetMapping("/sin/status")
    public Resultado verificaEstadoSIN()
            throws JsonProcessingException {
        return sincore.validaEstadoServicioSIN();
    }

    @PostMapping("/sin/login")
    public Resultado authSIN(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
    	ObjectMapper mapper = new ObjectMapper();
        // Convertir el objeto a una cadena JSON
        String jsonEntrada = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(vObjEntradaDatos);
        
        // Mostrar el objeto en el log de forma detallada
        System.out.println("[DTICKET] - Intento de inicio de sesión: " + jsonEntrada);
        
        return dticketCore.loginSIN(vObjEntradaDatos);
    }
    
    @PostMapping("/ci/login")
    public Resultado authCd(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
        return dticketCore.authCI(vObjEntradaDatos);
    }
    
    @PostMapping("/sup/login")
    public Resultado authSup(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
        return dticketCore.authSUP(vObjEntradaDatos);
    }
    
    @PostMapping("/gestionEmpleador")
    public Resultado gestionEmpleador(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
        return dticketCore.gestionEmpleador(vObjEntradaDatos);
    }
    
    @PostMapping("/gestionDTicket")
    public Resultado gestionTicket(@RequestBody ObjectNode vObjEntradaDatos)
            throws JsonProcessingException, SQLException {
        return dticketCore.gestionTicket(vObjEntradaDatos);
    }
    
    @GetMapping("/obtenerDatos")
    public Resultado obtenerUsuario(@RequestParam (name = "idUsuario",required = true) Integer  vObjEntradaDatos) throws IOException, SQLException
             {
        return dticketCore.obtenerUsuario(vObjEntradaDatos);
    }
    
    @GetMapping("/obtenerDepartamentos")
    public Resultado obtenerDepartamentos() throws IOException, SQLException
             {
        return dticketCore.f_obtener_departamentos();
    }
    
    @GetMapping("/listaDTickets")
    public Resultado listaDTickets(@RequestParam (name = "idUsuario",required = true) Integer  vObj1,@RequestParam (name = "estado",required = true) String  vObj2) throws IOException, SQLException
             {
        return dticketCore.listaDTickets(vObj1,vObj2);
    }
    
    @GetMapping("/listarDTickets")
    public Resultado listarDTickets(@RequestParam (name = "login",required = true) String  vObj1,@RequestParam (name = "estado",required = true) String  vObj2) throws IOException, SQLException
             {
        return dticketCore.listarDTickets(vObj1,vObj2);
    }
    
    @GetMapping("/listaTramites")
    public Resultado listarDTickets(@RequestParam (name = "id",required = true) Integer  vObj1) throws IOException, SQLException
             {
        return dticketCore.listaTramite(vObj1);
    }
    
    @GetMapping("/obtenerDetalleTickets")
    public Resultado obtenerDetalleTickets(@RequestParam (name = "login",required = true) String  vObj1) throws IOException, SQLException
             {
        return dticketCore.obtenerDetalleTickets(vObj1);
    }
        
}
