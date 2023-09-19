package test.java.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import main.java.model.Case;
import main.java.model.EDeplacement;
import main.java.model.Puzzle;

public class TestPuzzle {

	private static Puzzle puzzle;
	private final static int TAILLE = 3;

	@BeforeAll
	public static void setUp() {
		puzzle = new Puzzle(TAILLE);
	}

	@Test
	public void testConstructor1() {
		int tailleMauvaise = 2;
		Puzzle p = new Puzzle(tailleMauvaise);
		Assertions.assertTrue(p.getGrille().length > 2,
				"La taille du puzzle devrait être supérieure à 2, actuellement:" + tailleMauvaise);
	}

	@Test
	public void testConstructor2() {
		int tailleMauvaise = -1;
		Puzzle p2 = new Puzzle(tailleMauvaise);
		Assertions.assertTrue(p2.getGrille().length > 2,
				"La taille du puzzle devrait être supérieure à 2, actuellement:" + tailleMauvaise);
	}

	@Test
	public void testConstructor3() {
		int bonneTaille = 4;
		Puzzle p = new Puzzle(bonneTaille);
		Assertions.assertEquals(p.getTaille(), bonneTaille,
				"La taille du puzzle devrait être " + bonneTaille + ", actuellement:" + p.getTaille());
	}

	@Test
	public void testConstructorCase() {
		int indexCaseVide = Case.INDEX_CASE_VIDE;
		Case caseVide = new Case(indexCaseVide);
		Assertions.assertEquals(indexCaseVide, caseVide.getIndex(),
				"On devrait avoir une case vide construite avec l'index " + indexCaseVide + " mais on a "
						+ caseVide.getIndex());
	}

	@Test
	public void testMelangerGrille() {
		Case[][] grille1 = puzzle.getGrille().clone();
		puzzle.melanger();
		Case[][] grille2 = puzzle.getGrille();

		Assertions.assertNotEquals(grille1, grille2, "Les 2 grilles sont identiques, elles n'ont pas été mélangées.");
	}

	@Test
	public void verifierGrille() {
		Case[][] grilleCorrecte = new Case[TAILLE][TAILLE];
		Case[][] grilleIncorrecte = new Case[TAILLE][TAILLE];

		int compteur = 0;
		for (int i = 0; i < TAILLE; i++) {
			for (int j = 0; j < TAILLE; j++) {
				if (!(i == 0 && j == 0)) {
					grilleCorrecte[j][i] = new Case(compteur);
					grilleIncorrecte[j][i] = new Case(compteur);
				}
				compteur++;
			}
		}

		grilleCorrecte[0][0] = new Case(-1);
		int numChangement = grilleIncorrecte[TAILLE - 1][TAILLE - 2].getIndex();
		grilleIncorrecte[TAILLE - 1][TAILLE - 2] = new Case(-1);
		grilleIncorrecte[0][0] = new Case(numChangement);

		puzzle.setGrille(grilleCorrecte);
		Assertions.assertTrue(puzzle.verifierGrille(), "La grille devrait être correcte");

		puzzle.setGrille(grilleIncorrecte);
		Assertions.assertFalse(puzzle.verifierGrille(), "La grille devrait être incorrecte");
	}

	@Test
	public void testDeplacerCaseHaut() {
		// on prépare la grille avec une case vide qui ne se trouve pas en bas
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		int oldX, oldY;
		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.HAUT);

		int newX, newY;
		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldY, newY - 1,
				"La case vide se trouve en " + newY + ", elle devait se trouver en " + (newY - 1));
	}

	@Test
	public void testDeplacerCaseHautImpossible() {
		// on prépare la grille avec une case vide qui se trouve tout en bas
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = this.TAILLE - 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		int oldX, oldY;
		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.HAUT);

		int newX, newY;
		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldY, newY,
				"Le déplacement vers le haut n'est pas possible lorsque la case vide se trouve tout en bas (" + oldY
						+ ") or elle se trouve maintenant en " + newY);
	}

	@Test
	public void testDeplacerCaseGauche() {
		// on prépare la grille avec une case vide qui ne se trouve pas à droite
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		int oldX, oldY;
		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.GAUCHE);

		int newX, newY;
		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();

		Assertions.assertEquals(oldX, newX - 1,
				"La case vide se trouve en " + newX + ", elle devait se trouver en " + (newX - 1));
	}

	@Test
	public void testDeplacerCaseGaucheImpossible() {
		// on prépare la grille avec une case vide qui se trouve tout à droite
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = this.TAILLE - 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		int oldX, oldY;
		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.HAUT);

		int newX, newY;
		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldX, newX,
				"Le déplacement vers la gauche n'est pas possible lorsque la case vide se trouve tout à droite (" + oldX
						+ ") or elle se trouve maintenant en " + newX);
	}

	@Test
	public void testDeplacerCaseBas() {
		// on prépare la grille avec une case vide qui ne se trouve pas en bas
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		int oldX, oldY;
		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.BAS);

		int newX, newY;
		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldY, newY + 1,
				"La case vide se trouve en " + newY + ", elle devait se trouver en " + (newY + 1));
	}

	@Test
	public void testDeplacerCaseBasImpossible() {
		// on prépare la grille avec une case vide qui se trouve tout en haut
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 0;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		int oldX, oldY;
		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.BAS);

		int newX, newY;
		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldY, newY,
				"Le déplacement vers le haut n'est pas possible lorsque la case vide se trouve tout en haut(" + oldY
						+ ") or elle se trouve maintenant en " + newY);
	}

	@Test
	public void testDeplacerCaseDroit() {
		// on prépare la grille avec une case vide qui ne se trouve pas à gauche
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 1;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		int oldX, oldY;
		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.DROITE);

		int newX, newY;
		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();

		Assertions.assertEquals(oldX, newX + 1,
				"La case vide se trouve en " + newX + ", elle devait se trouver en " + (newX + 1));
	}

	@Test
	public void testDeplacerCaseDroitImpossible() {
		// on prépare la grille avec une case vide qui se trouve tout à gauche
		puzzle.melanger();
		Case[][] grille = puzzle.getGrille();
		int x, y, posX, posY;
		x = 0;
		y = 1;
		posX = puzzle.getXCaseVide();
		posY = puzzle.getYCaseVide();
		Case caseVide = puzzle.getCase(posX, posY);
		Case caseSwap = puzzle.getCase(x, y);
		grille[posX][posY] = caseSwap;
		grille[x][y] = caseVide;

		int oldX, oldY;
		oldX = puzzle.getXCaseVide();
		oldY = puzzle.getYCaseVide();

		puzzle.deplacerCase(EDeplacement.DROITE);

		int newX, newY;
		newX = puzzle.getXCaseVide();
		newY = puzzle.getYCaseVide();
		Assertions.assertEquals(oldX, newX,
				"Le déplacement vers la gauche n'est pas possible lorsque la case vide se trouve tout à gauche (" + oldX
						+ ") or elle se trouve maintenant en " + newX);
	}

	@Test
	public void testGetCoordonneesCaseVide() {
		int x, y;
		x = puzzle.getXCaseVide();
		y = puzzle.getYCaseVide();

		int valeurCaseRecuperee, valeurAttendue;
		valeurCaseRecuperee = puzzle.getGrille()[x][y].getIndex();
		valeurAttendue = Case.INDEX_CASE_VIDE;

		Assertions.assertEquals(valeurCaseRecuperee, valeurAttendue, "Normalement la case vide doit avoir comme index:"
				+ valeurAttendue + " or l'index de la case récupérée est:" + valeurCaseRecuperee);
	}

	@Test
	public void testGetCaseArrayIndexOutOfBounds() {
		int xAbsurde = this.TAILLE + 1;
		int y = 0;

		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
			Case caseTmp = puzzle.getCase(xAbsurde, y);
		});
	}
}