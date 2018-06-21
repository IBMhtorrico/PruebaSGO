package sgo.datos;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.ClonacionProgTrans;

public class ClonacionProgTransMapper implements RowMapper<ClonacionProgTrans>{
	
	public ClonacionProgTrans mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		ClonacionProgTrans clonacionProgTrans = null;
		try {
			clonacionProgTrans = new ClonacionProgTrans();
			clonacionProgTrans.setIdTransporte(rs.getInt("id_transporte"));
			clonacionProgTrans.setIdDetalleTransporte(rs.getInt("id_dtransporte"));
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return clonacionProgTrans;
		
	}

}
