package sgo.datos;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sgo.entidad.Cisterna;
import sgo.entidad.Conductor;
import sgo.entidad.DetalleProgramacion;
import sgo.entidad.DiaOperativo;
import sgo.entidad.Planta;
import sgo.entidad.Producto;
import sgo.entidad.Programacion;
import sgo.entidad.Tracto;
import sgo.entidad.Transportista;
import sgo.utilidades.Utilidades;
public class ReporteProgramacionMapper implements RowMapper<DetalleProgramacion>{
	public DetalleProgramacion mapRow(ResultSet rs, int arg1) throws SQLException 
	{
		Programacion eProgramacion = null;
		DiaOperativo eDiaOperativo = null;		
		DetalleProgramacion eDetalleProgramacion = null;
		Producto eProducto = null;
		Conductor eConductor = null;		
		Cisterna eCisterna = null;
		Tracto eTracto = null;
		Transportista eTransportista=null;
		Planta ePlanta = null;
		try {
					eProgramacion = new Programacion();
			eDiaOperativo=new DiaOperativo();
			eDiaOperativo.setId(rs.getInt("id_doperativo"));			
			eDiaOperativo.setFechaOperativa(rs.getDate("fecha_operativa"));
			eDiaOperativo.setFechaEstimadaCarga(rs.getDate("fecha_estimada_carga"));
			eProgramacion.setDiaOperativo(eDiaOperativo);			
			eDetalleProgramacion=new DetalleProgramacion();
			eDetalleProgramacion.setOrdenCompra(Utilidades.cleanXSS(rs.getString("orden_compra")));	
			//Agregado por req 9000002608 para Programacion=============================
			eDetalleProgramacion.setNumeroCompartimiento(rs.getInt("numero_compartimento"));
			eDetalleProgramacion.setCapacidadVolumetrica(rs.getInt("capacidad_volumetrica"));
			//==========================================================================
			//Comentado por req 9000002608 para Programacion=============================
//			eDetalleProgramacion.setCapacidadCisternaTotal(rs.getFloat("capacidad_cisterna"));
			//==========================================================================
			eProducto=new Producto();
			eProducto.setNombre(Utilidades.cleanXSS(rs.getString("nombre_producto")));			
			eDetalleProgramacion.setProducto(eProducto);			
			eDetalleProgramacion.setProgramacion(eProgramacion);
			eProgramacion.setId(rs.getInt("id_programacion"));
			
			eTransportista=new Transportista();
			eTransportista.setRazonSocial(Utilidades.cleanXSS(rs.getString("razon_social")));			
			eProgramacion.setTransportista(eTransportista);
			
			//Comentado por req 9000002608 para Programacion=============================
//			eProgramacion.setTotalVolumenCisterna(rs.getFloat("total_capacidad_cisterna"));
			//==========================================================================
			eDetalleProgramacion.setCodigoScop(Utilidades.cleanXSS(rs.getString("codigo_scop")));
			
			eTracto = new Tracto();
			eTracto.setPlaca(Utilidades.cleanXSS(rs.getString("placa_tracto")));
			eTracto.setCodigoReferencia(Utilidades.cleanXSS(rs.getString("codigo_sap_unidad")));
			
			ePlanta = new Planta();
			ePlanta.setId(rs.getInt("id_planta"));
			ePlanta.setDescripcion(Utilidades.cleanXSS(rs.getString("descripcion_planta")));
			
			eCisterna=new Cisterna();
			eCisterna.setTarjetaCubicacion(Utilidades.cleanXSS(rs.getString("tarjeta_cubicacion")));
			eCisterna.setTracto(eTracto);			
			eCisterna.setPlaca(Utilidades.cleanXSS(rs.getString("placa")));
			eDetalleProgramacion.setCisterna(eCisterna);
			
			eConductor = new Conductor();
			eConductor.setBrevete(Utilidades.cleanXSS(rs.getString("brevete")));
			eConductor.setApellidos(Utilidades.cleanXSS(rs.getString("apellidos")));
			eConductor.setNombres(Utilidades.cleanXSS(rs.getString("nombres")));
			
			eDetalleProgramacion.setConductor(eConductor);
			eDetalleProgramacion.setCodigoSapPedido(Utilidades.cleanXSS(rs.getString("codigo_sap_pedido")));			
			eDetalleProgramacion.setProgramacion(eProgramacion);
			eDetalleProgramacion.setPlanta(ePlanta);

		} catch(Exception ex){
			ex.printStackTrace();
		}
		return eDetalleProgramacion;
	}
}