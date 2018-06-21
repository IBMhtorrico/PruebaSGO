package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Cliente;
import sgo.entidad.Operacion;
import sgo.entidad.Rol;
import sgo.entidad.Transportista;
import sgo.entidad.Usuario;
import sgo.utilidades.Utilidades;
public class UsuarioMapper implements RowMapper<Usuario>{
 	
	/*
	 * @author Rafael Reyna Camones
	 *  @param rs
	 * 
	 */
	public Usuario mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Usuario mUsuario = null;
		try {
		mUsuario = new Usuario();
		mUsuario.setId(rs.getInt("id_usuario"));
		mUsuario.setNombre(Utilidades.cleanXSS(rs.getString("nombre")));
		mUsuario.setClave(Utilidades.cleanXSS(rs.getString("clave")));
		mUsuario.setIdentidad(Utilidades.cleanXSS(rs.getString("identidad")));
		mUsuario.setZonaHoraria(Utilidades.cleanXSS(rs.getString("zona_horaria")));
		mUsuario.setEstado(rs.getInt("estado"));
		mUsuario.setEmail(Utilidades.cleanXSS(rs.getString("email")));
		mUsuario.setTipo(rs.getInt("tipo"));
		mUsuario.setId_rol(rs.getInt("id_rol"));
		mUsuario.setId_operacion(rs.getInt("id_operacion"));
		mUsuario.setIdCliente(rs.getInt("id_cliente"));
		mUsuario.setIdTransportista(rs.getInt("id_transportista"));
		mUsuario.setActualizacionClave(rs.getDate("actualizacion_clave"));
		mUsuario.setIntentos(rs.getInt("intentos"));
		mUsuario.setClaveTemporal(rs.getInt("clave_temporal"));
		
		//Parametros de auditoria
		mUsuario.setCreadoPor(rs.getInt("creado_por"));
		mUsuario.setCreadoEl(rs.getLong("creado_el"));
		mUsuario.setActualizadoPor(rs.getInt("actualizado_por"));
		mUsuario.setActualizadoEl(rs.getLong("actualizado_el"));
		mUsuario.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
		mUsuario.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
		mUsuario.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
		mUsuario.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
		
		Rol mRol = new Rol();	
			mRol.setId(rs.getInt("id_rol"));
			mRol.setNombre(Utilidades.cleanXSS(rs.getString("nombre_rol")));
			mUsuario.setRol(mRol);
			
		Cliente mCliente = new Cliente();
			mCliente.setId(rs.getInt("id_cliente"));
			mCliente.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_cliente")));
			mUsuario.setCliente(mCliente);
			
		Operacion mOperacion = new Operacion();	
			mOperacion.setId(rs.getInt("id_operacion"));
			mOperacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_operacion")));
			mOperacion.setReferenciaPlantaRecepcion(Utilidades.cleanXSS(rs.getString("referencia_planta_recepcion")));
			mOperacion.setReferenciaDestinatarioMercaderia(Utilidades.cleanXSS(rs.getString("referencia_destinatario_mercaderia")));
			mOperacion.setVolumenPromedioCisterna(rs.getFloat("volumen_promedio_cisterna"));
			mUsuario.setOperacion(mOperacion);
			
		Transportista mTransportista = new Transportista();
			mTransportista.setId(rs.getInt("id_transportista"));
			mTransportista.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social")));
			mTransportista.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_corto")));
			mTransportista.setRuc(Utilidades.cleanXSS(rs.getString("ruc")));
			mUsuario.setTransportista(mTransportista);
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return mUsuario;
	}
}