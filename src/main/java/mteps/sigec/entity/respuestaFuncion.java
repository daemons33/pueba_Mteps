package mteps.sigec.entity;

import javax.persistence.Id;

public class respuestaFuncion {
@Id
public int id;
public String hojaRuta;

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getHojaRuta() {
	return hojaRuta;
}
public void setHojaRuta(String hojaRuta) {
	this.hojaRuta = hojaRuta;
}



}
