package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Contometro;
import sgo.entidad.Estacion;
import sgo.utilidades.Utilidades;
public class ContometroMapper implements RowMapper<Contometro>{
	public Contometro mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Contometro eContometro = null;
		Estacion eEstacion = null;
		try {
			eContometro = new Contometro();
			eContometro.setId(rs.getInt("id_contometro"));
			eContometro.setAlias(Utilidades.cleanXSS(rs.getString("alias")));
			eContometro.setEstado(rs.getInt("estado"));
			eContometro.setTipoContometro(rs.getInt("tipo_contometro"));
			eContometro.setIdEstacion(rs.getInt("id_estacion"));
			
			eEstacion = new Estacion();
			eEstacion.setId(rs.getInt("id_estacion"));
			eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
			eContometro.setEstacion(eEstacion);
			
			//Parametros de auditoria
			eContometro.setCreadoPor(rs.getInt("creado_por"));
			eContometro.setCreadoEl(rs.getLong("creado_el"));
			eContometro.setActualizadoPor(rs.getInt("actualizado_por"));
			eContometro.setActualizadoEl(rs.getLong("actualizado_el"));
			eContometro.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eContometro.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eContometro.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eContometro.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eContometro;
	}
}