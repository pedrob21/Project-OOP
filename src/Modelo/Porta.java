/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author marcelhrb
 */
package Modelo;

import java.io.Serializable;


public class Porta extends Personagem implements Serializable{

    public Porta(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true; // O herói pode passar por ela
        this.bMortal = false;      // Não causa dano
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho(); // Apenas desenha
    }
}
