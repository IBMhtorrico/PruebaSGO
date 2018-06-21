package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Rol;
import sgo.utilidades.Utilidades;
public class RolMapper implements RowMapper<Rol>{
	public Rol mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Rol eRol = null;
		try {
			eRol = new Rol();
			eRol.setId(rs.getInt("id_rol"));
			eRol.setNombre(Utilidades.cleanXSS(rs.getString("nombre")));
			eRol.setEstado(rs.getInt("estado"));
			//Parametros de auditoria
			eRol.setCreadoPor(rs.getInt("creado_por"));
			eRol.setCreadoEl(rs.getLong("creado_el"));
			eRol.setActualizadoPor(rs.getInt("actualizado_por"));
			eRol.setActualizadoEl(rs.getLong("actualizado_el"));
			eRol.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eRol.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eRol.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eRol.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eRol;
	}
}