package Modelo;
import java.io.Serializable;


public class Moeda extends Personagem implements Serializable{

    public Moeda(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true; // O herói pode passar por ela
        this.bMortal = false;      // Não é mortal
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho(); // Apenas desenha, não se move
    }
}

