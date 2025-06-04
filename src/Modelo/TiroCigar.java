package Modelo;

import Auxiliar.Desenho;
import Controler.Tela;
import java.awt.Graphics;
import java.io.Serializable;
import Auxiliar.LeitorMapa;
import auxiliar.Posicao;


public class TiroCigar extends Personagem implements Serializable {

    private int direcao; // 1 = direita, 2 = esquerda
    
    public TiroCigar(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = true;
        this.bTransponivel = true;
    }
    

    public void setDirecao(int direcao) {
        this.direcao = direcao;
    }

    @Override
public void autoDesenho() {
    super.autoDesenho();

    boolean conseguiuMover = false;

    if (direcao == 1) {
        Posicao novaPos = new Posicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
        if (Desenho.acessoATelaDoJogo().ehPosicaoValida(novaPos)) {
            conseguiuMover = this.moveRight();
        }
    } else if (direcao == 2) {
        Posicao novaPos = new Posicao(pPosicao.getLinha(), pPosicao.getColuna() - 1);
        if (Desenho.acessoATelaDoJogo().ehPosicaoValida(novaPos)) {
            conseguiuMover = this.moveLeft();
        }
    }

    if (!conseguiuMover) {
        Desenho.acessoATelaDoJogo().removePersonagem(this);
    }
}
}
