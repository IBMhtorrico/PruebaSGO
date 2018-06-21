package sgo.seguridad;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import sgo.datos.UsuarioDao;
import sgo.entidad.Rol;
import sgo.entidad.Usuario;

public class UserDetailsServiceImpl extends JdbcDaoImpl {
	//protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
    private UsuarioDao usuarioDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	//String errorMessage= "Usuario no encontrado";
    	String errorMessage= "Usuario o clave incorrecto.";
        Usuario mUsuario = usuarioDAO.getRecord(username);
        if (mUsuario == null) {
        	throw new UsernameNotFoundException(errorMessage);
        }       
        Rol mRol = mUsuario.getRol();
        Collection<SimpleGrantedAuthority> listaRoles = new ArrayList<SimpleGrantedAuthority>();
        listaRoles.add(new SimpleGrantedAuthority(mRol.getNombre()));
       	return new AuthenticatedUserDetails(mUsuario, true, true, true, listaRoles);
    }
}