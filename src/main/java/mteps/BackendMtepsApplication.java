package mteps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@ComponentScan({ "mteps"})
@EntityScan("mteps")
@EnableJpaRepositories("mteps")
public class BackendMtepsApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(BackendMtepsApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)   
	{  
	return application.sources(BackendMtepsApplication.class);  
	}  
	 

	
}
