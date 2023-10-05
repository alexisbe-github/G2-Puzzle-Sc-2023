package main.java.model.partie;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.java.model.EDeplacement;
import main.java.model.Puzzle;
import main.java.model.joueur.Joueur;

public class PartieMultijoueurCooperative extends PartieMultijoueur {

	private Puzzle puzzleCommun;
	private int indexJoueurCourant; // index qui indique quel joueur de la List<Joueur> joueurs doit jouer son tour

	/**
	 * Construit une partie multijoueur cooperative se jouant tour par tour
	 * 
	 */
	public PartieMultijoueurCooperative() {
		indexJoueurCourant = 0;
		joueurs = new ArrayList<>();
		tableSocketDesJoueurs = new HashMap<>();
	}

	@Override
	public void lancerPartie(BufferedImage image, int taillePuzzle) {
		puzzleCommun = new Puzzle(taillePuzzle);
		for (Map.Entry<Joueur, Socket> mapEntry : tableSocketDesJoueurs.entrySet()) {
			Joueur j = mapEntry.getKey();
			Socket s = mapEntry.getValue();

			try {
				PrintStream fluxSortant = new PrintStream(s.getOutputStream());
				fluxSortant.println();
				fluxSortant.println(puzzleCommun);
				fluxSortant.println(this.getJoueurCourant().getNom() + " doit jouer");
				fluxSortant.println();
				fluxSortant.println("HAUT:h BAS:b GAUCHE:g DROITE:d");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deconnecterJoueur(Joueur j) {
		joueurs.remove(j);
		tableSocketDesJoueurs.remove(j);
	}

	/**
	 * Incrémente l'index du joueur qui joue son tour dans la liste de joueurs
	 * 
	 * @throws IOException
	 */
	private void passerAuJoueurSuivant() throws IOException {
		this.indexJoueurCourant++;
		this.indexJoueurCourant %= joueurs.size();
		for (Map.Entry<Joueur, Socket> mapEntry : tableSocketDesJoueurs.entrySet()) {
			Joueur j = mapEntry.getKey();
			Socket s = mapEntry.getValue();

			PrintStream fluxSortant = new PrintStream(s.getOutputStream());
			fluxSortant.println(puzzleCommun);
			fluxSortant.println();
			fluxSortant.println(this.getJoueurCourant().getNom() + " doit jouer");
			fluxSortant.println();
			fluxSortant.println("HAUT:h BAS:b GAUCHE:g DROITE:d");
		}
	}

	/**
	 * 
	 * @param dp        EDeplacement de la case
	 * @param numJoueur numero du joueur dans la liste
	 * @throws IOException
	 */
	public void deplacerCase(EDeplacement dp, Joueur joueur, int numJoueur) throws IOException {
		if (numJoueur == this.indexJoueurCourant + 1 && !puzzleCommun.verifierGrille()) {
			puzzleCommun.deplacerCase(dp);
			passerAuJoueurSuivant();
		}
		if (puzzleCommun.verifierGrille()) {
			for (Map.Entry<Joueur, Socket> mapEntry : tableSocketDesJoueurs.entrySet()) {
				Joueur j = mapEntry.getKey();
				Socket s = mapEntry.getValue();

				PrintStream fluxSortant = new PrintStream(s.getOutputStream());
				fluxSortant.println("VOUS AVEZ FINI LE PUZZLE EN " + puzzleCommun.getNbCoups() + " COUPS!");
			}
		}
	}
	
	public boolean partieFinie() {
		return puzzleCommun.verifierGrille();
	}

	public Puzzle getPuzzleCommun() {
		return this.puzzleCommun;
	}

	private Joueur getJoueurCourant() {
		Joueur joueurCourant = joueurs.get(indexJoueurCourant);
		return joueurCourant;
	}

	public int getIndexJoueurCourant() {
		return indexJoueurCourant;
	}

}