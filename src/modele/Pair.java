package modele;

import javafx.beans.property.SimpleStringProperty;

public class Pair {
	private SimpleStringProperty uuid;
	private SimpleStringProperty joueur;
	
	public Pair(String uuid, String joueur) {
		this.uuid = new SimpleStringProperty(uuid);
		this.joueur = new SimpleStringProperty(joueur);
	}

	public String getUuid() {return uuid.get();}

	public void setUuid(String uuid) {this.uuid.set(uuid);}

	public String getJoueur() {return joueur.get();}

	public void setJoueur(String joueur) {this.joueur.set(joueur);}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (joueur.get() == null) {
			if (other.joueur.get() != null)
				return false;
		} else if (!joueur.get().equals(other.joueur.get()))
			return false;
		if (uuid.get() == null) {
			if (other.uuid.get() != null)
				return false;
		} else if (!uuid.get().equals(other.uuid.get()))
			return false;
		return true;
	}
}