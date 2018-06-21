 	

package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Producto;
import sgo.utilidades.Utilidades;
public class ProductoMapper implements RowMapper<Producto>{
	public Producto mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Producto eProducto = null;
		try {
			eProducto = new Producto();
			eProducto.setId(rs.getInt("id_producto"));
			eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre")));
			eProducto.setCodigoOsinerg(Utilidades.cleanXSS(rs.getString("codigo_osinerg")));
			eProducto.setAbreviatura(Utilidades.cleanXSS(rs.getString("abreviatura")));
			eProducto.setIndicadorProducto(rs.getInt("indicador_producto"));
			eProducto.setEstado(rs.getInt("estado"));
			//Parametros de auditoria
			eProducto.setCreadoPor(rs.getInt("creado_por"));
			eProducto.setCreadoEl(rs.getLong("creado_el"));
			eProducto.setActualizadoPor(rs.getInt("actualizado_por"));
			eProducto.setActualizadoEl(rs.getLong("actualizado_el"));
			eProducto.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eProducto.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			eProducto.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eProducto.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
			eProducto.setCodigoReferencia(rs.getString("codigo_referencia"));//7000001924
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eProducto;
	}
}