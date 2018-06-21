package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Estacion;
import sgo.entidad.Producto;
import sgo.entidad.Tanque;
import sgo.utilidades.Utilidades;
public class TanqueMapper implements RowMapper<Tanque>{
	public Tanque mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Tanque eTanque = null;
		Estacion eEstacion = null;
		Producto eProducto = null;
		try {
			eTanque = new Tanque();
			eTanque.setId(rs.getInt("id_tanque"));
			eTanque.setVolumenTotal(rs.getFloat("volumen_total"));
			eTanque.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion")));
			eTanque.setVolumenTrabajo(rs.getFloat("volumen_trabajo"));
			eTanque.setIdEstacion(rs.getInt("id_estacion"));
			eTanque.setEstado(rs.getInt("estado"));
			eTanque.setTipo(rs.getInt("tipo_tanque"));
			eTanque.setIdProducto(rs.getInt("id_producto"));
			
			eEstacion = new Estacion();
			eEstacion.setId(rs.getInt("id_estacion"));
			eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
			eEstacion.setIdOperacion(rs.getInt("id_operacion"));
			eTanque.setEstacion(eEstacion);
			
			eProducto = new Producto();
			eProducto.setId(rs.getInt("id_producto"));
			eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre_producto")));
			eProducto.setAbreviatura(Utilidades.cleanXSS(rs.getString("abreviatura")));
			eTanque.setProducto(eProducto);
			
			//Parametros de auditoria
			eTanque.setCreadoPor(rs.getInt("creado_por"));
			eTanque.setCreadoEl(rs.getLong("creado_el"));
			eTanque.setActualizadoPor(rs.getInt("actualizado_por"));
			eTanque.setActualizadoEl(rs.getLong("actualizado_el"));
			eTanque.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eTanque.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eTanque.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eTanque.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eTanque;
	}
}