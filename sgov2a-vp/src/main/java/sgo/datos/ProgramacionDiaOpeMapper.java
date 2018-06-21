package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Cliente;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Operacion;
import sgo.entidad.Planta;
import sgo.utilidades.Utilidades;
public class ProgramacionDiaOpeMapper implements RowMapper<DiaOperativo>{
	public DiaOperativo mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		
		Operacion eOperacion = null;
		DiaOperativo diaOperativo = null;
		Cliente eCliente=null;
		Planta eplanta=null;
		try {
			
			
			diaOperativo=new DiaOperativo();
			diaOperativo.setId(rs.getInt("id_doperativo"));
			diaOperativo.setFechaEstimadaCarga(rs.getDate("fecha_estimada_carga"));
			diaOperativo.setFechaOperativa(rs.getDate("fecha_operativa"));
			diaOperativo.setTotalCisternasProgramados(rs.getInt("total_cisternas_prog"));
			diaOperativo.setTotalCisternasPlanificados(rs.getInt("total_cisternas_pla"));
			diaOperativo.setIdOperacion(rs.getInt("id_operacion"));			
			diaOperativo.setEstado(rs.getInt("estado"));

			//Parametros de auditoria
			diaOperativo.setActualizadoPor(rs.getInt("actualizado_por"));
			diaOperativo.setActualizadoEl(rs.getLong("actualizado_el"));
			diaOperativo.setCreadoEl(rs.getLong("creado_el"));
			diaOperativo.setCreadoPor(rs.getInt("creado_por"));
			diaOperativo.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			diaOperativo.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
			diaOperativo.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("nombre_usuario_creador")));
			diaOperativo.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("nombre_usuario_actualizador")));
			
			
			eOperacion = new Operacion();
			eOperacion.setId(rs.getInt("id_operacion"));
			eOperacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_operacion")));			
			eplanta=new Planta();
			eplanta.setId(rs.getInt("planta_despacho_defecto"));
			eplanta.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion_planta_despacho")));		
			eOperacion.setPlantaDespacho(eplanta);
			
			eCliente=new Cliente();			
			eCliente.setId(rs.getInt("id_cliente"));
			eCliente.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social_cliente")));
			eOperacion.setCliente(eCliente);
			diaOperativo.setOperacion(eOperacion);			
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return diaOperativo;
	}
}