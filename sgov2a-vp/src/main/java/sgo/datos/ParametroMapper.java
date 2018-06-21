package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Parametro;
import sgo.utilidades.Utilidades;
public class ParametroMapper implements RowMapper<Parametro>{
	public Parametro mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Parametro eParametro = null;
		try {
			eParametro = new Parametro();
			eParametro.setId(rs.getInt("id_parametro"));
			eParametro.setValor(Utilidades.cleanXSS(rs.getString("valor")));
			eParametro.setAlias(Utilidades.cleanXSS(rs.getString("alias")));
			//Parametros de auditoria
			eParametro.setCreadoPor(rs.getInt("creado_por"));
			eParametro.setCreadoEl(rs.getLong("creado_el"));
			eParametro.setActualizadoPor(rs.getInt("actualizado_por"));
			eParametro.setActualizadoEl(rs.getLong("actualizado_el"));
			eParametro.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eParametro.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eParametro.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eParametro.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eParametro;
	}
}