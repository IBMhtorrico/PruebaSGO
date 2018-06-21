package sgo.datos;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.ProductoProgramacion;

public class ProductoProgramacionMapper implements RowMapper<ProductoProgramacion> {

	@Override
	public ProductoProgramacion mapRow(ResultSet rs, int arg1)
			throws SQLException {
		ProductoProgramacion eProductoProgramacion = null;
		try {
			eProductoProgramacion = new ProductoProgramacion();
			eProductoProgramacion.setId_operacion(rs.getInt("id_operacion"));
			eProductoProgramacion.setId_cliente(rs.getInt("id_cliente"));
			eProductoProgramacion.setId_producto(rs.getInt("id_producto"));
			eProductoProgramacion.setNombre(rs.getString("nombre"));
			eProductoProgramacion.setRazon_social(rs.getString("razon_social"));
			eProductoProgramacion.setAbreviatura(rs.getString("abreviatura"));
			eProductoProgramacion.setActualizado_por(rs.getString("actualizado_por"));
			eProductoProgramacion.setUsuario_actualizacion(rs.getString("usuario_actualizacion"));
			eProductoProgramacion.setIp_actualizacion(rs.getString("ip_actualizacion"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eProductoProgramacion;
	}

}
