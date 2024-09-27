package mteps.sistpoa.Servicios.impl;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class SubirarchivosImpl implements SubirarchivosInt {

    @Value("${ruta.documentos}")
    private Path ruta;
    @Value("${ruta.imagenes}")
    private Path imagen;

    @Override
    public void cargardocumento(Long id, MultipartFile archivo) {
        try {
            //Files.copy(archivo.getInputStream(), this.ruta.resolve(id.toString()+".pdf"));
            Files.copy(archivo.getInputStream(), this.ruta.resolve(id.toString()+".pdf"), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar el archivo. Error: " + e.getMessage());
        }
    }

    public Resource descargardocumento(String archivo) {
        try {
            Path file = ruta.resolve(archivo+".pdf");
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public UploadFileResponse cargarpdf(String nombre_archivo, MultipartFile archivo) {

        try {
            String tipo = nombre_archivo.split("_")[0];
            String id = (nombre_archivo.split("_")[1]).split("\\.")[0];
            Files.copy(archivo.getInputStream(), this.ruta.resolve(nombre_archivo), StandardCopyOption.REPLACE_EXISTING);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("documentos/descargarpdf/" + tipo + "/" + id)
                .toUriString();

        return new UploadFileResponse(nombre_archivo, fileDownloadUri, archivo.getContentType(), archivo.getSize());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar el archivo. Error: " + e.getMessage());
        }
    }

    public Resource descargarpdf(String archivo) {
        try {
            Path file = ruta.resolve(archivo);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer el archivo!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public UploadFileResponse cargarLogo(MultipartFile archivo) {

        try {
            Path direccion = Paths.get(".");
            String archivo_nombre = archivo.getOriginalFilename();
            System.out.println("nombre para logo =" + archivo_nombre);
            Files.copy(archivo.getInputStream(), direccion.resolve(archivo_nombre), StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri   = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/logo")
                    .toUriString();

            return new UploadFileResponse(archivo_nombre, fileDownloadUri, archivo.getContentType(), archivo.getSize());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar la imagen Logo. Error: " + e.getMessage());
        }
    }

    public Resource descargarLogo(String nombre) {
        try {
            //String nombre = "logo.jpg";
            Path direccion = Paths.get(".");
            Path file = direccion.resolve(nombre);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer la imagen Logo!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public UploadFileResponse cargarJpg(String nombre_archivo, MultipartFile archivo) {
        try {
            String tipo = nombre_archivo.split("_")[0];
            String id = (nombre_archivo.split("_")[1]).split("\\.")[0];
            Files.copy(archivo.getInputStream(), this.imagen.resolve(nombre_archivo), StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri   = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("documentos/imagen/" + tipo + "/" + id)
                    .toUriString();

            return new UploadFileResponse(nombre_archivo, fileDownloadUri, archivo.getContentType(), archivo.getSize());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo almacenar la imagen. Error: " + e.getMessage());
        }
    }

    public Resource descargarJpg(String archivo) {
        try {
            Path file = this.imagen.resolve(archivo);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se pudo leer la imagen!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}
