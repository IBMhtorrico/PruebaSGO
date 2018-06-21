package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Mensaje;
import sgo.utilidades.Utilidades;
public class MensajeMapper implements RowMapper<Mensaje>{
	public Mensaje mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Mensaje eMensaje = null;
		try {
			eMensaje = new Mensaje();
			eMensaje.setId(rs.getInt("id_mensaje"));
			eMensaje.setTitulo(Utilidades.cleanXSS(rs.getString("titulo")));
			//Parametros de auditoria
			eMensaje.setCreadoPor(rs.getInt("creado_por"));
			eMensaje.setCreadoEl(rs.getLong("creado_el"));
			eMensaje.setActualizadoPor(rs.getInt("actualizado_por"));
			eMensaje.setActualizadoEl(rs.getLong("actualizado_el"));
			eMensaje.setUsuarioActualizacion(rs.getString("usuario_actualizacion"));
			eMensaje.setUsuarioCreacion(rs.getString("usuario_creacion"));
			eMensaje.setIpCreacion(rs.getString("ip_creacion"));
			eMensaje.setIpActualizacion(rs.getString("ip_actualizacion"));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eMensaje;
	}
}