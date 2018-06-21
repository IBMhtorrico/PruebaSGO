 	

package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Planta;
import sgo.utilidades.Utilidades;
public class PlantaMapper implements RowMapper<Planta>{
	public Planta mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Planta ePlanta = null; 
		try {
			ePlanta = new Planta();
			ePlanta.setId(rs.getInt("id_planta"));
			ePlanta.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion")));
//			ePlanta.setAbreviatura(Utilidades.cleanXSS(rs.getString("abreviatura"));
			ePlanta.setEstado(rs.getInt("estado"));
			ePlanta.setCorreoPara(Utilidades.cleanXSS(rs.getString("correoPara")));
			ePlanta.setCorreoCC(Utilidades.cleanXSS(rs.getString("correoCC")));
			//ePlanta.setSincronizadoEl(Utilidades.cleanXSS(rs.getString("sincronizado_el"));
			//ePlanta.setFechaReferencia(Utilidades.cleanXSS(rs.getString("fecha_referencia"));
			//ePlanta.setCodigoReferencia(Utilidades.cleanXSS(rs.getString("codigo_referencia"));
			//Parametros de auditoria
			ePlanta.setCreadoPor(rs.getInt("creado_por"));
			ePlanta.setCreadoEl(rs.getLong("creado_el"));
			ePlanta.setActualizadoPor(rs.getInt("actualizado_por"));
			ePlanta.setActualizadoEl(rs.getLong("actualizado_el"));
			ePlanta.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			ePlanta.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			ePlanta.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			ePlanta.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return ePlanta;
	}
}