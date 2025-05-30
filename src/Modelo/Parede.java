package Modelo;

import Auxiliar.Desenho;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author pbern
 */
public class Parede extends Personagem {
    public Parede(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = false;
        this.bTransponivel = false;
    }

    public void autoDesenho() {
        super.autoDesenho();
    }
}
