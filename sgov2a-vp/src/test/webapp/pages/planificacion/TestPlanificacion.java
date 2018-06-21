import static org.junit.Assert.*;

import org.elasticsearch.common.netty.handler.codec.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;

import sgo.servicio.PlanificacionControlador;


public class TestPlanificacion {

	@Before
	public void setUp() throws Exception {
	}

	HttpRequest httpRequest = new HttpRequest();
	
	xmlhttp.open("GET","xmlhttp_info.txt",true); 
	
	
	@Test
	public void test() {
		PlanificacionControlador pc = new PlanificacionControlador();
		pc.recuperarRegistros(httpRequest, locale)
	}
	
	
}
