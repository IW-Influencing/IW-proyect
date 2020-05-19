package es.ucm.fdi.iw.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@NamedQueries({
	@NamedQuery(name="Usuario.byNombreCuenta",
	query="SELECT u FROM Usuario u "
			+ "WHERE u.nombreCuenta = :nombreCuenta AND u.activo = 1"),
	
	@NamedQuery(name="Usuario.hasNombre",
	query="SELECT COUNT(u) "
			+ "FROM Usuario u "
			+ "WHERE u.nombre = :nombre"),
		
	@NamedQuery(name="Usuario.searchByNombre",
	query="SELECT u FROM Usuario u "
			+ "WHERE u.nombre LIKE :nombre AND u.roles NOT LIKE '%ADMIN%' "),
	
	@NamedQuery(name="Usuario.getAllUsers",
	query="SELECT u FROM Usuario u WHERE u.roles NOT LIKE '%ADMIN%' "),
	
	@NamedQuery(name="Usuario.searchByTag",
	query="SELECT u FROM Usuario u "
			+ "WHERE u.tags LIKE :tag  AND u.roles NOT LIKE '%ADMIN%' ")

})

@Table(uniqueConstraints={@UniqueConstraint(columnNames={"nombreCuenta"})})
public class Usuario {

	
	private static Logger log = LogManager.getLogger(Usuario.class);	
	private static PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	private long id; // Id unico para cada usuario
	private byte activo;
	@Column(name = "nombreCuenta")
	private String nombreCuenta;
	private String password;
	public enum Rol {ADMIN, INFLUENCER, EMPRESA, USER};
	private String roles; // Aqui se almacena el rol que tiene el usuario en la aplicación mediante números
	private String nombre;
	private String apellidos;
	private int edad;
	private String tags; // Se almacenan los tags
	private int num_contrataciones;
	private Float score;



	@OneToMany(targetEntity=Propuesta.class)
	@JoinColumn(name="empresa_id")
	public List<Propuesta> getPropuestasPropias() {
		return propuestasPropias;
	}

	public void setPropuestasPropias(List<Propuesta> propuestasPropias) {
		this.propuestasPropias = propuestasPropias;
	}
	
	@OneToMany(targetEntity=Candidatura.class)
	@JoinColumn(name="candidato_id")
	public List<Candidatura> getCandidaturas() {
		return candidaturas;
	}

	public void setCandidaturas(List<Candidatura> candidaturas) {
		this.candidaturas = candidaturas;
	}

	private List<Propuesta> propuestasPropias;
	private List<Candidatura> candidaturas;

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	/**
	 * Tests a raw (non-encoded) password against the stored one.
	 * @param rawPassword to test against
	 * @return true if encoding rawPassword with correct salt (from old password)
	 * matches old password. That is, true iff the password is correct  
	 */
	public boolean passwordMatches(String rawPassword) {
		return encoder.matches(rawPassword, this.password);
	}

	/**
	 * Encodes a password, so that it can be saved for future checking. Notice
	 * that encoding the same password multiple times will yield different
	 * encodings, since encodings contain a randomly-generated salt.
	 * @param rawPassword to encode
	 * @return the encoded password (typically a 60-character string)
	 * for example, a possible encoding of "test" is 
	 * $2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
	 */
	public static String encodePassword(String rawPassword) {
		return encoder.encode(rawPassword);
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	
	public String getNombreCuenta() {
		return nombreCuenta;
	}

	public void setNombreCuenta(String nombreCuenta) {
		this.nombreCuenta = nombreCuenta;
	}

	public Float getScore(){
		return score;
	}

	public void setScore(Float score){
		this.score = score;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String contraseña) {
		this.password = contraseña;
	}

	public byte getActivo() {
		return activo;
	}

	public void setActivo(byte activo) {
		this.activo = activo;
	}

	
	
	public int getNum_contrataciones() {
		return num_contrataciones;
	}

	public void setNum_contrataciones(int num_contrataciones) {
		this.num_contrataciones = num_contrataciones;
	}

	/**
	 * Checks whether this user has a given role.
	 * @param role to check
	 * @return true iff this user has that role.
	 */
	public boolean hasRole(Rol role) {
		String roleName = role.name();
		return Arrays.stream(roles.split(","))
				.anyMatch(r -> r.equals(roleName));
	}

	public void updatePuntuacion(int puntuacion) {
		// TODO Auto-generated method stub
		this.num_contrataciones+=1;
		this.score = (this.score*(num_contrataciones-1) + puntuacion)/(num_contrataciones);
	}
	

}
