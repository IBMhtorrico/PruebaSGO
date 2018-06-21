package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Autorizacion;
import sgo.entidad.AutorizacionUsuario;
import sgo.entidad.Usuario;
import sgo.utilidades.Utilidades;
public class AutorizacionUsuarioMapper implements RowMapper<AutorizacionUsuario>{
	public AutorizacionUsuario mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		AutorizacionUsuario eAutorizacionUsuario = null;
		Usuario eUsuario = null;
		Autorizacion eAutorizacion = null;
		try {
			eAutorizacionUsuario = new AutorizacionUsuario();
			eAutorizacionUsuario.setId(rs.getInt("id_ausuario"));
			eAutorizacionUsuario.setIdUsuario(rs.getInt("id_usuario"));
			eAutorizacionUsuario.setIdAutorizacion(rs.getInt("id_autorizacion"));
			eAutorizacionUsuario.setCodigoAutorizacion(Utilidades.cleanXSS(rs.getString("codigo_autorizacion")));
			eAutorizacionUsuario.setIdentidad(Utilidades.cleanXSS(rs.getString("identidad")));
			
			eAutorizacionUsuario.setVigenteDesde(rs.getDate("vigente_desde"));
			eAutorizacionUsuario.setVigenteHasta(rs.getDate("vigente_hasta"));
			eAutorizacionUsuario.setEstado(rs.getInt("estado"));
			
			eUsuario = new Usuario();
			eUsuario.setId(rs.getInt("id_usuario"));
			eUsuario.setIdentidad(Utilidades.cleanXSS(rs.getString("identidad")));
			eUsuario.setNombre(Utilidades.cleanXSS(rs.getString("nombre")));
			eAutorizacionUsuario.seteUsuario(eUsuario);
			
			eAutorizacion = new Autorizacion();
			eAutorizacion.setId(rs.getInt("id_autorizacion"));
			eAutorizacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_autorizacion")));
			eAutorizacion.setCodigoInterno(Utilidades.cleanXSS(rs.getString("codigo_interno")));
			eAutorizacion.setEstado(rs.getInt("estado_autorizacion"));
			eAutorizacionUsuario.seteAutorizacion(eAutorizacion);
			
			//Parametros de auditoria
			eAutorizacionUsuario.setCreadoPor(rs.getInt("creado_por"));
			eAutorizacionUsuario.setCreadoEl(rs.getLong("creado_el"));
			eAutorizacionUsuario.setActualizadoPor(rs.getInt("actualizado_por"));
			eAutorizacionUsuario.setActualizadoEl(rs.getLong("actualizado_el"));
			eAutorizacionUsuario.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eAutorizacionUsuario.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eAutorizacionUsuario.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eAutorizacionUsuario.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));

		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eAutorizacionUsuario;
	}
}