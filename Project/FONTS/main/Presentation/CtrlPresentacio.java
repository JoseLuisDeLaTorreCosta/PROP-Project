package main.Presentation;

import java.util.ArrayList;
import java.util.HashMap;
import main.Domain.CtrlDomini;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

public class CtrlPresentacio {
	private main.Domain.CtrlDomini ctrlDomini;
	private VistaLogin vistaLogin;
	private VistaPrincipal vistaPrincipal;
	private boolean teclatEnModificacio;
	private VistaTeclat vistaTeclat;
	private VistaCrearDocument vistaCrearDocument;
	private VistaCrearTeclat vistaCrearTeclat;
	private ArrayList<String> teclatIds;
	private ArrayList<String> documentIds;
	private String username;
	private String password;

	public CtrlPresentacio() {
		ctrlDomini = new main.Domain.CtrlDomini();
		vistaLogin = new VistaLogin(this);
	}

	public void initPresentacio() {
		vistaLogin.makeVisible();
	}

	// Mètode que inicia la sessió d'un usuari si coincideixen el username i
	// password
	public void iniciarSessio(String username, String password) {
		if (ctrlDomini.iniciar_sessio(username, password)) {
			this.username = username;
			documentIds = ctrlDomini.get_all_ids_Documents();
			teclatIds = ctrlDomini.get_all_ids_Teclats();
			
			vistaLogin.tancarVista();
			vistaPrincipal = new VistaPrincipal(this, teclatIds, documentIds);
			vistaPrincipal.makeVisible();
			this.username = username;
			this.password = password;
		} else {
			vistaLogin.mostrarMissatgeError("El nom d'usuari o la contrasenya no són correctes.");
		}
	}

	// Mètode que registra un usuari si no existeix cap amb aquest username i inicia
	// la sessió
	public void registrarUsuari(String username, String password) {
		if (ctrlDomini.registrar(username, password)) {
			this.username = username;
			vistaLogin.tancarVista();
			documentIds = new ArrayList<>();
			teclatIds = new ArrayList<>();
			vistaPrincipal = new VistaPrincipal(this, teclatIds, documentIds);
			vistaPrincipal.makeVisible();
			this.username = username;
			this.password = password;
		} else {
			vistaLogin.mostrarMissatgeError("L'usuari '" + username + "'' ja existeix.");
		}
	}

	private void resetVistaPrincipal() {
		vistaPrincipal.tancarVista();
		documentIds = ctrlDomini.get_all_ids_Documents();
		teclatIds = ctrlDomini.get_all_ids_Teclats();
		vistaPrincipal = new VistaPrincipal(this, teclatIds, documentIds);
		vistaPrincipal.makeVisible();
	}

	public void crearDocument() {
		VistaCrearDocument vistaCrearDocument = new VistaCrearDocument(this);
		vistaCrearDocument.showVista();
	}

	public void crearAlfabet() {
		VistaCrearAlfabet vistaCrearAlfabet = new VistaCrearAlfabet(this);
		vistaCrearAlfabet.showVista();
	}

	public boolean importarAlfabet(JFrame frameCrearDocument) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fitxers de text", "txt");
		fileChooser.addChoosableFileFilter(filter);

		int result = fileChooser.showOpenDialog(frameCrearDocument);
		File file;
		if (result == JFileChooser.APPROVE_OPTION) file = fileChooser.getSelectedFile();
		else return false;

		String name = file.getName();
		if (existeixDocument(name)) return false;

		StringBuilder contingutFitxer = new StringBuilder();
		try {
			BufferedReader lector = new BufferedReader(new FileReader(file));

			String linia;
			while ((linia = lector.readLine()) != null) contingutFitxer.append(linia).append("\n");
			
			lector.close();
		} catch (IOException exc) {
			return false;
		}

		String contingutFitxerDef = contingutFitxer.toString();
		String[] contingutPerLinies = contingutFitxerDef.split("\n");
		
		if (!contingutPerLinies[0].startsWith("N caracters: ")) return false;
		int n = 0;
		contingutPerLinies[0] = contingutPerLinies[0].trim();
		for (int i = 13; i < contingutPerLinies[0].length(); ++i) {
			char read = contingutPerLinies[0].charAt(i);
			if (read < '0' || read > '9') return false;
			n *= 10;
			n += read - '0';
		}
		if (n <= 0) return false;
		if (contingutPerLinies.length != n + 2) return false;

		ArrayList<Character> alfabet = new ArrayList<Character>();
		String aux = contingutPerLinies[1].trim();
		String[] pseudoAlfabet = aux.split(" ");
		if (pseudoAlfabet.length != n) return false;
		for (int i = 0; i < n; ++i) {
			if (pseudoAlfabet[i].length() != 1) return false;
			char read = pseudoAlfabet[i].charAt(0);
			if (read == '_') return false;
			if (alfabet.contains(read)) return false;
			alfabet.add(read);
		}

		int[][] taulaMarkov = new int[n][n];
		for (int i = 2; i < n + 2; ++i) {
			String aux2 = contingutPerLinies[i].trim();
			String[] filaMarkov = aux2.split(" ");
			if (filaMarkov.length != n) return false;
			for (int j = 0; j < n; ++j) {
				int value;
				try {
					value = Integer.valueOf(filaMarkov[j]);
				} catch (NumberFormatException exc) {
					return false;
				}
				if (value < 0) return false;
				taulaMarkov[i - 2][j] = value;
			}
		}

		crearAlfabet(name, alfabet, taulaMarkov);
		return true;
	}

	public void crearAlfabet(String alfabetId, ArrayList<Character> alfabet, int[][] taulaMarkov) {
		ctrlDomini.newAlfabet(alfabet, taulaMarkov, alfabetId);
		resetVistaPrincipal();
	}

	public void crearLlista() {
		VistaCrearLlista vistaCrearLlista = new VistaCrearLlista(this);
		vistaCrearLlista.showVista();
	}

	public boolean importarLlista(JFrame frameCrearDocument) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fitxers de text", "txt");
		fileChooser.addChoosableFileFilter(filter);

		int result = fileChooser.showOpenDialog(frameCrearDocument);
		File file;
		if (result == JFileChooser.APPROVE_OPTION) file = fileChooser.getSelectedFile();
		else return false;

		String name = file.getName();
		if (existeixDocument(name)) return false;

		StringBuilder contingutFitxer = new StringBuilder();
		try {
			BufferedReader lector = new BufferedReader(new FileReader(file));

			String linia;
			while ((linia = lector.readLine()) != null) contingutFitxer.append(linia).append("\n");
			
			lector.close();
		} catch (IOException exc) {
			return false;
		}

		String contingutFitxerDef = contingutFitxer.toString();
		String[] contingutPerLinies = contingutFitxerDef.split("\n");

		if (!contingutPerLinies[0].startsWith("N paraules: ")) return false;
		int n = 0;
		contingutPerLinies[0] = contingutPerLinies[0].trim();
		for (int i = 12; i < contingutPerLinies[0].length(); ++i) {
			char read = contingutPerLinies[0].charAt(i);
			if (read < '0' || read > '9') return false;
			n *= 10;
			n += read - '0';
		}
		if (n <= 0) return false;
		if (contingutPerLinies.length != 3) return false;

		ArrayList<String> llista = new ArrayList<String>();
		contingutPerLinies[1] = contingutPerLinies[1].trim();
		String[] pseudoLlista = contingutPerLinies[1].split(" ");
		if (pseudoLlista.length != n) return false;
		for (int i = 0; i < n; ++i) {
			if (pseudoLlista[i].contains("_")) return false;
			if (llista.contains(pseudoLlista[i])) return false;
			llista.add(pseudoLlista[i]);
		}

		ArrayList<Integer> freqs = new ArrayList<Integer>();
		contingutPerLinies[2] = contingutPerLinies[2].trim();
		String[] pseudoFreqs = contingutPerLinies[2].split(" ");
		if (pseudoFreqs.length != n) return false;
		for (int i = 0; i < n; ++i) {
			int value;
				try {
					value = Integer.valueOf(pseudoFreqs[i]);
				} catch (NumberFormatException exc) {
					return false;
				}
				if (value < 0) return false;
				freqs.add(value);
		}

		crearLlista(name, llista, freqs);
		return true;
	}

	public void crearLlista(String llistaId, ArrayList<String> llista, ArrayList<Integer> freqs) {
		ctrlDomini.newLlistaParaules(llista, freqs, llistaId);
		resetVistaPrincipal();
	}

	public void crearText() {
		VistaCrearText vistaCrearText = new VistaCrearText(this);
		vistaCrearText.showVista();
	}

	public boolean importarText(JFrame frameCrearDocument) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fitxers de text", "txt");
		fileChooser.addChoosableFileFilter(filter);

		int result = fileChooser.showOpenDialog(frameCrearDocument);
		File file;
		if (result == JFileChooser.APPROVE_OPTION) file = fileChooser.getSelectedFile();
		else return false;

		String name = file.getName();
		if (existeixDocument(name)) return false;

		StringBuilder contingutFitxer = new StringBuilder();
		try {
			BufferedReader lector = new BufferedReader(new FileReader(file));

			String linia;
			while ((linia = lector.readLine()) != null) contingutFitxer.append(linia).append("\n");
			
			lector.close();
		} catch (IOException exc) {
			return false;
		}

		String contingutFitxerDef = contingutFitxer.toString();
		if (contingutFitxerDef.contains("_")) return false;
		contingutFitxerDef = contingutFitxerDef.trim();

		crearText(name, contingutFitxerDef);
		return true;
	}

	public void crearText(String textId, String text) {
		ctrlDomini.newText(text, textId);
		resetVistaPrincipal();
	}

	public boolean existeixDocument(String idDoc) {
		return documentIds.contains(idDoc);
	}

	public void showVistaCrearTeclat() {
		vistaCrearTeclat = new VistaCrearTeclat(this, documentIds);
		vistaCrearTeclat.makeVisible();
	}

	public void crearTeclat(String name, String algorithmSelected, String documentId) {
		// Assuming ctrlDomini.newTeclat returns a boolean indicating success
		if (algorithmSelected.equals("QAP"))
			ctrlDomini.newTeclat(name, 1, documentId);
		if (algorithmSelected.equals("Hill Climbing"))
			ctrlDomini.newTeclat(name, 2, documentId);
		// If the teclat creation is successful, update the teclatIds
		teclatIds = ctrlDomini.get_all_ids_Teclats();
		vistaCrearTeclat.tancarVista();
		resetVistaPrincipal();
	}

	public boolean existeixTeclat(String idTeclat) {
		return teclatIds.contains(idTeclat);
	}

	public boolean teclatActualitzat(String idTeclat, char[][] teclat) {
		if (!ctrlDomini.Teclat_exists(idTeclat)) return false;
		char[][] teclatReal = ctrlDomini.getTeclats(idTeclat);
		return teclat.equals(teclatReal);
	}

	private String getTipusDocument(String idDoc) {
		if (ctrlDomini.is_Alfabet(idDoc))
			return "Alfabet";
		else if (ctrlDomini.is_Llista_paraules(idDoc))
			return "Llista";
		else if (ctrlDomini.is_Text(idDoc))
			return "Text";
		else
			return "Inexistent";
	}

	public void mostrarDocument(String idDoc) {
		String tipusDoc = getTipusDocument(idDoc);
		if (tipusDoc == "Alfabet")
			mostrarAlfabet(idDoc);
		else if (tipusDoc == "Llista")
			mostrarLlista(idDoc);
		else if (tipusDoc == "Text")
			mostrarText(idDoc);
		else {
			// pot donar-se el cas que aquesta funció es cridi amb un idDoc que no
			// existeixi?nopp
		}
	}

	public void mostrarAlfabet(String idAlfabet) {
		ArrayList<Character> alfabet = ctrlDomini.getAlfabet(idAlfabet);
		int[][] taulaMarkov = ctrlDomini.getMatMarkov(idAlfabet);
		VistaAlfabet vA = new VistaAlfabet(this, idAlfabet, alfabet, taulaMarkov);
		vA.showVista();
	}

	public void mostrarLlista(String idLlista) {
		ArrayList<String> llista = ctrlDomini.getLlista_Paraules(idLlista);
		ArrayList<Integer> freqs = ctrlDomini.get_Freq_Llista_Paraules(idLlista);
		VistaLlista vL = new VistaLlista(this, idLlista, llista, freqs);
		vL.showVista();
	}

	public void mostrarText(String idText) {
		String text = ctrlDomini.getText(idText);
		VistaText vT = new VistaText(this, idText, text);
		vT.showVista();
	}

	public void mostrarTeclat(String idTeclat) {
		char[][] teclat = ctrlDomini.getTeclats(idTeclat);
		VistaTeclat vT = new VistaTeclat(this, idTeclat, teclat);
		vT.showVista();
	}

	public boolean iniciarModificacioTeclat() {
		if (!teclatEnModificacio) {
			teclatEnModificacio = true;
			return true;
		}
		return false;
	}

	public void modificaTeclat(String idTeclat, char[][] novaDisposicio) {
		ctrlDomini.modTeclat(idTeclat, novaDisposicio);
		cancelarModificar();
	}

	// Mètode que esborra un teclat a l'usuari amb sessió iniciada
	public void removeTeclat(String teclatId) {
		teclatIds.remove(teclatId);
		ctrlDomini.deleteTeclat(teclatId);
		resetVistaPrincipal();
	}

	// Mètode que esborra un document a l'usuari amb sessió iniciada
	public void removeDocument(String documentId) {
		documentIds.remove(documentId);
		ctrlDomini.deleteDocument(documentId);
		resetVistaPrincipal();
	}

	public void tancarSessio() {
		// Guardar la sessió
		ctrlDomini.sign_out(username, password);
	}


	public static void main(String[] args) {
		// Crear una instancia de CtrlPresentacio
		CtrlPresentacio ctrlPresentacio = new CtrlPresentacio();

		// Llamar al método initPresentacio()
		ctrlPresentacio.initPresentacio();
	}

	public void cancelarModificar() {
		teclatEnModificacio = false;
	}
}
