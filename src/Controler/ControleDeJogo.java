package Controler;

import Modelo.Chaser;
import Modelo.Hero;
import Modelo.Moeda;
import Modelo.Personagem;
import auxiliar.Posicao;
import Auxiliar.Desenho;
import Modelo.Chave;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class ControleDeJogo {

    public void desenhaTudo(ArrayList<Personagem> e) {
    for (int i = 0; i < e.size(); i++) {
        e.get(i).autoDesenho();
    }

    if (!e.isEmpty() && e.get(0) instanceof Hero) {
        Hero hero = (Hero) e.get(0);
        Graphics g = Desenho.acessoATelaDoJogo().getGraphics();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Vidas: " + hero.getVidas() + " | Pontuação: " + hero.getPontuacao(), 10, 20);
    }
}


public void processaTudo(ArrayList<Personagem> umaFase) {
    if (umaFase == null || umaFase.isEmpty() || !(umaFase.get(0) instanceof Hero)) {
        return;
    }

    Hero hero = (Hero) umaFase.get(0);

    for (int i = 1; i < umaFase.size(); i++) {
        Personagem p = umaFase.get(i);

        // Verifica colisão com o herói
        if (hero.getPosicao().igual(p.getPosicao())) {

            // Coleta moeda
            if (p instanceof Moeda) {
                hero.addPontuacao();
                umaFase.remove(i--);
                continue;
            }

            // Coleta chave
            if (p instanceof Chave) {
                hero.coletarChave();
                umaFase.remove(i--);
                System.out.println("Herói coletou a chave!");
                hero.setProntoParaTrocarFase(true);
                continue;
            }

            // Dano de inimigos mortais
            if (p.isbMortal()) {
                if (hero.podeLevarDano()) {
                    hero.perderVida();
                    System.out.println("Herói perdeu uma vida! Vidas: " + hero.getVidas()
                                     + " | Pontuação: " + hero.getPontuacao());

                    if (!hero.estaVivo()) {
                        umaFase.remove(0); // Remove o herói da fase
                        System.out.println("Herói morreu.");
                    }
                } else {
                    hero.permitirDanoNovamente();
                }
                break;
            }
        }
    }

    // Movimento dos Chasers
    for (int i = 1; i < umaFase.size(); i++) {
        Personagem p = umaFase.get(i);
        if (p instanceof Chaser) {
            ((Chaser) p).mover(umaFase, this, hero.getPosicao());
        }
    }
}


    /* Retorna true se a posição p é válida para o herói com relação aos personagens */
    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p) {
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem pIesimoPersonagem = umaFase.get(i);
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }

}
