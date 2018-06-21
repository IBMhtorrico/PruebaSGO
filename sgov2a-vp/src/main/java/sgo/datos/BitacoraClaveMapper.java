package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Documento;
import sgo.utilidades.Utilidades;
public class BitacoraClaveMapper implements RowMapper<Documento>{
	public Documento mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Documento eDocumento = null;
		try {
			eDocumento = new Documento();
			eDocumento.setId(rs.getInt("id_documento"));
			eDocumento.setPerteneceA(rs.getInt("pertenece_a"));
			eDocumento.setPeriodoVigencia(rs.getInt("periodo_vigencia"));
			eDocumento.setTiempoAlerta(rs.getInt("tiempo_alerta"));
			eDocumento.setNombreDocumento(Utilidades.cleanXSS(rs.getString("nombre_documento")));
			//Parametros de auditoria
			eDocumento.setCreadoPor(rs.getInt("creado_por"));
			eDocumento.setCreadoEl(rs.getLong("creado_el"));
			eDocumento.setActualizadoPor(rs.getInt("actualizado_por"));
			eDocumento.setActualizadoEl(rs.getLong("actualizado_el"));
			eDocumento.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eDocumento.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eDocumento.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eDocumento.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eDocumento;
	}
}