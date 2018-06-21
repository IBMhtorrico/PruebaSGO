package sgo.datos;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.BusquedaDetalleProgramado;

//Agregado por 9000002608
public class BusquedaDetalleProgramadoMapper implements RowMapper<BusquedaDetalleProgramado>{
	public BusquedaDetalleProgramado mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		BusquedaDetalleProgramado eBusquedaDetalleProgramado = null;
	    try {
	    	eBusquedaDetalleProgramado = new BusquedaDetalleProgramado();
	    	eBusquedaDetalleProgramado.setIdProducto(rs.getInt("id_producto"));
	    	eBusquedaDetalleProgramado.setNumeroCompartimento(rs.getInt("numero_compartimento"));
	    	eBusquedaDetalleProgramado.setCodigoReferencia(rs.getString("codigo_referencia"));
	     
	    } catch(Exception ex){
	      ex.printStackTrace();
	    }
	    return eBusquedaDetalleProgramado;
	}


}
