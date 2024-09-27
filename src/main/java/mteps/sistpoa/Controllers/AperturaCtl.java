package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mteps.sistpoa.Excepciones.DatosInvalidosExcepciones;
import mteps.sistpoa.Mappers.AperturaMap;
import mteps.sistpoa.Mappers.FodaMap;
import mteps.sistpoa.Models.*;
import mteps.sistpoa.Pojos.POperacionProcesoUnidad;
import mteps.sistpoa.Pojos.PResultado001;

import java.util.List;

@RestController
@RequestMapping("/aperturas")
public class AperturaCtl {

	@Autowired
    AperturaMap aperturaMap;

    @GetMapping(value = "/listaapertura/{id_gestion}")
    public List<Apertura> getListaApertura(@PathVariable Integer id_gestion) {
        return aperturaMap.listaAperturas(id_gestion);
    }

    @GetMapping(value = "/listaapertura")
    public List<Apertura> getListaApertura() {
        return aperturaMap.listaAperturas1();
    }

    @GetMapping(value = "/listaaperturaorganizacion/{id_apertura}")
    public List<AperturaOrganizacion> getListaAperturaOrganizacion(@PathVariable Integer id_apertura) {
        return aperturaMap.listaAperturaOrganizacion(id_apertura);
    }

    @GetMapping(value = "/listaaccionpoaapertura")
    public List<AccionPoaApertura> getListaAccionPoaApertura(
    		@RequestParam(name = "id_apertura", required = true, defaultValue = "") Integer pVariable1,
			@RequestParam(name = "id_tipo_proceso", required = true, defaultValue = "") Integer pVariable2) {
        return aperturaMap.listaAccionPoaApertura1(pVariable1,pVariable2);
    }

    @PostMapping(value = "/insertasolicitud")
    public List<PResultado001> adicionaSolicitud(@Validated @RequestBody Solicitud dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return aperturaMap.InsertaActualizaSolicitudCertificacion(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/actualizasolicitud/{id_solicitud}")
    public List<PResultado001> actualizaSolicitud(@PathVariable Integer id_solicitud, @Validated @RequestBody Solicitud dato, BindingResult resultado) {
        dato.setId_solicitud(id_solicitud);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return aperturaMap.InsertaActualizaSolicitudCertificacion(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/insertadetallesolicitud")
    public List<PResultado001> getAdicionaDetalleSolicitud(@Validated @RequestBody SolicitudDetalle dato, BindingResult resultado) {
        dato.setId_detalle(null);
        System.out.println("detalle="+ dato);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return aperturaMap.InsertaActualizaDetalleSolicitudCertificacion(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/buscasolicitudcertificacion/{id_solicitud}")
    public List<Solicitud> getBuscaSolicitudCertificacion(@PathVariable Integer id_solicitud) {
        return aperturaMap.BuscaSolicitudCertificacion(id_solicitud);
    }

    @GetMapping(value = "/listasolicitudcertificacionestado/{estado}")
    public List<Solicitud> getBuscaSolicitudCertificacionEstado(@PathVariable String estado) {
        return aperturaMap.ListaSolicitudCertificacionEstado(estado);
    }

    @GetMapping(value = "/listasolicituddetallecertificacion/{id_solicitud}")
    public List<SolicitudDetalle> getBuscaSolicitudCertificacionEstado(@PathVariable Integer id_solicitud) {
        return aperturaMap.ListaDetalleCertificacion(id_solicitud);
    }

    @GetMapping(value = "/listaoperacionpoaxaperturaunidad/{id_apertura}/{id_unidad_organizacional}")
    public List<POperacionProcesoUnidad> getListaOperacionPoaxProceso(@PathVariable Integer id_apertura, @PathVariable Integer id_unidad_organizacional) {
        if (id_unidad_organizacional == 0) {
            return aperturaMap.ListaOperacionPoaxApertura(id_apertura);
        } else {
            return aperturaMap.ListaOperacionPoaxAperturaUnidad(id_apertura, id_unidad_organizacional);
        }
    }

    @GetMapping(value = "/listaoperacionpoaxunidadorganizacional/{id_unidad_organizacional}")
    public List<POperacionProcesoUnidad> getListaOperacionPoaxUnidadOrganizacional(@PathVariable Integer id_unidad_organizacional) {
            return aperturaMap.ListaOperacionPoaxUnidadOrganizacional(id_unidad_organizacional);

    }

    @PutMapping(value = "/actualizadetallesolicitud/{id_detalle}")
    public List<PResultado001> actualizaSolicitud(@PathVariable Integer id_detalle, @Validated @RequestBody SolicitudDetalle dato, BindingResult resultado) {
        dato.setId_detalle(id_detalle);
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return aperturaMap.InsertaActualizaDetalleSolicitudCertificacion(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/actualizarestadosolicitud/{id_solicitud}")
    public List<PResultado001> getActualizaEstadoSolicitud(@PathVariable Integer id_solicitud,@Validated @RequestBody Solicitud dato) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        return aperturaMap.actualizaEstadoSolicitud(id_solicitud, dato.estado_solicitud,dato.id_usuario,dato.justificacion,dato.observacion,dato.usr_firma);
    }

    @GetMapping(value = "/listasolicitudxproceso/{id_proceso}")
    public List<Solicitud> getListaSolicitudxProceso(@PathVariable Integer id_proceso) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
        return aperturaMap.listaSolicitudCertificacionxProceso(id_proceso);
    }
    
    @PutMapping(value = "/registrareversion/{idDetalle}")
    public List<PResultado001> getRegistraRevision(@PathVariable Integer idDetalle,@Validated @RequestBody Solicitud dato) {
        //Integer id_usuario = 1; //cambiar ESTO ESTA CON UN ESTATICO
    	
        return aperturaMap.getRegistraRevision(idDetalle, dato.id_estado,dato.id_solicitud,(int) dato.id_usuario,dato.monto_revertido);
    }
    /*
    @PostMapping(value = "/actualizafoda")
    public List<Proceso> actualiza(@Validated @RequestBody Foda dato, BindingResult resultado) {
        if (resultado.hasErrors()) {
            throw new DatosInvalidosExcepciones(resultado);
        }
        return fodaMap.actualizaFoda(dato);
        //return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    */
}
