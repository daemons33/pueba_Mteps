package mteps.repo_nfs;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/repoNfs")
public class RepoNfsREST {
	
	@Value("${dbovt.url}")
	private String db_url;

	@Value("${dbovt.usuario}")
	private String db_usuario;

	@Value("${dbovt.password}")
	private String db_password;
	
	@Value("${ruta.archivos}")
	private String rutaprincipal;
	
	


}
