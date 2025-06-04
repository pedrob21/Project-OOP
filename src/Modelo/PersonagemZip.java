package Modelo;

import auxiliar.Posicao;
import Auxiliar.Consts;
import Auxiliar.Desenho;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class PersonagemZip extends Personagem implements Serializable {
    private String nome;

    public PersonagemZip(BufferedImage imagem, String nome, boolean mortal) {
        super(""); // Passa string vazia para não carregar imagem via caminho
        this.nome = nome;
        this.bMortal = mortal;
        this.bTransponivel = false; // por padrão, não transponível (pode ajustar depois)

        // Redimensiona a imagem recebida
        Image img = imagem.getScaledInstance(Consts.CELL_SIDE, Consts.CELL_SIDE, Image.SCALE_SMOOTH);
        BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
        bi.getGraphics().drawImage(img, 0, 0, null);
        this.iImage = new ImageIcon(bi);
    }

    public String getNome() {
        return nome;
    }

    @Override
    public void autoDesenho() {
        Desenho.desenhar(this.iImage, this.pPosicao.getColuna(), this.pPosicao.getLinha());
    }
}
