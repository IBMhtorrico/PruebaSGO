package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Estacion;
import sgo.entidad.Jornada;
import sgo.entidad.Operacion;
import sgo.entidad.OtroMovimiento;
import sgo.entidad.Planta;
import sgo.entidad.Tanque;
import sgo.utilidades.Utilidades;
public class OtroMovimientoMapper implements RowMapper<OtroMovimiento>{
	public OtroMovimiento mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		OtroMovimiento eOtroMovimiento = null;
		Jornada eJornada = null;
		Estacion eEstacion=null;
		Operacion eOperacion=null;
		Tanque eTanqueOrigen=null;
		Tanque eTanqueDestino=null;
		try {
			//entidad otroMovimiento
			eOtroMovimiento = new OtroMovimiento();
			eOtroMovimiento.setId(rs.getInt("id_omovimiento"));
			eOtroMovimiento.setTipoMovimiento(rs.getInt("tipo_movimiento"));
			eOtroMovimiento.setClasificacion(rs.getInt("clasificacion"));
			eOtroMovimiento.setIdJornada(rs.getInt("id_jornada"));
			eOtroMovimiento.setVolumen(rs.getInt("volumen"));
			eOtroMovimiento.setComentario(Utilidades.cleanXSS(rs.getString("comentario")));
			eOtroMovimiento.setIdTanqueOrigen(rs.getInt("id_tanque_origen"));
			eOtroMovimiento.setIdTanqueDestino(rs.getInt("id_tanque_destino"));
			eOtroMovimiento.setNumeroMovimiento(rs.getInt("numero_movimiento"));
			//entidad jornada
			eJornada = new Jornada();
			eJornada.setId(rs.getInt("id_estacion"));
			eJornada.setEstado(rs.getInt("estado_jornada"));
			eJornada.setIdOperario1(rs.getInt("operario1"));
			eJornada.setIdOperario2(rs.getInt("operario2"));
			eJornada.setComentario(Utilidades.cleanXSS(rs.getString("comentario_jornada")));
			eJornada.setFechaOperativa(rs.getDate("fecha_operativa"));
			//entidad estacion			 
			eEstacion=new Estacion();
			eEstacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_estacion")));
			eEstacion.setTipo(rs.getInt("tipo"));
			eEstacion.setEstado(rs.getInt("estado_estacion"));
			eEstacion.setMetodoDescarga(rs.getInt("metodo_descarga"));
			//entidad operacion 
			eOperacion=new Operacion();
			eOperacion.setId(rs.getInt("id_operacion"));
			eOperacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_operacion")));
			eOperacion.setIdCliente(rs.getInt("id_cliente"));
			eOperacion.setReferenciaPlantaRecepcion(Utilidades.cleanXSS(rs.getString("referencia_planta_recepcion")));
			eOperacion.setVolumenPromedioCisterna(rs.getFloat("volumen_promedio_cisterna"));
			eOperacion.setReferenciaDestinatarioMercaderia(Utilidades.cleanXSS(rs.getString("referencia_destinatario_mercaderia")));
			eOperacion.setEstado(rs.getInt("estado_operacion"));
			eOperacion.setAlias(Utilidades.cleanXSS(rs.getString("alias")));
			eOperacion.setFechaInicioPlanificacion(rs.getDate("fecha_inicio_planificacion"));
			eOperacion.setEta(rs.getInt("eta_origen"));			
			Planta plantaDespacho = new Planta();
			plantaDespacho.setId(rs.getInt("planta_despacho_defecto"));
			eOperacion.setPlantaDespacho(plantaDespacho);
			//entidad tanque ORIGEN
			eTanqueOrigen = new Tanque();
			//eTanqueOrigen.setId(rs.getInt("id_tanque_torigen"));
			eTanqueOrigen.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion_torigen")));
			eTanqueOrigen.setVolumenTotal(rs.getFloat("vtotal_torigen"));
			eTanqueOrigen.setVolumenTrabajo(rs.getFloat("vtrabajo_torigen"));
			eTanqueOrigen.setEstado(rs.getInt("estado_torigen"));
			eTanqueOrigen.setTipo(rs.getInt("estado_torigen"));
			eTanqueOrigen.setCertificadoCalibracion(rs.getInt("ccalibracion_torigen"));
			eTanqueOrigen.setFecha_emision_calibracion(rs.getDate("fecalibracion_torigen"));
			//entidad tanque DESTINO
			eTanqueDestino = new Tanque();
			eTanqueDestino.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion_tdestino")));
			eTanqueDestino.setVolumenTotal(rs.getFloat("vtotal_tdestino"));
			eTanqueDestino.setVolumenTrabajo(rs.getFloat("vtrabajo_tdestino"));
			eTanqueDestino.setEstado(rs.getInt("estado_tdestino"));
			eTanqueDestino.setTipo(rs.getInt("estado_tdestino"));
			eTanqueDestino.setCertificadoCalibracion(rs.getInt("ccalibracion_tdestino"));
			eTanqueDestino.setFecha_emision_calibracion(rs.getDate("fecalibracion_tdestino"));
			//asociaciones entre las entidades
			eEstacion.setOperacion(eOperacion);
			eJornada.setEstacion(eEstacion);
			eOtroMovimiento.setJornada(eJornada);
			eOtroMovimiento.setTanqueOrigen(eTanqueOrigen);
			eOtroMovimiento.setTanqueDestino(eTanqueDestino);
			//Parametros de auditoria
			eOtroMovimiento.setCreadoPor(rs.getInt("creado_por"));
			eOtroMovimiento.setCreadoEl(rs.getLong("creado_el"));
			eOtroMovimiento.setActualizadoPor(rs.getInt("actualizado_por"));
			eOtroMovimiento.setActualizadoEl(rs.getLong("actualizado_el"));
			eOtroMovimiento.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion")));
			eOtroMovimiento.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion")));
			eOtroMovimiento.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion")));
			eOtroMovimiento.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion")));
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eOtroMovimiento;
	}
}