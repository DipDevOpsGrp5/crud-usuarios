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
		String nombre = request.getParameter("nombre");
		String rut = request.getParameter("rut");
		String correo = request.getParameter("correo");
		String password = request.getParameter("password");
        JsonObject respuesta = new JsonObject();
        Gson gson = new Gson();

		try {
			
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        session.beginTransaction();

	        Usuario user = new Usuario();
	        user.setNombre(nombre);
	        user.setRut(rut);
	        user.setCorreo(correo);
	        user.setPassword(password);

	        session.save(user);
	 
	        session.getTransaction().commit();
	        //HibernateUtil.shutdown();
            respuesta.addProperty("resultado", 0);
		}
        catch(Exception e) {
            respuesta.addProperty("resultado", -1);
            respuesta.addProperty("respuesta.", e.getMessage());

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
}
