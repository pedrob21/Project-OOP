package Modelo;

import Auxiliar.Desenho;
import Controler.Tela;
import java.io.Serializable;
import Auxiliar.Desenho;
import auxiliar.Posicao;

public class CaixaEmpurravel extends Personagem implements Serializable {

    public CaixaEmpurravel(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = false;
    }

    public boolean empurrar(int deltaLinha, int deltaColuna) {
        int novaLinha = this.pPosicao.getLinha() + deltaLinha;
        int novaColuna = this.pPosicao.getColuna() + deltaColuna;

        Posicao novaPosicao = new Posicao(novaLinha, novaColuna);
        Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.getFaseAtual() != null) {
            if (tela.ehPosicaoValida(novaPosicao)) {
                return this.setPosicao(novaLinha, novaColuna);
            }
        }

        return false;
    }
}
