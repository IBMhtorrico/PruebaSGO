package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Autorizacion;
import sgo.utilidades.Utilidades;
public class AutorizacionMapper implements RowMapper<Autorizacion>{
	public Autorizacion mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Autorizacion eAutorizacion = null;
		try {
			eAutorizacion = new Autorizacion();
			eAutorizacion.setId(rs.getInt("id_autorizacion"));
			eAutorizacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_autorizacion")));
			eAutorizacion.setCodigoInterno(Utilidades.cleanXSS(rs.getString("codigo_interno_autorizacion")));
			eAutorizacion.setEstado(rs.getInt("estado_autorizacion"));
			
			//Parametros de auditoria
			eAutorizacion.setCreadoPor(rs.getInt("creado_por_autorizacion"));
			eAutorizacion.setCreadoEl(rs.getLong("creado_el_autorizacion"));
			eAutorizacion.setActualizadoPor(rs.getInt("actualizado_por_autorizacion"));
			eAutorizacion.setActualizadoEl(rs.getLong("actualizado_el_autorizacion"));
			eAutorizacion.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion_autorizacion")));
			eAutorizacion.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion_autorizacion")));
			eAutorizacion.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion_autorizacion")));
			eAutorizacion.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion_autorizacion")));
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eAutorizacion;
	}
}