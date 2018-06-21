package sgo.datos;
import java.util.Date;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.Evento;
import sgo.utilidades.Utilidades;

public class EventoMapper implements RowMapper<Evento>{
	public Evento mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Evento eEvento= null;

		try {
			eEvento = new Evento();
			eEvento.setId(rs.getInt("id_evento"));
			eEvento.setTipoEvento(rs.getInt("tipo_evento"));
			eEvento.setFechaHoraTimestamp(rs.getTimestamp("fecha_hora"));
			//esto para que la hora no se pierda al formatearlo a tipo Date
			DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date e = df2.parse(Utilidades.cleanXSS(rs.getString("fecha_hora").toString()));

			eEvento.setFechaHora(e);
			eEvento.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion")));
			eEvento.setTipoRegistro(rs.getInt("tipo_registro"));
			eEvento.setIdRegistro(rs.getInt("id_registro"));

			//Parametros de auditoria
			eEvento.setCreadoPor(rs.getInt("creado_por"));
			eEvento.setCreadoEl(rs.getLong("creado_el"));
			eEvento.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eEvento.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eEvento;
	}
}