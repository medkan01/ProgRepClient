package modele.interfaceRMI;

import java.io.Serializable;

public class PartieAllumettes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int[] tabScore = new int[2];
	private int nbAllumettes;
	private int tour;
	
	public PartieAllumettes() {
	}

	public int[] getTabScore() {
		return tabScore;
	}

	public void setTabScore(int[] tabScore) {
		this.tabScore = tabScore;
	}

	public int getNbAllumettes() {
		return nbAllumettes;
	}

	public void setNbAllumettes(int nbAllumettes) {
		this.nbAllumettes = nbAllumettes;
	}

	public int getTour() {
		return tour;
	}

	public void setTour(int tour) {
		this.tour = tour;
	}
}
