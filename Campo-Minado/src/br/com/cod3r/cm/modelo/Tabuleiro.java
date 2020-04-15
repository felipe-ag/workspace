package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import br.com.cod3r.cm.exce��o.ExplosaoException;

public class Tabuleiro {

	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<Campo>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampo();
		associarVizinhos();
		sortearMinas();
		
	}
	public void abrir(int linha, int coluna) {
		try {
			campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst().
			ifPresent(c -> c.abrir());
		}catch(ExplosaoException e) {
			
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}
	public void marcar(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst().
		ifPresent(c -> c.alternarMarcado());
	}

	private void gerarCampo() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				campos.add(new Campo(i, j));				
			}			
		}		
	}
	
	private void associarVizinhos() {
		for(Campo c1 : campos) {
			for(Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}		
	}
	
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
			
		}while(minasArmadas < minas);		
	}
	public boolean objetivoAlcado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	public void reiniciar() {
		campos.stream().forEach(c-> c.reiniciar());
		sortearMinas();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int cont = 0;
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				sb.append(" ");
				sb.append(campos.get(cont));
				sb.append(" ");
				cont++;
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
}
