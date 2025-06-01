package Auxiliar;

import Modelo.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LeitorMapa {
    public static ArrayList<Personagem> carregarMapa(String caminhoArquivo) {
        ArrayList<Personagem> personagens = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(caminhoArquivo))) {
            int linha = 0;
            while (scanner.hasNextLine()) {
                String[] valores = scanner.nextLine().split(" ");
                for (int coluna = 0; coluna < valores.length; coluna++) {
                    int tipo = Integer.parseInt(valores[coluna]);

                    switch (tipo) {
                        case 1: // parede
                            Personagem parede = new Personagem("Parede.png") {}; // anônimo
                            parede.setbTransponivel(false);
                            parede.setPosicao(linha, coluna);
                            personagens.add(parede);
                            break;

                        case 2: // moeda
                            Moeda moeda = new Moeda("Caveira.png");
                            moeda.setPosicao(linha, coluna);
                            personagens.add(moeda);
                            break;

                        case 3: // inimigo
                            Chaser inimigo = new Chaser("Robbo.png");
                            inimigo.setPosicao(linha, coluna);
                            personagens.add(inimigo);
                            break;

                        case 4: // herói
                            Hero heroi = new Hero("Robbo.png");
                            heroi.setPosicao(linha, coluna);
                            personagens.add(0, heroi); // herói sempre na posição 0
                            break;

                        default:
                            break;
                    }
                }
                linha++;
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o mapa: " + e.getMessage());
        }

        return personagens;
    }
}
