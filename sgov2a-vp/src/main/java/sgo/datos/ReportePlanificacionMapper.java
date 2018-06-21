package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Cliente;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Operacion;
import sgo.entidad.Planificacion;
import sgo.entidad.Producto;
import sgo.utilidades.Utilidades;
public class ReportePlanificacionMapper implements RowMapper<Planificacion>{
	public Planificacion mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		DiaOperativo eDiaOperativo = null;
		Planificacion ePlanificacion = null;		
		Producto eProducto = null;
		Operacion eOperacion = null;		
		Cliente eCliente = null;
		
		try {
			ePlanificacion = new Planificacion();
			ePlanificacion.setId(rs.getInt("id_planificacion"));
			eDiaOperativo=new DiaOperativo();
			eDiaOperativo.setId(rs.getInt("id_doperativo"));			
			eDiaOperativo.setFechaOperativa(rs.getDate("fecha_planificada"));
			eDiaOperativo.setFechaEstimadaCarga(rs.getDate("fecha_carga"));
			eCliente=new Cliente();
			eCliente.setId(rs.getInt("id_cliente"));
			eCliente.setNombreCorto(Utilidades.cleanXSS(rs.getString("cliente_nombre")));
			eCliente.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social")));
			eOperacion=new Operacion();
			eOperacion.setId(rs.getInt("id_operacion"));
			eOperacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_operacion")));
			eOperacion.setCorreoPara(Utilidades.cleanXSS(rs.getString("correoPara")));
			eOperacion.setCorreoCC(Utilidades.cleanXSS(rs.getString("correoCC")));
			eOperacion.setEta(rs.getInt("eta_origen"));
			eOperacion.setCliente(eCliente);
			eProducto=new Producto();
			eProducto.setId(rs.getInt("id_producto"));
			eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre_producto")));
			eDiaOperativo.setOperacion(eOperacion);
			ePlanificacion.setVolumenPropuesto(rs.getFloat("volumen_propuesto"));
			ePlanificacion.setVolumenSolicitado(rs.getFloat("volumen_solicitado"));
			ePlanificacion.setCantidadCisternas(rs.getInt("cantidad_cisternas"));
			ePlanificacion.setActualizadoEl(rs.getLong("actualizado_el"));
			ePlanificacion.setActualizadoPor(rs.getInt("actualizado_por"));
			ePlanificacion.setObservacion(Utilidades.cleanXSS(rs.getString("observacion")));
			ePlanificacion.setBitacora(Utilidades.cleanXSS(rs.getString("bitacora")));
			ePlanificacion.setProducto(eProducto);
			ePlanificacion.setDiaOperativo(eDiaOperativo);
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return ePlanificacion;
	}
}