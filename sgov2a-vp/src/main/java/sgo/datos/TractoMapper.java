package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Tracto;
import sgo.entidad.Transportista;
import sgo.utilidades.Utilidades;
public class TractoMapper implements RowMapper<Tracto>{
	public Tracto mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Tracto eTracto = null;
		Transportista eTransportista = null;
		try {
			eTracto = new Tracto();
			eTracto.setId(rs.getInt("id_tracto"));
			eTracto.setPlaca(Utilidades.cleanXSS(rs.getString("placa")));
			eTracto.setEstado(rs.getInt("estado"));
			eTracto.setIdTransportista(rs.getInt("id_transportista"));
			eTracto.setRazonSocialTransportista(Utilidades.cleanXSS(rs.getString("razon_social_transportista")));
			//eTracto.setSincronizadoEl(Utilidades.cleanXSS(rs.getString("sincronizado_el"));
			//eTracto.setFechaReferencia(Utilidades.cleanXSS(rs.getString("fecha_referencia"));
			eTracto.setCodigoReferencia(Utilidades.cleanXSS(rs.getString("codigo_referencia")));
			eTransportista = new Transportista(); 
			eTransportista.setId(rs.getInt("id_transportista"));
			eTransportista.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social_transportista")));
			eTracto.setTransportista(eTransportista);
			//Parametros de auditoria
			eTracto.setCreadoPor(rs.getInt("creado_por"));
			eTracto.setCreadoEl(rs.getLong("creado_el"));
			eTracto.setActualizadoPor(rs.getInt("actualizado_por"));
			eTracto.setActualizadoEl(rs.getLong("actualizado_el"));
			eTracto.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eTracto.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eTracto.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eTracto.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eTracto;
	}
}