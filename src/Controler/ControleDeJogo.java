package Controler;

import Modelo.Chaser;
import Modelo.Hero;
import Modelo.Moeda;
import Modelo.Personagem;
import auxiliar.Posicao;
import Auxiliar.Desenho;

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
        // Só 1 linha de drawString com as duas informações
        g.drawString("Vidas: " + hero.getVidas() + " | Pontuação: " + hero.getPontuacao(), 10, 20);
    }
}


    public void processaTudo(ArrayList<Personagem> umaFase) {
        // Verifica se o herói ainda está na fase
        if (umaFase.isEmpty() || !(umaFase.get(0) instanceof Hero)) {
            return;
        }

        Hero hero = (Hero) umaFase.get(0);
        Personagem pIesimoPersonagem;

        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i);

            // Verifica colisão com o herói
            if (hero.getPosicao().igual(pIesimoPersonagem.getPosicao())) {

                // Se for moeda, incrementa pontuação e remove
                if (pIesimoPersonagem instanceof Moeda) {
                    hero.addPontuacao();
                    umaFase.remove(i);
                    i--;
                    continue;
                }

                // Se for inimigo mortal, o herói perde uma vida
                if (pIesimoPersonagem.isbMortal()) {
                    if (hero.podeLevarDano()) {
                        hero.perderVida();
                        System.out.println("Herói perdeu uma vida! Vidas: " + hero.getVidas() +
                                           " | Pontuação: " + hero.getPontuacao());

                        // Se acabou as vidas, remove o herói
                        if (!hero.estaVivo()) {
                            umaFase.remove(0);
                            System.out.println("Herói morreu definitivamente.");
                        }
                    } else {
                        // Reativa a possibilidade de levar dano
                        hero.permitirDanoNovamente();
                    }
                    break;
                }
            }
        }

        // Movimento dos Chasers
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i);
            if (pIesimoPersonagem instanceof Chaser) {
                ((Chaser) pIesimoPersonagem).computeDirection(hero.getPosicao());
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
