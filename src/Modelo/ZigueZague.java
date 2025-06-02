package Modelo;

import Auxiliar.Desenho;
import java.util.Random;
import auxiliar.Posicao;
import Controler.ControleDeJogo;

public class ZigueZague extends Personagem {

    public ZigueZague(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }

    @Override
    public void autoDesenho() {
        Random rand = new Random();
        int iDirecao = rand.nextInt(4);

        int novaLinha = pPosicao.getLinha();
        int novaColuna = pPosicao.getColuna();

        switch (iDirecao) {
            case 0 -> novaLinha--; // cima
            case 1 -> novaColuna++; // direita
            case 2 -> novaLinha++; // baixo
            case 3 -> novaColuna--; // esquerda
        }

        Posicao novaPos = new Posicao(novaLinha, novaColuna);

         if (Desenho.acessoATelaDoJogo().ehPosicaoValida(novaPos)) {
            this.setPosicao(novaLinha, novaColuna);
        }

        super.autoDesenho();
    }
}
