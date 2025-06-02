package Modelo;

import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;

import java.io.Serializable;

public class Hero extends Personagem implements Serializable {
    private int vidas;
    private boolean podeLevarDano = true;
    private int pontuacao;

    public Hero(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = false;
        this.vidas = 3;
        this.pontuacao = 0;
    }

    public int getVidas() {
        return vidas;
    }

     public int getPontuacao() {
        return pontuacao;
    }
     
     public void setPontuacao(int valor){
         this.pontuacao = valor;
     }

    public void addPontuacao() {
        pontuacao++;
    }
    
    public void perderVida() {
        if (vidas > 0) {
            vidas--;
            podeLevarDano = false;
        }
    }

    public void permitirDanoNovamente() {
        this.podeLevarDano = true;
    }

    public boolean estaVivo() {
        return vidas > 0;
    }

    public boolean podeLevarDano() {
        return this.podeLevarDano;
    }

    public void voltaAUltimaPosicao() {
        pPosicao.volta(); // já cuida internamente da posição anterior
    }

    @Override
    public boolean setPosicao(int linha, int coluna) {
        Posicao nova = new Posicao(linha, coluna);

        Tela tela = Desenho.acessoATelaDoJogo();

        // Valida apenas se a tela e a fase estiverem prontas
        if (tela != null && tela.getFaseAtual() != null && tela.ehPosicaoValida(nova)) {
            return super.setPosicao(linha, coluna);
        }

        // Caso contrário, ignora a validação (fase ainda não pronta)
        return super.setPosicao(linha, coluna);
    }


    private boolean tentaMover(Runnable movimento) {
        int linhaAntes = pPosicao.getLinha();
        int colunaAntes = pPosicao.getColuna();

        movimento.run();

        Posicao nova = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(nova)) {
            pPosicao.setPosicao(linhaAntes, colunaAntes);
            return false;
        }
        return true;
    }

    @Override
    public boolean moveUp() {
        return tentaMover(() -> super.moveUp());
    }

    @Override
    public boolean moveDown() {
        return tentaMover(() -> super.moveDown());
    }

    @Override
    public boolean moveRight() {
        return tentaMover(() -> super.moveRight());
    }

    @Override
    public boolean moveLeft() {
        return tentaMover(() -> super.moveLeft());
    }
}
