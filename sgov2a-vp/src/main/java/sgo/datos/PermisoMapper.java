package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Permiso;
import sgo.utilidades.Utilidades;

public class PermisoMapper implements RowMapper<Permiso>{
 	
	/*
	 * @author Rafael Reyna Camones
	 *  @param rs
	 * 
	 */
	public Permiso mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Permiso mPermiso = new Permiso();		
		mPermiso.setId(rs.getInt("id_permiso"));
		mPermiso.setNombre(Utilidades.cleanXSS(rs.getString("nombre")));
		mPermiso.setEstado(rs.getInt("estado"));	
		//Parametros de auditoria
		mPermiso.setCreadoPor(rs.getInt("creado_por"));
		mPermiso.setCreadoEl(rs.getLong("creado_el"));
		mPermiso.setActualizadoPor(rs.getInt("actualizado_por"));
		mPermiso.setActualizadoEl(rs.getLong("actualizado_el"));
		mPermiso.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
		mPermiso.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
		mPermiso.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
		mPermiso.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
		return mPermiso;
	}
}