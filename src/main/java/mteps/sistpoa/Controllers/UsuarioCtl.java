package mteps.sistpoa.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import mteps.sistpoa.Mappers.PlanMap;
import mteps.sistpoa.Mappers.UsuarioMap;
import mteps.sistpoa.Models.Plan;
import mteps.sistpoa.Models.Usuario;
import mteps.sistpoa.Servicios.impl.UploadFileResponse;

import java.util.List;

@RestController
@RequestMapping("/usuarios")

public class UsuarioCtl {
    @Autowired
    UsuarioMap usuarioMap;

    @GetMapping("/listausuarios")
    public List<Usuario> getAll() {
        return usuarioMap.listaUsuarios();
    }

}
