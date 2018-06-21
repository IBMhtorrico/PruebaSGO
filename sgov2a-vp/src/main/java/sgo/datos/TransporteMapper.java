package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Cisterna;
import sgo.entidad.Conductor;
import sgo.entidad.Operacion;
import sgo.entidad.Planta;
import sgo.entidad.Transporte;
import sgo.entidad.Transportista;
import sgo.utilidades.Utilidades;
public class TransporteMapper implements RowMapper<Transporte>{
	public Transporte mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Transporte eTransporte = null;
		Conductor eConductor = null;
		Transportista eTransportista = null;
		Planta ePlanta1 = null;
		Operacion eOperacion = null;
		Cisterna eCisterna = null;
		try {
			eTransporte = new Transporte();
			eTransporte.setId(rs.getInt("id_transporte"));
			eTransporte.setNumeroGuiaRemision(Utilidades.cleanXSS(rs.getString("numero_guia_remision")));
			eTransporte.setIdPlantaDespacho(rs.getInt("planta_despacho"));
			eTransporte.setIdCliente(rs.getInt("cliente_transporte"));
			eTransporte.setIdConductor(rs.getInt("id_conductor"));
			eTransporte.setFechaEmisionGuia(rs.getDate("fecha_emision"));
			eTransporte.setIdCisterna(rs.getInt("id_cisterna"));
			eTransporte.setPlacaCisterna(Utilidades.cleanXSS(rs.getString("placa_cisterna")));
			eTransporte.setVolumenTotalObservado(rs.getFloat("volumen_total_observado"));
			eTransporte.setVolumenTotalCorregido(rs.getFloat("volumen_total_corregido"));
			eTransporte.setIdTransportista(rs.getInt("id_transportista"));
			eTransporte.setBreveteConductor(Utilidades.cleanXSS(rs.getString("brevete_conductor")));
			eTransporte.setNumeroOrdenCompra(Utilidades.cleanXSS(rs.getString("numero_orden_entrega")));
			eTransporte.setCodigoScop(Utilidades.cleanXSS(rs.getString("codigo_scop")));
			eTransporte.setEstado(rs.getInt("estado_transporte"));
			//eTransporte.setSincronizadoEl(rs.getLong("sincronizado_el"));
			eTransporte.setNumeroFactura(Utilidades.cleanXSS(rs.getString("numero_factura")));
			eTransporte.setIdPlantaRecepcion(rs.getInt("planta_recepcion"));
			eTransporte.setIdTracto(rs.getInt("id_tracto"));
			eTransporte.setPlacaTracto(Utilidades.cleanXSS(rs.getString("placa_tracto")));
			eTransporte.setTarjetaCubicacionCompartimento(Utilidades.cleanXSS(rs.getString("tarjeta_cubicacion_cisterna")));
			eTransporte.setCisternaTracto(Utilidades.cleanXSS(rs.getString("cisterna_tracto")));
			eTransporte.setPrecintosSeguridad(Utilidades.cleanXSS(rs.getString("precintos_seguridad")));
			eTransporte.setOrigen(Utilidades.cleanXSS(rs.getString("tipo_registro")));
			eTransporte.setPesoBruto(rs.getFloat("peso_bruto"));
			eTransporte.setPesoTara(rs.getFloat("peso_tara"));
			eTransporte.setPesoNeto(rs.getFloat("peso_neto"));
			
			eConductor = new Conductor();
			eConductor.setId(rs.getInt("id_conductor"));
			eConductor.setNombres(Utilidades.cleanXSS(rs.getString("nombresconductor")));
			eConductor.setApellidos(Utilidades.cleanXSS(rs.getString("apellidosconductor")));
			eTransporte.setConductor(eConductor);
			
			eTransportista = new Transportista();
			eTransportista.setId(rs.getInt("id_transportista"));
			eTransportista.setRazonSocial(Utilidades.cleanXSS(rs.getString("razonsocialtransportista")));
			eTransporte.setTransportista(eTransportista);
			
			ePlanta1 = new Planta();
			ePlanta1.setId(rs.getInt("planta_despacho"));
			ePlanta1.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcionPlantaDespacho")));
			eTransporte.setPlantaDespacho(ePlanta1);

			eOperacion = new Operacion();
			eOperacion.setId(rs.getInt("planta_recepcion"));
			eOperacion.setNombre(Utilidades.cleanXSS(rs.getString("nombre_planta_recepcion")));
			eOperacion.setAlias(Utilidades.cleanXSS(rs.getString("alias_planta_recepcion")));
			
			eTransporte.setPlantaRecepcion(eOperacion);
			
			eCisterna = new Cisterna();
			eCisterna.setId(rs.getInt("id_cisterna"));
			eCisterna.setPlaca(Utilidades.cleanXSS(rs.getString("placa_cisterna")));
			eCisterna.setIdTracto(rs.getInt("id_tracto"));
			eCisterna.setPlacaTracto(Utilidades.cleanXSS(rs.getString("placa_tracto")));
		    eCisterna.setCantidadCompartimentos(rs.getInt("cantidad_compartimentos"));
		
			eTransporte.setCisterna(eCisterna);
			
			//Parametros de auditoria
			eTransporte.setCreadoPor(rs.getInt("creado_por_transporte"));
			eTransporte.setCreadoEl(rs.getLong("creado_el_transporte"));
			eTransporte.setActualizadoPor(rs.getInt("actualizado_por_transporte"));
			eTransporte.setActualizadoEl(rs.getLong("actualizado_el_transporte"));
			eTransporte.setUsuarioActualizacion(Utilidades.cleanXSS(rs.getString("usuario_actualizacion_transporte")));
			eTransporte.setUsuarioCreacion(Utilidades.cleanXSS(rs.getString("usuario_creacion_transporte")));
			eTransporte.setIpCreacion(Utilidades.cleanXSS(rs.getString("ip_creacion_transporte")));
			eTransporte.setIpActualizacion(Utilidades.cleanXSS(rs.getString("ip_actualizacion_transporte")));
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eTransporte;
	}
}