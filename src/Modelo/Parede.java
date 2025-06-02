package Modelo;

import Auxiliar.Desenho;
import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author pbern
 */
public class Parede extends Personagem implements Serializable {
    public Parede(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = false;
        this.bTransponivel = false;
    }

    public void autoDesenho() {
        super.autoDesenho();
    }
}
