package mteps.sistpoa.Servicios;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import mteps.sistpoa.Mappers.EntidadMap;
import mteps.sistpoa.Mappers.UsuarioMap;
import mteps.sistpoa.Pojos.PLogo;
import mteps.sistpoa.Servicios.impl.SubirarchivosInt;
import mteps.sistpoa.Servicios.impl.UploadFileResponse;

@RestController
@RequestMapping("/documentos")
public class DocumentoCtl {
    @Value("${ruta.documentos}")
    private String ruta_documentos;

    @Value("${ruta.imagenes}")
    private String ruta_imagenes;

    @Autowired
    SubirarchivosInt subirarchivosInt;

    @Autowired
    EntidadMap entidadMap;

    @Autowired
    UsuarioMap usuarioMap;

    @PostMapping(value = "/cargardocumento/{id}")
    ResponseEntity<?> cargar(@RequestParam("archivo") MultipartFile archivo, @PathVariable Long id) {
        subirarchivosInt.cargardocumento(id, archivo);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value="/descargardocumento/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> descargas(@PathVariable Long id) {
        Resource archivo = subirarchivosInt.descargardocumento(id.toString());
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"");
        return new ResponseEntity<Resource>(archivo, cabecera, HttpStatus.OK);
    }

    @PostMapping(value = "/cargarpdf/{tipo}/{id}")
    UploadFileResponse cargarPdf(@RequestParam("archivo") MultipartFile archivo, @PathVariable(value = "tipo") String tipo, @PathVariable(value = "id") Long id) {
        String nombre_archivo = tipo + "_" + id.toString() + ".pdf";
        UploadFileResponse subir = subirarchivosInt.cargarpdf(nombre_archivo, archivo);
        return subir;
    }

    @GetMapping(value="/descargarpdf/{tipo}/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> descargarPdf(@PathVariable(value = "tipo") String tipo, @PathVariable(value = "id") Long id) {
        String nombre_archivo = tipo + "_" + id.toString() + ".pdf";
        Resource archivo = subirarchivosInt.descargarpdf(nombre_archivo);
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"");
        return new ResponseEntity<Resource>(archivo, cabecera, HttpStatus.OK);
    }


    ////////////////////
    // CARGAR LOGO
    ////////////////////

    @PostMapping(value = "/cargarlogo")
    UploadFileResponse cargarLogo(@RequestParam("logo") MultipartFile logo) {
        Integer id_entidad = 1;
        entidadMap.actualizaLogo(id_entidad,logo.getOriginalFilename());
        UploadFileResponse subir = subirarchivosInt.cargarLogo(logo);
        return subir;
    }

    @GetMapping(value="/logo", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody ResponseEntity<?> descargarLogo() {
        Integer id_entidad = 1;
        PLogo logo = entidadMap.buscaLogo(id_entidad);
        Resource archivo = subirarchivosInt.descargarLogo(logo.getImagen_logo());
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"");
        return new ResponseEntity<Resource>(archivo, cabecera, HttpStatus.OK);
    }

    @PostMapping(value = "/cargarfirma/{id}")
    UploadFileResponse cargarFirma(@RequestParam("archivo") MultipartFile archivo, @PathVariable(value = "id") Integer id) {
        String nombre_archivo = "firma_" + id.toString() + ".jpg";
        usuarioMap.actualizaFirma(id, nombre_archivo);
        UploadFileResponse subir = subirarchivosInt.cargarJpg(nombre_archivo, archivo);
        return subir;
    }

    @GetMapping(value="/descargarfirma/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody ResponseEntity<?> descargarFirma(@PathVariable(value = "id") Long id) {
        String nombre_archivo = "firma_" + id.toString() + ".jpg";
        Resource archivo = subirarchivosInt.descargarJpg(nombre_archivo);
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"");
        return new ResponseEntity<Resource>(archivo, cabecera, HttpStatus.OK);
    }


    ////////////////////
    // CARGAR IMAGENES
    ////////////////////
    @PostMapping(value = "/cargarimagen/{tipo}/{id}")
    UploadFileResponse cargarImagen(@RequestParam("archivo") MultipartFile archivo, @PathVariable(value = "tipo") String tipo, @PathVariable(value = "id") Long id) {
        String nombre_archivo = tipo + "_" + id.toString() + ".jpg";

        UploadFileResponse subir = subirarchivosInt.cargarJpg(nombre_archivo, archivo);
        return subir;
    }

    @GetMapping(value="/descargarimagen/{tipo}/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> descargarImagen(@PathVariable(value = "tipo") String tipo, @PathVariable(value = "id") Long id) {
        String nombre_archivo = tipo + "_" + id.toString() + ".jpg";
        Resource archivo = subirarchivosInt.descargarJpg(nombre_archivo);
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"");
        return new ResponseEntity<Resource>(archivo, cabecera, HttpStatus.OK);
    }

}
