package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import java.io.Serializable;

public class Cigar extends Personagem implements Serializable {
    private int iContaIntervalos;
    private int direcaoDisparo; // 1 = direita, 2 = esquerda

    public Cigar(String sNomeImagePNG, int direcaoDisparo) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = false;
        this.iContaIntervalos = 0;
        this.direcaoDisparo = direcaoDisparo;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();

        this.iContaIntervalos++;
        if (this.iContaIntervalos == Consts.TIMER) {
            this.iContaIntervalos = 0;

            // Escolhe imagem de acordo com a direção
            String imagemFogo = (direcaoDisparo == 2) ? "CigarEsq.png" : "Cigar.png";

            TiroCigar f = new TiroCigar(imagemFogo);

            int linha = pPosicao.getLinha();
            int coluna = pPosicao.getColuna();
            if (direcaoDisparo == 1) {
                f.setPosicao(linha, coluna + 1); // Direita
                f.setDirecao(1);
            } else if (direcaoDisparo == 2) {
                f.setPosicao(linha, coluna - 1); // Esquerda
                f.setDirecao(2);
            }

            Desenho.acessoATelaDoJogo().addPersonagem(f);
        }
    }
}