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
    private boolean possuiChave = false;

    public Hero(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        this.bMortal = false;
        this.vidas = 3;
        this.pontuacao = 0;
    }

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

    public void setPontuacao(int valor) {
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
    
    public void setVidas(int valor){
        this.vidas = valor;
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
        Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && !tela.ehPosicaoValida(nova)) {
            pPosicao.setPosicao(linhaAntes, colunaAntes);
            return false;
        }
        return true;
    }

    @Override
public boolean moveUp() {
    return tentaMoverComEmpurrao(-1, 0);
}

@Override
public boolean moveDown() {
    return tentaMoverComEmpurrao(1, 0);
}

@Override
public boolean moveLeft() {
    return tentaMoverComEmpurrao(0, -1);
}

@Override
public boolean moveRight() {
    return tentaMoverComEmpurrao(0, 1);
}

    private boolean tentaMoverComEmpurrao(int deltaLinha, int deltaColuna) {
        int novaLinha = pPosicao.getLinha() + deltaLinha;
        int novaColuna = pPosicao.getColuna() + deltaColuna;
        Posicao nova = new Posicao(novaLinha, novaColuna);

        Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null) {
            Personagem obstaculo = tela.getPersonagemNaPosicao(nova); // <--- Correção aqui
            if (obstaculo instanceof CaixaEmpurravel caixa) {
                if (!caixa.empurrar(deltaLinha, deltaColuna)) {
                    return false; // não conseguiu empurrar
                }
            } else if (!tela.ehPosicaoValida(nova)) {
                return false;
            }
        }

        return this.setPosicao(novaLinha, novaColuna);
    }
}