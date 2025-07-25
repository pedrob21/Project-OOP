package Auxiliar;

import Modelo.Porta;
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
                String linhaAtual = scanner.nextLine().trim();
                if (linhaAtual.isEmpty()) continue; // ← pula linhas vazias

                String[] valores = linhaAtual.split(" ");
                for (int coluna = 0; coluna < valores.length; coluna++) {
                    String[] partes = valores[coluna].split("-");
                    int tipo = Integer.parseInt(partes[0]);

                    switch (tipo) {
                        case 1: // Parede
                            Parede p = new Parede("bricks.png");
                            p.setPosicao(linha, coluna);
                            personagens.add(p);
                            break;

                        case 2: // Moeda
                            Moeda m = new Moeda("bitcoin.png");
                            m.setPosicao(linha, coluna);
                            personagens.add(m);
                            break;

                        case 3: // Hero
                            Chico hero = new Chico("ChicoCoins.png");
                            hero.setPosicao(linha, coluna);
                            personagens.add(0, hero); // sempre na posição 0
                            break;

                        case 4: // BichinhoVaiVemHorizontal
                            int alcanceE = (partes.length > 1) ? Integer.parseInt(partes[1]) : 1;
                            int alcanceD = (partes.length > 2) ? Integer.parseInt(partes[2]) : 1;

                            BichinhoVaiVemHorizontal bvh = new BichinhoVaiVemHorizontal("roboPink.png", alcanceE, alcanceD);
                            bvh.setPosicao(linha, coluna);
                            personagens.add(bvh);
                            break;

                        case 5: // BichinhoVaiVemVertical
                            int alcanceC = (partes.length > 1) ? Integer.parseInt(partes[1]) : 1;
                            int alcanceB = (partes.length > 2) ? Integer.parseInt(partes[2]) : 1;

                            BichinhoVaiVemVertical bvv = new BichinhoVaiVemVertical("roboPink.png", alcanceC, alcanceB);
                            bvv.setPosicao(linha, coluna);
                            personagens.add(bvv);
                            break;
                        case 6:
                            Porta chave = new Porta("fire.png");
                            chave.setPosicao(linha, coluna);
                            personagens.add(chave);
                            break;
                        case 7: // BichinhoVaiVemVerticalContrario
                            int cima = (partes.length > 1) ? Integer.parseInt(partes[1]) : 1;
                            int baixo = (partes.length > 2) ? Integer.parseInt(partes[2]) : 1;

                            BichinhoContrarioVertical bvvc = new BichinhoContrarioVertical("roboPink.png", cima, baixo);
                            bvvc.setPosicao(linha, coluna);
                            personagens.add(bvvc);
                            break;
                        case 8: // BichinhoVaiVemHorizontal
                            int alcanceEsq = (partes.length > 1) ? Integer.parseInt(partes[1]) : 1;
                            int alcanceDir = (partes.length > 2) ? Integer.parseInt(partes[2]) : 1;

                            BichinhoContrarioHorizontal bvhc = new BichinhoContrarioHorizontal("roboPink.png", alcanceEsq, alcanceDir);
                            bvhc.setPosicao(linha, coluna);
                            personagens.add(bvhc);
                            break;
                        case 9:
                            Louro chaser = new Louro("Chaser.png");
                            chaser.setPosicao(linha, coluna);
                            personagens.add(chaser);
                            break;
                        case 10:
                            int direcao = (partes.length > 1) ? Integer.parseInt(partes[1]) : 1; // padrão direita
                            Cigar cav = new Cigar("caixaCigar.png", direcao);
                            cav.setPosicao(linha, coluna);
                            personagens.add(cav);
                            break;
                        case 11: // Caixa empurrável
                            CaixaEmpurravel caixa = new CaixaEmpurravel("caveira.png"); // ou o nome da imagem que você deseja
                            caixa.setPosicao(linha, coluna);
                            personagens.add(caixa);
                            break;
                        case 12:
                            AnaMaria zz = new AnaMaria("AnaMaria.png");
                            zz.setPosicao(linha, coluna);
                            personagens.add(zz);

                        default:
                            // 0 ou desconhecido → ignora
                            break;
                    }
                }

                linha++;
            }

        } catch (IOException e) {
              // Silencia o erro quando o arquivo da próxima fase não existe
            if (e instanceof FileNotFoundException) {
                // Nada a fazer, fim do jogo é esperado
            } else {
                System.out.println("Erro ao ler o mapa: " + e.getMessage());
            }
        }

        return personagens;
    }

    // ✅ Novo método
    public static char[][] gerarMapaTextual(ArrayList<Personagem> personagens) {
        char[][] mapa = new char[Consts.MUNDO_ALTURA][Consts.MUNDO_LARGURA];

        for (int i = 0; i < Consts.MUNDO_ALTURA; i++) {
            for (int j = 0; j < Consts.MUNDO_LARGURA; j++) {
                mapa[i][j] = '.';
            }
        }

        for (Personagem p : personagens) {
            int linha = p.getPosicao().getLinha();
            int coluna = p.getPosicao().getColuna();
            char simbolo = '?';

            if (p instanceof Chico) {
                simbolo = 'H';
            } else if (p instanceof Moeda) {
                simbolo = 'M';
            } else if (p instanceof Parede) {
                simbolo = '#';
            } else if (p instanceof BichinhoVaiVemHorizontal) {
                simbolo = '>';
            } else if (p instanceof BichinhoVaiVemVertical) {
                simbolo = 'V';
            } else if (p instanceof Louro) {
                simbolo = 'C';
            } else if (p instanceof Cigar) {
                simbolo = 'K';
            } else if (p instanceof AnaMaria) {
                simbolo = 'Z';
            }

            mapa[linha][coluna] = simbolo;
        }

        return mapa;
    }
}
