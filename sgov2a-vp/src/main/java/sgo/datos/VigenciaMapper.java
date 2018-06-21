package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Documento;
import sgo.entidad.Vigencia;
import sgo.utilidades.Utilidades;

public class VigenciaMapper implements RowMapper<Vigencia>{
	public Vigencia mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Vigencia eVigencia = null;
		Documento eDocumento = null;
		try {
			eVigencia = new Vigencia();
			eVigencia.setId(rs.getInt("id_vigencia"));
			eVigencia.setIdDocumento(rs.getInt("id_documento"));
			eVigencia.setNumeroDocumento(Utilidades.cleanXSS(rs.getString("numero_documento")));
			eVigencia.setFechaEmision(rs.getDate("fecha_emision"));
			eVigencia.setFechaExpiracion(rs.getDate("fecha_expiracion"));
			eVigencia.setPerteneceA(rs.getInt("pertenece_a"));
			eVigencia.setIdEntidad(rs.getInt("id_entidad"));
			
			eDocumento = new Documento();
			eDocumento.setNombreDocumento(Utilidades.cleanXSS(rs.getString("nombre_documento")));
			eDocumento.setPeriodoVigencia(rs.getInt("periodo_vigencia"));
			eDocumento.setTiempoAlerta(rs.getInt("tiempo_alerta"));
			eVigencia.setDocumento(eDocumento);
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eVigencia;
	}
}