
package Modelo;

import Auxiliar.Desenho;
import java.util.Random;


public class BichinhoVaiVemVertical extends Personagem {
    private boolean bSubindo;
    private int linhaMin;
    private int linhaMax;
    private int delay;
    private int contador;
    private boolean limitesDefinidos;
    private int alcance;

    public BichinhoVaiVemVertical(String sNomeImagePNG, int alcance) {
        super(sNomeImagePNG);
        this.bSubindo = true;
        this.delay = 2;
        this.contador = 0;
        this.limitesDefinidos = false;
        this.alcance = alcance;
    }

    @Override
    public void autoDesenho() {
        if (!limitesDefinidos) {
            int linhaAtual = pPosicao.getLinha();
            linhaMin = linhaAtual - alcance;
            linhaMax = linhaAtual + alcance;
            limitesDefinidos = true;
        }

        contador++;
        if (contador >= delay) {
            int linhaAtual = pPosicao.getLinha();
            if (bSubindo) {
                if (linhaAtual > linhaMin) {
                    this.setPosicao(linhaAtual - 1, pPosicao.getColuna());
                } else {
                    bSubindo = false;
                    this.setPosicao(linhaAtual + 1, pPosicao.getColuna());
                }
            } else {
                if (linhaAtual < linhaMax) {
                    this.setPosicao(linhaAtual + 1, pPosicao.getColuna());
                } else {
                    bSubindo = true;
                    this.setPosicao(linhaAtual - 1, pPosicao.getColuna());
                }
            }
            contador = 0;
        }

        super.autoDesenho();
    }
}