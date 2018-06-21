package sgo.seguridad;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppVersion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public AppVersion() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		validationApp(request, response);
	}

	private void validationApp(HttpServletRequest req, HttpServletResponse resp){
		if(req.getRemoteAddr().equalsIgnoreCase("0:0:0:0:0:0:0:1") || req.getRemoteAddr().equalsIgnoreCase("127.0.0.1")) {
			String versionApp = "";
			String hostnameBD = "";
			String nombreBD= "";
			String userBD = "";
			ResourceBundle rb = ResourceBundle.getBundle("jdbc"); 
			if(rb != null) {
				versionApp = rb.getString("appVersion");
				hostnameBD = rb.getString("hostname");
				nombreBD= rb.getString("bd");
				userBD= rb.getString("userBD");
			}
			System.out.println("***************************************************************************************");
			System.out.println( "SGO versión:			       " + versionApp + " \n" +
								"Nombre de la Base de Datos:   " + nombreBD   + " \n" + 
								"Usuario de Base de Datos:     " + userBD 	  + " \n" +
								"Hostname de la Base de Datos: " + hostnameBD);
			System.out.println("***************************************************************************************");
		}
	}
}
