package sgo.entidad;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sgo.datos.EnlaceDao;
import sgo.utilidades.Constante;
@Component
public class MenuGestor {
	@Autowired
	private EnlaceDao dEnlace;
	public RespuestaCompuesta Generar(int idRol, String urlActual){
		ArrayList<?>listaEnlaces = null;
		ArrayList<?>listaGrupos = null;
		ArrayList<Enlace>listaFinal = null;
		Contenido<Enlace> contenido = new Contenido<Enlace>();
		RespuestaCompuesta respuesta = null;
		Enlace eGrupoActual= null;
		Enlace eEnlaceActual=null;
		int numeroEnlaces =0;
		int numeroGrupos=0;
		try {
			listaFinal= new ArrayList<Enlace>();
			respuesta= dEnlace.recuperarMenu(Constante.ENLACE_TIPO_GRUPO,idRol);
			if (respuesta.estado==false){
				throw new Exception("No se encontro grupos");
			}
			listaGrupos= (ArrayList<?>) respuesta.contenido.carga;			
			numeroGrupos = listaGrupos.size();
			respuesta= dEnlace.recuperarMenu(Constante.ENLACE_TIPO_MENU,idRol);
			if (respuesta.estado==false){
				throw new Exception("No se encontro enlaces");
			}
			listaEnlaces =(ArrayList<?>) respuesta.contenido.carga;
			numeroEnlaces= listaEnlaces.size();			
			for(int indice =0;indice<numeroGrupos;indice++){
				eGrupoActual = (Enlace) listaGrupos.get(indice);
				for(int subindice=0;subindice<numeroEnlaces;subindice++){
					eEnlaceActual = (Enlace) listaEnlaces.get(subindice);
					if (eEnlaceActual.getPadre() == eGrupoActual.getId()){
						if (eEnlaceActual.getUrlCompleta().equals(urlActual)){							
							eEnlaceActual.setEnlaceActual(true);
							eGrupoActual.setEnlaceActual(true);
						}	
						eGrupoActual.agregarEnlace(eEnlaceActual);
					}
				}
				listaFinal.add(eGrupoActual);
			}	
			contenido.carga= listaFinal;
			respuesta.contenido= contenido;
			respuesta.estado=true;			
		} catch(Exception ex){
			ex.printStackTrace();
			respuesta.estado=false;
			respuesta.contenido=null;
		}
		return respuesta;
	}
}
