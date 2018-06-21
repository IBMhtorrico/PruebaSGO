package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Operacion;
import sgo.entidad.Cliente;
import sgo.entidad.Planta;
import sgo.utilidades.Utilidades;
public class DiaOperativoMapper implements RowMapper<DiaOperativo>{
	public DiaOperativo mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		DiaOperativo eDiaOperativo = null;
		Operacion eOperacion = null;
		Cliente eCliente = null;
		Planta ePlanta = null;
		try {
   eDiaOperativo = new DiaOperativo();
   eDiaOperativo.setId(rs.getInt("id_doperativo"));
   eDiaOperativo.setFechaOperativa(rs.getDate("fecha_operativa"));
   eDiaOperativo.setFechaEstimadaCarga(rs.getDate("fecha_estimada_carga"));
   eDiaOperativo.setUltimaJornadaLiquidada(rs.getDate("ultima_jornada_liquidada"));
   eDiaOperativo.setIdOperacion(rs.getInt("id_operacion"));
   eDiaOperativo.setEstado(rs.getInt("estado"));
   eDiaOperativo.setTotalCisternas(rs.getInt("total_cisternas"));   
   eDiaOperativo.setTotalCisternasPlanificados(rs.getInt("total_planificados"));
   eDiaOperativo.setTotalCisternasAsignados(rs.getInt("total_asignados"));
   eDiaOperativo.setTotalCisternasDescargados(rs.getInt("total_descargados"));
   
   // Parametros de auditoria
   eDiaOperativo.setCreadoPor(rs.getInt("creado_por"));
   eDiaOperativo.setCreadoEl(rs.getLong("creado_el"));
   eDiaOperativo.setActualizadoPor(rs.getInt("actualizado_por"));
   eDiaOperativo.setActualizadoEl(rs.getLong("actualizado_el"));
   eDiaOperativo.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
   eDiaOperativo.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
   eDiaOperativo.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
   eDiaOperativo.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
   //Operacion
   eOperacion = new Operacion();
   eOperacion.setId(rs.getInt("id_operacion"));
   eOperacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_operacion")));
   eOperacion.setIdCliente(rs.getInt("id_cliente"));
   eOperacion.setIdPlantaDespacho(rs.getInt("planta_despacho_defecto"));
   
   //Planta de despacho
   ePlanta = new Planta();
   ePlanta.setId(rs.getInt("planta_despacho_defecto"));
   ePlanta.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion_planta_despacho")));
   eOperacion.setPlantaDespacho(ePlanta);
   
//Cliente
   eCliente = new Cliente();
   eCliente.setId(rs.getInt("id_cliente"));
   eCliente.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social_cliente")));
   eCliente.setNombreCorto(Utilidades.cleanXSS(rs.getString("nombre_corto_cliente")));
   eOperacion.setCliente(eCliente);

   eDiaOperativo.setOperacion(eOperacion);
   
   //9000002608==========================================
   eDiaOperativo.setCantidadCisternasPlan(rs.getInt("cantidad_cisternas_planificadas"));
   //====================================================
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eDiaOperativo;
	}
}