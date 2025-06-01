
package Modelo;

import Auxiliar.Desenho;
import java.util.Random;
import Auxiliar.LeitorMapa;



public class BichinhoVaiVemVertical extends Personagem {
    private boolean subindo;
    private int linhaMin;
    private int linhaMax;
    private int delay;
    private int contador;
    private boolean limitesDefinidos;
    private int alcanceCima;
    private int alcanceBaixo;

    public BichinhoVaiVemVertical(String sNomeImagePNG, int alcanceCima, int alcanceBaixo) {
        super(sNomeImagePNG);
        this.subindo = true;
        this.delay = 2;
        this.contador = 0;
        this.limitesDefinidos = false;
        this.alcanceCima = alcanceCima;
        this.alcanceBaixo = alcanceBaixo;
        this.bMortal = true;
    }

    @Override
    public void autoDesenho() {
        if (!limitesDefinidos) {
            int linhaAtual = pPosicao.getLinha();
            linhaMin = linhaAtual - alcanceCima;
            linhaMax = linhaAtual + alcanceBaixo;
            limitesDefinidos = true;
        }

        contador++;
        if (contador >= delay) {
            int linhaAtual = pPosicao.getLinha();
            if (subindo) {
                if (linhaAtual > linhaMin) {
                    this.setPosicao(linhaAtual - 1, pPosicao.getColuna());
                } else {
                    subindo = false;
                    this.setPosicao(linhaAtual + 1, pPosicao.getColuna());
                }
            } else {
                if (linhaAtual < linhaMax) {
                    this.setPosicao(linhaAtual + 1, pPosicao.getColuna());
                } else {
                    subindo = true;
                    this.setPosicao(linhaAtual - 1, pPosicao.getColuna());
                }
            }
            contador = 0;
        }

        super.autoDesenho();
    }
}