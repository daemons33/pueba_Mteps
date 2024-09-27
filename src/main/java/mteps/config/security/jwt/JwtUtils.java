package mteps.config.security.jwt;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;




import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import mteps.config.security.servicio.UserDetailsImpl;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${planpago.app.jwtSecret}")
	private String jwtSecret;

	@Value("${planpago.app.jwtExpirationMs}")
	private int jwtExpirationMs;

/*	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 31);
		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(c.getTime())
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
*/
	
	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(c.getTime())
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String generateJwtTokenDTicket(Authentication authentication) {
	    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

	    // Añadimos 1 hora a la fecha actual para obtener la fecha de expiración
	    Calendar expirationTime = Calendar.getInstance();
	    expirationTime.add(Calendar.HOUR_OF_DAY, 1);

	    return Jwts.builder()
	            .setSubject(userPrincipal.getUsername())
	            .setIssuedAt(new Date())
	            .setExpiration(expirationTime.getTime())
	            .signWith(SignatureAlgorithm.HS512, jwtSecret)
	            .compact();
	}
	
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Firma JWT no válida: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Token no válido: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("El token ha caducado: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("El token no es compatible: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("La cadena de reclamaciones está vacía: {}", e.getMessage());
		}

		return false;
	}

}
