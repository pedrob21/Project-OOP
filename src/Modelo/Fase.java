package Modelo;

import java.util.ArrayList;
import Auxiliar.LeitorMapa;

public class Fase {
    private final int numero;
    private final String caminho;
    private ArrayList<Personagem> personagens;

    public Fase(int numero, String caminho) {
        this.numero = numero;
        this.caminho = caminho;
        this.personagens = LeitorMapa.carregarMapa(caminho);
    }

    public int getNumero() {
        return numero;
    }

    public String getCaminho() {
        return caminho;
    }

    public ArrayList<Personagem> getPersonagens() {
        return personagens;
    }

    public void recarregar() {
        this.personagens = LeitorMapa.carregarMapa(caminho);
    }

    public boolean estaValida() {
        return personagens != null && !personagens.isEmpty() && personagens.get(0) instanceof Chico;
    }

    public Chico getHeroi() {
        if (estaValida()) {
            return (Chico) personagens.get(0);
        }
        return null;
    }
}
