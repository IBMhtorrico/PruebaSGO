package sgo.seguridad;

import java.sql.Date;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import sgo.entidad.Cliente;
import sgo.entidad.Rol;
import sgo.entidad.Operacion;
import sgo.entidad.Usuario;

public class AuthenticatedUserDetails extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	private String identidad;
	private String _email;
	private Rol _rol;
	private int _tipo;
	private String _zonaHoraria;
	private Operacion _operacion;
	private Cliente _cliente;
	private int _estado;
	
	private Date _actualizacion_clave;
	private int _intentos;
	private int _clave_temporal;

	public AuthenticatedUserDetails(Usuario fUsuario, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {  
		super(fUsuario.getNombre(), fUsuario.getClave().trim(),true, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);  
		this._id = fUsuario.getId();  
		this.identidad = fUsuario.getIdentidad();
		this._email = fUsuario.getEmail();
		this._tipo= fUsuario.getTipo();
		this._zonaHoraria = fUsuario.getZonaHoraria();
		this._rol= fUsuario.getRol();
		this._operacion = fUsuario.getOperacion();
		this._cliente = fUsuario.getCliente();
		this._estado = fUsuario.getEstado();
		this._actualizacion_clave = fUsuario.getActualizacionClave();
		this._intentos = fUsuario.getIntentos();
		this._clave_temporal = fUsuario.getClaveTemporal();
	}

	public String getNombre(){
		return super.getUsername();
	}
	
	public String getEmail(){
		return this._email;
	}
	
	public String getZonaHoraria(){
		return this._zonaHoraria;
	}
	
	
	public int getTipo(){
		return this._tipo;
	}
	public Rol getRol(){
		return this._rol;	
	}
	
	public Operacion getOperacion(){
		return this._operacion;	
	}
	
	public Cliente getCliente(){
		return this._cliente;	
	}
	
	public int getID(){
		return this._id;
	}

	public String getIdentidad() {
		return identidad;
	}

	public int getEstado() {
		return _estado;
	}

	public Date getActualizacionClave() {
		return _actualizacion_clave;
	}

	public void setActualizacionClave(Date actualizacionClave) {
		this._actualizacion_clave = actualizacionClave;
	}

	public int getIntentos() {
		return _intentos;
	}

	public void setIntentos(int intentos) {
		this._intentos = intentos;
	}

	public int getClaveTemporal() {
		return _clave_temporal;
	}
	
}