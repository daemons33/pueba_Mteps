package mteps.config.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import mteps.config.security.jwt.AuthEntryPointJwt;
import mteps.config.security.jwt.AuthTokenFilter;
import mteps.config.security.servicio.UserDetailsServiceImpl;



@Configuration
@EnableAsync
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	@Lazy
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	//Configuracion para login MTEPS
	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {
	        @Override
	        public String encode(CharSequence charSequence) {
	            return getMd5(charSequence.toString());
	        }

	        @Override
	        public boolean matches(CharSequence charSequence, String s) {
	            return getMd5(charSequence.toString()).equals(s);
	        }
	    };
	}
	//Configuracion para login OVT
	 @Bean
	    public PasswordEncoder bCryptPasswordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	
	
	public static String getMd5(String input) {
	    try {
	        // Static getInstance method is called with hashing SHA
	        MessageDigest md = MessageDigest.getInstance("MD5");

	        // digest() method called
	        // to calculate message digest of an input
	        // and return array of byte
	        byte[] messageDigest = md.digest(input.getBytes());

	        // Convert byte array into signum representation
	        BigInteger no = new BigInteger(1, messageDigest);

	        // Convert message digest into hex value
	        String hashtext = no.toString(16);

	        while (hashtext.length() < 32) {
	            hashtext = "0" + hashtext;
	        }

	        return hashtext;
	    }

	    // For specifying wrong message digest algorithms
	    catch (NoSuchAlgorithmException e) {
	        System.out.println("Exception thrown"
	                + " for incorrect algorithm: " + e);
	        return null;
	    }
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().
			antMatchers("/login/**").permitAll().
			antMatchers("/planpago/reporte/**").permitAll()
			.antMatchers("/tickets/reporte/**").permitAll()
			.antMatchers("/comiteMixto/reporte/**").permitAll()
			.antMatchers("/tramites/reporte/**").permitAll()
			.antMatchers("/tramites/fondoCustodia/reporte/**").permitAll()
			.antMatchers("/repoNfs/reporte/**").permitAll()
			.antMatchers("/inspectoria/reporte/**").permitAll()
			.antMatchers("/consultas/reporte/**").permitAll()
			.antMatchers("/denuncias/reporte/**").permitAll()
			.antMatchers("/correo/**").permitAll()
			.antMatchers("/config/**").permitAll()
			.antMatchers("/sispoa/reporte/**").permitAll()
			.antMatchers("/poa/usuario/**").permitAll()
			.antMatchers("/sscdyd/crtf/**").permitAll()
			.antMatchers("/tramitesSitioWeb/calculomulta").permitAll()
			.antMatchers("/sin/*").permitAll()
			.antMatchers("/dticket/sin/**").permitAll()
			.antMatchers("/dticket/ci/**").permitAll()
			.antMatchers("/dticket/sup/**").permitAll()
			.antMatchers("/dticket/reporte/**").permitAll()
			.antMatchers("/rtep/auth/**").permitAll()

		  //.antMatchers("/**").permitAll()
			.anyRequest().authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Configuration
    public class CrossOriginConfig {
        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry
                            .addMapping("/**")
                            .allowedOrigins("*") // quitar 
                            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
                }
            };
        }

    }

}
