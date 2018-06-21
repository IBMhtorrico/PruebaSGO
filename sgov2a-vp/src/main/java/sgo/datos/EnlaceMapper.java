package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.Enlace;
import sgo.entidad.Permiso;
import sgo.utilidades.Utilidades;
public class EnlaceMapper implements RowMapper<Enlace>{
	public Enlace mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Enlace eEnlace = null;
		Permiso ePermiso = null;
		try {
			eEnlace = new Enlace();
			eEnlace.setId(rs.getInt("id_enlace"));
			eEnlace.setTitulo(Utilidades.cleanXSS(rs.getString("titulo")));
			eEnlace.setUrlCompleta(Utilidades.cleanXSS(rs.getString("url_completa")));
			eEnlace.setUrlRelativa(Utilidades.cleanXSS(rs.getString("url_relativa")));
			eEnlace.setOrden(rs.getInt("orden"));
			eEnlace.setPadre(rs.getInt("padre"));
			eEnlace.setTipo(rs.getInt("tipo"));
			eEnlace.setPermiso(rs.getInt("id_permiso"));
			
			ePermiso = new Permiso();
			ePermiso.setId(rs.getInt("id_permiso"));
			ePermiso.setNombre(Utilidades.cleanXSS(rs.getString("desc_permiso")));
			eEnlace.setEntidadPermiso(ePermiso);
			
			//Parametros de auditoria
			eEnlace.setCreadoPor(rs.getInt("creado_por"));
			eEnlace.setCreadoEl(rs.getLong("creado_el"));
			eEnlace.setActualizadoPor(rs.getInt("actualizado_por"));
			eEnlace.setActualizadoEl(rs.getLong("actualizado_el"));
			eEnlace.setUsuarioActualizacion(rs.getString("usuario_actualizacion"));
			eEnlace.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eEnlace;
	}
}