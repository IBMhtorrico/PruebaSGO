package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Cliente;
import sgo.entidad.Operario;
import sgo.utilidades.Utilidades;
public class OperarioMapper implements RowMapper<Operario>{
	public Operario mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Operario eOperario = null;
		Cliente eCliente = null;
		try {
			eOperario = new Operario();
			eOperario.setId(rs.getInt("id_operario"));
			eOperario.setNombreOperario(Utilidades.cleanXSS(rs.getString("nombre_operario")));
			eOperario.setApellidoPaternoOperario(Utilidades.cleanXSS(rs.getString("apellido_paterno_operario")));
			eOperario.setApellidoMaternoOperario(Utilidades.cleanXSS(rs.getString("apellido_materno_operario")));
			eOperario.setDniOperario(Utilidades.cleanXSS(rs.getString("dni_operario")));
			eOperario.setEstado(rs.getInt("estado"));
			eOperario.setIdCliente(rs.getInt("id_cliente"));
			eOperario.setIndicadorOperario(rs.getInt("indicador_operario"));

			eCliente = new Cliente();
			eCliente.setId(rs.getInt("id_operario"));
			eCliente.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_corto")));
			eCliente.setRuc(Utilidades.cleanXSS(rs.getString("ruc")));
			eCliente.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social")));
			eOperario.setCliente(eCliente);
			
			//Parametros de auditoria
			eOperario.setCreadoPor(rs.getInt("creado_por"));
			eOperario.setCreadoEl(rs.getLong("creado_el"));
			eOperario.setActualizadoPor(rs.getInt("actualizado_por"));
			eOperario.setActualizadoEl(rs.getLong("actualizado_el"));
			eOperario.setUsuarioActualizacion(rs.getString("usuario_actualizacion"));
			eOperario.setUsuarioCreacion(rs.getString("usuario_creacion"));
			eOperario.setIpCreacion(rs.getString("ip_creacion"));
			eOperario.setIpActualizacion(rs.getString("ip_actualizacion"));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eOperario;
	}
}