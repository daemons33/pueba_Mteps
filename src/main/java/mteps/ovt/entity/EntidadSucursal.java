package mteps.ovt.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class EntidadSucursal {
	@Id
    private Integer idSucursal;

	@Column(name = "fid_empresa")
    private Integer fidEmpresa;

    @Column(name = "fid_representante_legal")
    private Integer fidRepresentanteLegal;

    @Column(name = "nombre", length = 200)
    private String nombre;

    @Column(name = "nro_sucursal")
    private Integer nroSucursal;

    @Column(name = "avenida_calle", length = 255)
    private String avenidaCalle;

    @Column(name = "avenida_calle_referencia", length = 255)
    private String avenidaCalleReferencia;

    @Column(name = "numero", length = 15)
    private String numero;

    @Column(name = "zona", length = 255)
    private String zona;

    @Column(name = "uv", length = 255)
    private String uv;

    @Column(name = "manzana", length = 100)
    private String manzana;

    @Column(name = "edificio", length = 150)
    private String edificio;

    @Column(name = "piso", length = 100)
    private String piso;

    @Column(name = "nro_oficina", length = 15)
    private String nroOficina;

    @Column(name = "nro_casilla_postal", length = 10)
    private String nroCasillaPostal;

    @Column(name = "municipio", length = 150)
    private String municipio;

    @Column(name = "provincia", length = 150)
    private String provincia;

    @Column(name = "departamento", length = 11)
    private String departamento;

    @Column(name = "telefonos", columnDefinition = "text")
    private String telefonos;

    @Column(name = "telefonos_adicionales", columnDefinition = "text")
    private String telefonosAdicionales;

    @Column(name = "fax", columnDefinition = "text")
    private String fax;

    @Column(name = "correos", columnDefinition = "text")
    private String correos;

    @Column(name = "correos_adicionales", columnDefinition = "text")
    private String correosAdicionales;

    @Column(name = "estado", length = 255)
    private String estado;

    @Column(name = "tipo_sucursal", length = 2)
    private String tipoSucursal;

    @Column(name = "direccion_completa", columnDefinition = "text")
    private String direccionCompleta;

    @Column(name = "latitud", precision = 8, scale = 6)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 9, scale = 6)
    private BigDecimal longitud;

    @Column(name = "nro_patronal", length = 30)
    private String nroPatronal;

    @Column(name = "codigo_instancia", length = 50)
    private String codigoInstancia;

    @Column(name = "nombre_completo", length = 255)
    private String nombreCompleto;

    @Column(name = "tipo_documento", length = 255)
    private String tipoDocumento;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	public Date fechaSincronizacion;
	
	private String nroDocumento;
	
	public Integer getIdSucursal() {
		return idSucursal;
	}
	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}
	public Integer getNroSucursal() {
		return nroSucursal;
	}
	public void setNroSucursal(Integer nroSucursal) {
		this.nroSucursal = nroSucursal;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNroPatronal() {
		return nroPatronal;
	}
	public void setNroPatronal(String nroPatronal) {
		this.nroPatronal = nroPatronal;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getNroDocumento() {
		return nroDocumento;
	}
	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}
	public String getTipoSucursal() {
		return tipoSucursal;
	}
	public void setTipoSucursal(String tipoSucursal) {
		this.tipoSucursal = tipoSucursal;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getAvenidaCalle() {
		return avenidaCalle;
	}
	public void setAvenidaCalle(String avenidaCalle) {
		this.avenidaCalle = avenidaCalle;
	}
	public String getAvenidaCalleReferencia() {
		return avenidaCalleReferencia;
	}
	public void setAvenidaCalleReferencia(String avenidaCalleReferencia) {
		this.avenidaCalleReferencia = avenidaCalleReferencia;
	}
	public String getUv() {
		return uv;
	}
	public void setUv(String uv) {
		this.uv = uv;
	}
	public String getManzana() {
		return manzana;
	}
	public void setManzana(String manzana) {
		this.manzana = manzana;
	}
	public String getEdificio() {
		return edificio;
	}
	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getNroOficina() {
		return nroOficina;
	}
	public void setNroOficina(String nroOficina) {
		this.nroOficina = nroOficina;
	}
	public String getNroCasillaPostal() {
		return nroCasillaPostal;
	}
	public void setNroCasillaPostal(String nroCasillaPostal) {
		this.nroCasillaPostal = nroCasillaPostal;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getTelefonos() {
		return telefonos;
	}
	public void setTelefonos(String telefonos) {
		this.telefonos = telefonos;
	}
	public String getTelefonosAdicionales() {
		return telefonosAdicionales;
	}
	public void setTelefonosAdicionales(String telefonosAdicionales) {
		this.telefonosAdicionales = telefonosAdicionales;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getCorreos() {
		return correos;
	}
	public void setCorreos(String correos) {
		this.correos = correos;
	}
	public String getCorreosAdicionales() {
		return correosAdicionales;
	}
	public void setCorreosAdicionales(String correosAdicionales) {
		this.correosAdicionales = correosAdicionales;
	}
	public String getDireccionCompleta() {
		return direccionCompleta;
	}
	public void setDireccionCompleta(String direccionCompleta) {
		this.direccionCompleta = direccionCompleta;
	}
	public Date getFechaSincronizacion() {
		return fechaSincronizacion;
	}
	public void setFechaSincronizacion(Date fechaSincronizacion) {
		this.fechaSincronizacion = fechaSincronizacion;
	}
	
	
	
}
