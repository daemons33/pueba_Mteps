package mteps.sistpoa.Servicios.impl;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface SubirarchivosInt {

    public void cargardocumento(Long id, MultipartFile file);

    public Resource descargardocumento(String filename);

    public UploadFileResponse cargarpdf(String nombre_archivo,  MultipartFile file);

    public Resource descargarpdf(String nombre_archivo);

    public UploadFileResponse cargarJpg(String nombre_archivo,  MultipartFile file);

    public Resource descargarJpg(String nombre_archivo);

    public UploadFileResponse cargarLogo(MultipartFile file);

    public Resource descargarLogo(String nombre);

}