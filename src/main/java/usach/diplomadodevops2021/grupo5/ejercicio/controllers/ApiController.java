package usach.diplomadodevops2021.grupo5.ejercicio.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import usach.diplomadodevops2021.grupo5.ejercicio.dto.Usuario;
import usach.diplomadodevops2021.grupo5.ejercicio.utils.HibernateUtil;

@RestController
public class ApiController {

	@GetMapping("/")
	public String inicio() {
		return "Bienvenido al ejercicio numero  del diplomado de devops de usach.";
	}
	
	@PostMapping("/create")
	public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject respuesta = new JsonObject();
        Gson gson = new Gson();

		try {

			String nombre = validarNombre(request, response);
			
			String correo = validarCorreo(request, response);
			
			String password = validarPassword(request, response);
			
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        session.beginTransaction();
			String rut = validarRut(request, response, session);

	        Usuario user = new Usuario();
	        user.setNombre(nombre);
	        user.setRut(rut);
	        user.setCorreo(correo);
	        user.setPassword(password);
	        

	        session.save(user);
	 
	        session.getTransaction().commit();
            respuesta.addProperty("resultado", 0);
		}
        catch(Exception e) {
            respuesta.addProperty("error:", e.getMessage());

		}
		response.getWriter().append(gson.toJson(respuesta));
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
	}
	
	@GetMapping("/read")
	public void read(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String rut = request.getParameter("rut");
        JsonObject respuesta = new JsonObject();
        Gson gson = new Gson();

		try {
			
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        //session.beginTransaction();

	        Criteria critUsuario = session.createCriteria(Usuario.class);
	        Usuario usuario = (Usuario) critUsuario.add(Restrictions.eq("rut", rut))
                    .uniqueResult();
	        if (usuario == null) throw new Exception("Usuario no existe.");
	        
	        //session.getTransaction().commit();
	        //HibernateUtil.shutdown();
	        JsonObject usuarioJson = new JsonObject();
	        
	        usuarioJson.addProperty("id", usuario.getId());
	        usuarioJson.addProperty("nombre", usuario.getNombre());
	        usuarioJson.addProperty("rut", usuario.getRut());
	        usuarioJson.addProperty("correo", usuario.getCorreo());
	        usuarioJson.addProperty("password", usuario.getPassword());

            respuesta.addProperty("resultado", 0);
            respuesta.add("usuario", usuarioJson);

		}
        catch(Exception e) {
            respuesta.addProperty("resultado", -1);
            respuesta.addProperty("respuesta.", e.getMessage());
		}
		response.getWriter().append(gson.toJson(respuesta));
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
	}

	private String validarPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String password;
		if(request.getParameter("password").isEmpty()) {
			response.setStatus(400);
			throw new IOException("Password no puede estar vacío.");
		}
		password = request.getParameter("password");
		if(password.length() < 8) {
			response.setStatus(400);
			throw new IOException("Password debe contener al menos 8 caracteres.");
		}
		if(!isPasswordValid(password)) {
			response.setStatus(400);
			throw new IOException("Password debe contener al menos una letra [A-Z a-z] y un número [0-9].");
		}
		return password;
	}

	private String validarCorreo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String correo;
		if(request.getParameter("correo").isEmpty()) {
			response.setStatus(400);
			throw new IOException("Correo no puede estar vacío.");
		}
		correo = request.getParameter("correo");
		if(!isEmailValid(correo)) {
			response.setStatus(400);
			throw new IOException("'" + correo + "' no es un correo válido.");
		};
		return correo;
	}

	private String validarRut(HttpServletRequest request, HttpServletResponse response, Session session) throws IOException {
		String rut;
		if(request.getParameter("rut").isEmpty()) {
			response.setStatus(400);
			throw new IOException("Rut no puede estar vacío.");
		}
		rut = request.getParameter("rut");
		if(!isRutValid(rut)) {
			response.setStatus(400);
			throw new IOException("Rut no válido.");
		}
		
        Criteria critUsuario = session.createCriteria(Usuario.class);
        Usuario usuario = (Usuario) critUsuario.add(Restrictions.eq("rut", rut))
                .uniqueResult();
        if (usuario != null) {
        	response.setStatus(409);
        	throw new IOException("El Rut ingresado ya existe.");
        }
		
		return rut;
	}

	private String validarNombre(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String nombre;
		if(request.getParameter("nombre").isEmpty()) {
			response.setStatus(400);
			throw new IOException("Nombre no puede estar vacío.");
		}
		nombre = request.getParameter("nombre");
		return nombre;
	}
	
	boolean isEmailValid(String email) {
	      String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	      return email.matches(regex);
   }
	
	boolean isPasswordValid(String email) {
	      String regex = "^[a-zA-Z0-9]*$";
	      return email.matches(regex);
	}
	
	public boolean isRutValid(String rut) {
	    boolean validacion = false;
	    try {
	        rut =  rut.toUpperCase();
	        rut = rut.replace(".", "");
	        rut = rut.replace("-", "");
	        int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

	        char dv = rut.charAt(rut.length() - 1);

	        int m = 0, s = 1;
	        for (; rutAux != 0; rutAux /= 10) {
	            s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
	        }
	        if (dv == (char) (s != 0 ? s + 47 : 75)) {
	            validacion = true;
	        }

	    } 
	    catch (java.lang.NumberFormatException e) {
	    } 
	    catch (Exception e) {
	    }
	    return validacion;
	}
}
