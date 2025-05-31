package Controler;

import Modelo.Chaser;
import Modelo.Hero;
import Modelo.Moeda;
import Modelo.Personagem;
import auxiliar.Posicao;

import java.util.ArrayList;

public class ControleDeJogo {

    public void desenhaTudo(ArrayList<Personagem> e) {
        for (int i = 0; i < e.size(); i++) {
            e.get(i).autoDesenho();
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
                // Se for moeda, ela some
                if (pIesimoPersonagem instanceof Moeda) {
                    umaFase.remove(i);
                    i--; // Corrige índice após remoção
                    continue;
                }

                // Se for inimigo mortal, o herói perde uma vida
                if (pIesimoPersonagem.isbMortal()) {
                    if (hero.podeLevarDano()) {
                        hero.perderVida();
                        System.out.println("Herói perdeu uma vida! Vidas restantes: " + hero.getVidas());

                        if (!hero.estaVivo()) {
                            umaFase.remove(0);
                            System.out.println("Herói morreu definitivamente.");
                        }
                    }
                    break;
                }
            }
        }

        // Após colisões, verifica se o herói está longe de inimigos para resetar o dano
        boolean encostandoEmInimigo = false;
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem p = umaFase.get(i);
            if (p.isbMortal() && hero.getPosicao().igual(p.getPosicao())) {
                encostandoEmInimigo = true;
                break;
            }
        }
        if (!encostandoEmInimigo) {
            hero.permitirDanoNovamente();
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
