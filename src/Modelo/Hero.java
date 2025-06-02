package Modelo;

import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;

import java.io.Serializable;

public class Hero extends Personagem implements Serializable {
    private int vidas;
    private boolean podeLevarDano = true;
    private int pontuacao;
    private boolean prontoParaTrocarFase = false;

    public Hero(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = false;
        this.vidas = 3;
        this.pontuacao = 0;
    }
    
    private boolean possuiChave = false;

    public void coletarChave() {
        this.possuiChave = true;
    }

    public boolean temChave() {
        return this.possuiChave;
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
    
    public boolean isProntoParaTrocarFase() {
    return prontoParaTrocarFase;
    }

    public void setProntoParaTrocarFase(boolean pronto) {
        this.prontoParaTrocarFase = pronto;
    }

    @Override
    public boolean setPosicao(int linha, int coluna) {
        Posicao nova = new Posicao(linha, coluna);

        Tela tela = Desenho.acessoATelaDoJogo();

        // Só valida se a tela e a fase estiverem prontas
        if (tela != null && tela.getFaseAtual() != null) {
            if (!tela.ehPosicaoValida(nova)) {
                return false;
            }
        }

        // Se não está pronto para validar, só define a posição mesmo
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
