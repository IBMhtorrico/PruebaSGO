package sgo.negocio;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import sgo.datos.CisternaDao;
import sgo.datos.ConductorDao;
import sgo.datos.DiaOperativoDao;
import sgo.datos.VigenciaDao;
import sgo.entidad.Cisterna;
import sgo.entidad.Conductor;
import sgo.entidad.ParametrosListar;
import sgo.entidad.RespuestaCompuesta;
import sgo.entidad.Vigencia;

@Component
public class VigenciaBusiness {
	@Autowired
	private MessageSource gestorDiccionario;//Gestor del diccionario de mensajes para la internacionalizacion
	@Autowired
	private DiaOperativoDao dDiaOperativo;
	@Autowired
	private VigenciaDao dVigencia;
	@Autowired
	private CisternaDao dCisternaDao;
	@Autowired
	private ConductorDao dConductorDao;
	
	public String verificaVigenciaDocumento(ParametrosListar parametros,Locale locale) throws NoSuchMessageException, Exception{
		//Recuperar registros
		String valor=null;
		RespuestaCompuesta respuesta = null;
		respuesta = dVigencia.recuperarRegistros(parametros);
        if (respuesta.estado==false){        	
        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
        }
        StringBuilder documentos=new StringBuilder();
        if(respuesta.getContenido().getCarga()!=null && respuesta.getContenido().getCarga().size()>0){
        	for(Vigencia vigencia:(ArrayList<Vigencia>)respuesta.getContenido().getCarga()){	        	       
                Date fechaActual=dDiaOperativo.recuperarFechaActualDateSql("yyyy-MM-dd");
                if(vigencia.getFechaExpiracion().compareTo(fechaActual)<0){//documento caducado
                	if(vigencia.getIdEntidad()==Vigencia.DOCUMENTO_CISTERNA){
                		RespuestaCompuesta respuestaCisterna=dCisternaDao.recuperarRegistro(vigencia.getPerteneceA());
            	        if (respuestaCisterna.estado==false){        	
            	        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            	        }
                		Cisterna cisterna=(Cisterna)respuestaCisterna.contenido.getCarga().get(0);
                		documentos.append("El Documento :"+vigencia.getDocumento().getNombreDocumento()+" de la Cisterna "+cisterna.getPlacaCisternaTracto()+" se encuentra caducado, fecha de expiración "+vigencia.getFechaExpiracion());
                	}else if(vigencia.getIdEntidad()==Vigencia.DOCUMENTO_CONDUCTOR){
                		RespuestaCompuesta respuestaConductor= dConductorDao.recuperarRegistro(vigencia.getPerteneceA());
            	        if (respuestaConductor.estado==false){        	
            	        	throw new Exception(gestorDiccionario.getMessage("sgo.recuperarFallido",null,locale));
            	        }
            	        Conductor conductor=(Conductor)respuestaConductor.contenido.getCarga().get(0);
                		documentos.append("El Documento :"+vigencia.getDocumento().getNombreDocumento()+" del Conductor "+conductor.getNombreCompleto()+" se encuentra caducado, fecha de expiración "+vigencia.getFechaExpiracion());
                	}
                }
        	}
        }
        valor=documentos.toString();
        return valor;
	}
}
