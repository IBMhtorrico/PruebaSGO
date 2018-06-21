package sgo.servlet;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import com.pe.petroperu.petrosecure.controller.LoginController;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.DefaultTextProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;

public class CaptchaGenServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String FILE_TYPE = "jpeg";
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
		
		//logger.debug("CaptchaGenServlet - doPost");
		

		List<Color> textColors = Arrays.asList(
			     Color.BLACK);
		List<Font> textFonts = Arrays.asList(
		     new Font("Arial", Font.BOLD, 30),
		     new Font("Courier", Font.BOLD, 30));
		
		Captcha.Builder builder = new Captcha.Builder(115, 35).addText(new DefaultTextProducer(), 
		        new DefaultWordRenderer(textColors, textFonts)).addNoise();
		
		int random = (new Random()).nextInt(10) + 1;
		
		if(random%2 == 0) {
			builder.gimp();
		}
		
		Captcha captcha = builder.build();
		
		/* esta captcha se genera al ser invocado como imágen 
		 * se debe cuidar de no invalidar la sesion luego de generarlo 
		 */
		request.getSession(true).setAttribute(Captcha.NAME, captcha);
		
		CaptchaServletUtil.writeImage(response, captcha.getImage());
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
        doPost(request, response);
    }
}

