package mteps.config.security.servicio;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import mteps.config.security.entity.User;



public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Integer id_usuario;

	private String username;

	private String email;
	private String cod_departamentos;
	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Integer id_usuario, String username, String password) {
		this.id_usuario = id_usuario;
		this.username = username;
		
		this.password = password;
		
	}

	public static UserDetailsImpl build(User user) {
		return new UserDetailsImpl(
				user.getIdusuario(), 
				user.getUsuario(), 
				user.getClave());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Integer getId_usuario() {
		return id_usuario;
	}

	public String getEmail() {
		return email;
	}
	
	public String getCod_departamentos() {
		return cod_departamentos;
	}
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id_usuario, user.id_usuario);
	}
}
