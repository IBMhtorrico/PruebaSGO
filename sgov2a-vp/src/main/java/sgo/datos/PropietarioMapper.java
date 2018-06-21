package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Propietario;
import sgo.utilidades.Utilidades;
public class PropietarioMapper implements RowMapper<Propietario>{
	public Propietario mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Propietario ePropietario = null;
		try {
			ePropietario = new Propietario();
			ePropietario.setId(rs.getInt("id_propietario"));
			ePropietario.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social")));
			ePropietario.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_corto")));
			ePropietario.setRuc(Utilidades.cleanXSS(rs.getString("ruc")));
			ePropietario.setTipo(rs.getInt("tipo"));
			ePropietario.setEstado(rs.getInt("estado"));

			//Parametros de auditoria
			ePropietario.setCreadoPor(rs.getInt("creado_por"));
			ePropietario.setCreadoEl(rs.getLong("creado_el"));
			ePropietario.setActualizadoPor(rs.getInt("actualizado_por"));
			ePropietario.setActualizadoEl(rs.getLong("actualizado_el"));
			ePropietario.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			ePropietario.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			ePropietario.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			ePropietario.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return ePropietario;
	}
}