package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Asignacion;
import sgo.utilidades.Utilidades;

public class AsignacionMapper implements RowMapper<Asignacion>{
	public Asignacion mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Asignacion eAsignacion = null;
		try {
			eAsignacion = new Asignacion();
			eAsignacion.setIdDoperativo(rs.getInt("id_doperativo"));
			eAsignacion.setIdTransporte(rs.getInt("id_transporte"));
			//datos de auditoria
			eAsignacion.setActualizadoEl(rs.getLong("actualizado_el"));
			eAsignacion.setActualizadoPor(rs.getInt("actualizado_por"));
			eAsignacion.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
			eAsignacion.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eAsignacion;
	}
}