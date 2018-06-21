package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.Conductor;
import sgo.entidad.Transportista;
import sgo.utilidades.Utilidades;
public class ConductorMapper implements RowMapper<Conductor>{
	public Conductor mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Conductor eConductor = null;
		Transportista eTransportista = null;
		try {
			eConductor = new Conductor();
			eConductor.setId(rs.getInt("id_conductor"));
			eConductor.setBrevete(Utilidades.cleanXSS(rs.getString("brevete")));
			eConductor.setApellidos(Utilidades.cleanXSS(rs.getString("apellidos")));
			eConductor.setNombres(Utilidades.cleanXSS(rs.getString("nombres")));
			eConductor.setDni(Utilidades.cleanXSS(rs.getString("dni")));
			eConductor.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
			//eConductor.setIdTransportista(rs.getInt("id_transportista"));
			eConductor.setEstado(rs.getInt("estado"));
			//eConductor.setSincronizadoEl(Utilidades.cleanXSS(rs.getString("sincronizado_el"));
			//eConductor.setFechaReferencia(Utilidades.cleanXSS(rs.getString("fecha_referencia"));
			//eConductor.setCodigoReferencia(Utilidades.cleanXSS(rs.getString("codigo_referencia"));

			/*eTransportista = new Transportista();
			eTransportista.setId(rs.getInt("id_transportista"));
			eTransportista.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social_transportista"));
			eConductor.setTransportista(eTransportista);*/
			
			//Parametros de auditoria
			eConductor.setCreadoPor(rs.getInt("creado_por"));
			eConductor.setCreadoEl(rs.getLong("creado_el"));
			eConductor.setActualizadoPor(rs.getInt("actualizado_por"));
			eConductor.setActualizadoEl(rs.getLong("actualizado_el"));
			eConductor.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eConductor.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eConductor.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eConductor.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
			
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eConductor;
	}
}