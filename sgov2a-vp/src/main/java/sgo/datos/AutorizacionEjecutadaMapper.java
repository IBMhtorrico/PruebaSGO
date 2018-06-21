package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.AutorizacionEjecutada;
import sgo.utilidades.Utilidades;
public class AutorizacionEjecutadaMapper implements RowMapper<AutorizacionEjecutada>{
	public AutorizacionEjecutada mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		AutorizacionEjecutada eAutorizacionEjecutada = null;
		try {
			eAutorizacionEjecutada = new AutorizacionEjecutada();
			eAutorizacionEjecutada.setId(rs.getInt("id_aejecutada"));
			eAutorizacionEjecutada.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion")));
			eAutorizacionEjecutada.setTipoRegistro(rs.getInt("tipo_registro"));
			eAutorizacionEjecutada.setIdRegistro(rs.getInt("id_registro"));
			eAutorizacionEjecutada.setEjecutadaEl(rs.getLong("ejecutada_el"));
			eAutorizacionEjecutada.setEjecutadaPor(rs.getInt("ejecutada_por"));
			eAutorizacionEjecutada.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eAutorizacionEjecutada.setIdAutorizacion(rs.getInt("id_autorizacion"));
			eAutorizacionEjecutada.setIdAutorizador(rs.getInt("id_autorizador"));
			eAutorizacionEjecutada.setVigenteDesde(rs.getDate("vigente_desde"));
			eAutorizacionEjecutada.setVigenteHasta(rs.getDate("vigente_hasta"));
			//esta tabla no tiene campos para la bitacora

		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eAutorizacionEjecutada;
	}
}