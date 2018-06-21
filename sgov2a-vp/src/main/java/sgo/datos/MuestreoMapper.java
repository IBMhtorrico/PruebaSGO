package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Muestreo;
import sgo.entidad.Producto;
import sgo.utilidades.Utilidades;
public class MuestreoMapper implements RowMapper<Muestreo>{
	public Muestreo mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Producto eProducto = null;
		Muestreo eMuestreo = null;
		try {
			eMuestreo = new Muestreo();
			eMuestreo.setId(rs.getInt("id_muestreo"));
			eMuestreo.setIdJornada(rs.getInt("id_jornada"));
			eMuestreo.setHoraMuestreo(rs.getTimestamp("hora_muestreo"));
			eMuestreo.setApiMuestreo(rs.getFloat("api_muestreo"));
			eMuestreo.setTemperaturaMuestreo(rs.getFloat("temperatura_muestreo"));
			eMuestreo.setFactorMuestreo(rs.getFloat("factor_muestreo"));
			eMuestreo.setProductoMuestreado(rs.getInt("producto_muestreado"));
			
			eProducto = new Producto();
			eProducto.setId(rs.getInt("producto_muestreado"));
			eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre")));
			eProducto.setAbreviatura(Utilidades.cleanXSS(rs.getString("abreviatura")));
			eProducto.setIndicadorProducto(rs.getInt("indicador_producto"));
			eMuestreo.setProducto(eProducto);

		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eMuestreo;
	}
}