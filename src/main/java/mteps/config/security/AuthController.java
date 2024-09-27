package mteps.config.security;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import mteps.config.security.entity.User;
import mteps.config.security.entity.LoginRequest;
import mteps.config.security.entity.SignupRequest;
import mteps.config.security.entity.JwtResponse;
import mteps.config.security.entity.MessageResponse;
import mteps.config.security.entity.loginResponse;
import mteps.config.security.jwt.JwtUtils;
import mteps.config.security.servicio.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/login")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;


	@Autowired
	@Qualifier("passwordEncoder")
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
		
		
		try {
		Authentication authentication = authenticationManager.authenticate(	new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();	
		//List<String> roles = userDetails.getAuthorities().stream()  LISTA DE  ROLES
		//	.map(item -> item.getAuthority())
			//.collect(Collectors.toList());
	JwtResponse jwtResponse = new JwtResponse(jwt,userDetails.getUsername(), userDetails.getEmail(),userDetails.getCod_departamentos());
	return ResponseEntity.ok(new loginResponse(true, HttpStatus.OK.value(),"Inicio de sesión correcto" ,jwtResponse));
		} catch (DisabledException e) {
			return ResponseEntity.ok(new loginResponse(false, 400,e.getMessage() ,null));
    } catch (BadCredentialsException e) {
    	return ResponseEntity.ok(new loginResponse(false, 400,"Credencial incorrecta" ,null));
    }
		
		
		
	}

	/*
	@PostMapping("/registrar")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByLogin(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Este nombre de usuario ya está en uso!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Correo electrónico ya está en uso!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: No se encuentra el rol."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: No se encuentra el rol."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: No se encuentra el rol."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: No se encuentra el rol."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Usuario registrado exitosamente!"));
	}
	*/
}
