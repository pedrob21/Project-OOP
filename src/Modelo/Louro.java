/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;
import Auxiliar.Desenho;
import Controler.ControleDeJogo;

/**
 *
 * @author 2373891
 */
public class Louro extends Personagem implements Serializable {

    private boolean iDirectionV;
    private boolean iDirectionH;
    private int tickCounter;
    private final int TICKS_POR_MOVIMENTO = 3; // Deixa o Chaser mais lento

    public Louro(String sNomeImagePNG) {
        super(sNomeImagePNG);
        iDirectionV = true;
        iDirectionH = true;
        this.bTransponivel = true;
        this.bMortal = true;
        this.tickCounter = 0;
    }

    private void computeDirection(Posicao heroPos) {
        if (heroPos.getColuna() < this.getPosicao().getColuna()) {
            iDirectionH = true;
        } else if (heroPos.getColuna() > this.getPosicao().getColuna()) {
            iDirectionH = false;
        }

        if (heroPos.getLinha() < this.getPosicao().getLinha()) {
            iDirectionV = true;
        } else if (heroPos.getLinha() > this.getPosicao().getLinha()) {
            iDirectionV = false;
        }
    }

    public void mover(ArrayList<Personagem> umaFase, ControleDeJogo controle, Posicao posHeroi) {
        tickCounter++;
        if (tickCounter < TICKS_POR_MOVIMENTO) {
            return;
        }
        tickCounter = 0;

        computeDirection(posHeroi); // Recalcula a direção do herói dinamicamente

        Posicao atual = this.getPosicao();
        Posicao novaPos = new Posicao(atual.getLinha(), atual.getColuna());

        // Movimento horizontal
        if (iDirectionH) {
            novaPos.setPosicao(atual.getLinha(), atual.getColuna() - 1);
            if (controle.ehPosicaoValida(umaFase, novaPos)) {
                this.moveLeft();
                return;
            }
        } else {
            novaPos.setPosicao(atual.getLinha(), atual.getColuna() + 1);
            if (controle.ehPosicaoValida(umaFase, novaPos)) {
                this.moveRight();
                return;
            }
        }

        // Movimento vertical
        novaPos.setPosicao(atual.getLinha(), atual.getColuna());
        if (iDirectionV) {
            novaPos.setPosicao(atual.getLinha() - 1, atual.getColuna());
            if (controle.ehPosicaoValida(umaFase, novaPos)) {
                this.moveUp();
            }
        } else {
            novaPos.setPosicao(atual.getLinha() + 1, atual.getColuna());
            if (controle.ehPosicaoValida(umaFase, novaPos)) {
                this.moveDown();
            }
        }
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }
}
