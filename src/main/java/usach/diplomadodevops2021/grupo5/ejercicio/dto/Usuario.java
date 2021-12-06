package usach.diplomadodevops2021.grupo5.ejercicio.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;


@Entity
@OptimisticLocking (type = OptimisticLockType.NONE)
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id", "rut"})})
public class Usuario implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long usuarioId;
	
	@Column(name = "nombre", nullable = false)
    private String nombre;
	
	@Column(name = "rut", unique = true, nullable = false)
    private String rut;
	
	@Column(name = "correo", nullable = true)
    private String correo;
	
	@Column(name = "password", nullable = false)
    private String password;
	
	private static final long serialVersionUID = -4325619662486928743L;

	public void setNombre(String value) {
		nombre = value;
		
	}
	
	public void setRut(String value) {
		rut = value;
		
	}
	
	public void setCorreo(String value) {
		correo = value;
		
	}
	
	public void setPassword(String value) {
		password = value;
		
	}

	public Long getId() {
		// TODO Auto-generated method stub
		return usuarioId;
	}

	public String getNombre() {
		// TODO Auto-generated method stub
		return nombre;
	}
	
	public String getRut() {
		// TODO Auto-generated method stub
		return rut;
	}
	
	public String getCorreo() {
		// TODO Auto-generated method stub
		return correo;
	}
	
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
}
