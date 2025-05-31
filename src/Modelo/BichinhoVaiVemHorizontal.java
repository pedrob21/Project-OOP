package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BichinhoVaiVemHorizontal extends Personagem {
    private boolean indoEsquerda;
    private int colunaMin;
    private int colunaMax;
    private int delay;
    private int contador;
    private boolean limitesDefinidos;
    private int alcanceEsquerda;
    private int alcanceDireita;

    public BichinhoVaiVemHorizontal(String sNomeImagePNG, int alcanceEsquerda, int alcanceDireita) {
        super(sNomeImagePNG);
        this.indoEsquerda = true;
        this.delay = 2;
        this.contador = 0;
        this.limitesDefinidos = false;
        this.alcanceEsquerda = alcanceEsquerda;
        this.alcanceDireita = alcanceDireita;
        this.bMortal = true;
    }

    @Override
    public void autoDesenho() {
        if (!limitesDefinidos) {
            int colunaAtual = pPosicao.getColuna();
            colunaMin = colunaAtual - alcanceEsquerda;
            colunaMax = colunaAtual + alcanceDireita;
            limitesDefinidos = true;
        }

        contador++;
        if (contador >= delay) {
            int colunaAtual = pPosicao.getColuna();
            if (indoEsquerda) {
                if (colunaAtual > colunaMin) {
                    this.setPosicao(pPosicao.getLinha(), colunaAtual - 1);
                } else {
                    indoEsquerda = false;
                    this.setPosicao(pPosicao.getLinha(), colunaAtual + 1);
                }
            } else {
                if (colunaAtual < colunaMax) {
                    this.setPosicao(pPosicao.getLinha(), colunaAtual + 1);
                } else {
                    indoEsquerda = true;
                    this.setPosicao(pPosicao.getLinha(), colunaAtual - 1);
                }
            }
            contador = 0;
        }

        super.autoDesenho();
    }
}