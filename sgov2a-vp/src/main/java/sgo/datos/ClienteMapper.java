package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.Cliente;
import sgo.utilidades.Utilidades;
public class ClienteMapper implements RowMapper<Cliente>{
	public Cliente mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Cliente eCliente = null;
		try {
			eCliente = new Cliente();
			eCliente.setId(rs.getInt("id_cliente"));
			eCliente.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_corto")));
			eCliente.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social")));
			eCliente.setEstado(rs.getInt("estado"));
			eCliente.setNumeroContrato(Utilidades.cleanXSS(rs.getString("numero_contrato")));
			eCliente.setDescripcionContrato(Utilidades.cleanXSS(rs.getString("descripcion_contrato")));
			eCliente.setRuc(Utilidades.cleanXSS(rs.getString("ruc")));
			//Parametros de auditoria
			eCliente.setCreadoPor(rs.getInt("creado_por"));
			eCliente.setCreadoEl(rs.getLong("creado_el"));
			eCliente.setActualizadoPor(rs.getInt("actualizado_por"));
			eCliente.setActualizadoEl(rs.getLong("actualizado_el"));
			eCliente.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eCliente.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eCliente.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eCliente.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eCliente;
	}
}