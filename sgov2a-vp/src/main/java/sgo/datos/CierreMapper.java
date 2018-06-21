package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Cierre;
import sgo.utilidades.Utilidades;
public class CierreMapper implements RowMapper<Cierre>{
	public Cierre mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Cierre eCierre = null;

		try {
			eCierre = new Cierre();
			eCierre.setId(rs.getInt("id_doperativo"));
			eCierre.setFechaOperativa(rs.getDate("fecha_operativa"));
			eCierre.setTotalAsignados(rs.getFloat("total_asignado"));
			eCierre.setTotalDescargados(rs.getFloat("total_descargado"));
			eCierre.setUltimaActualizacion(rs.getLong("ultima_actualizacion"));
			eCierre.setIdUsuario(rs.getInt("id_usuario"));
			eCierre.setNombreUsuario(Utilidades.cleanXSS(rs.getString("nombre_usuario")));
			eCierre.setNombreOperacion(Utilidades.cleanXSS(rs.getString("nombre_operacion")));
			eCierre.setNombreCliente(Utilidades.cleanXSS(rs.getString("nombre_cliente")));
			eCierre.setEstado(rs.getInt("estado"));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eCierre;
	}
}