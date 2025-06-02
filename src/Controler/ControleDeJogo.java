package Controler;

import Modelo.Chaser;
import Modelo.Hero;
import Modelo.Moeda;
import Modelo.Personagem;
import auxiliar.Posicao;
import Auxiliar.Desenho;
import Modelo.Chave;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ControleDeJogo {

    private static int vidasPersistentes = -1; // -1 indica que ainda não foi inicializado

    public void salvarFase(ArrayList<Personagem> faseAtual) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("fase_salva.dat"))) {
            out.writeObject(faseAtual);
            System.out.println("Fase salva com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar fase: " + e.getMessage());
        }
    }

    public ArrayList<Personagem> carregarFase() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("fase_salva.dat"))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList<?> fase) {
                System.out.println("Fase carregada com sucesso.");
                return (ArrayList<Personagem>) fase;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar fase: " + e.getMessage());
        }
        return null;
    }

    public void desenhaTudo(ArrayList<Personagem> e) {
        for (int i = 0; i < e.size(); i++) {
            e.get(i).autoDesenho();
        }

        if (!e.isEmpty() && e.get(0) instanceof Hero) {
            Hero hero = (Hero) e.get(0);
            Graphics g = Desenho.acessoATelaDoJogo().getGraphics();
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Vidas: " + hero.getVidas() + " | Pontuação: " + hero.getPontuacao(), 10, 20);
        }
    }

    public void processaTudo(ArrayList<Personagem> umaFase) {
        if (umaFase == null || umaFase.isEmpty() || !(umaFase.get(0) instanceof Hero)) {
            return;
        }

        Hero hero = (Hero) umaFase.get(0);

        // Inicializa as vidas persistentes, se ainda não estiverem definidas
        if (vidasPersistentes == -1) {
            vidasPersistentes = hero.getVidas();
        } else {
            hero.setVidas(vidasPersistentes); // Garante consistência das vidas ao trocar de fase
        }

        for (int i = 1; i < umaFase.size(); i++) {
            Personagem p = umaFase.get(i);

            // Colisão com o herói
            if (hero.getPosicao().igual(p.getPosicao())) {
                if (p instanceof Moeda) {
                    hero.addPontuacao();
                    umaFase.remove(i--);
                    continue;
                }

                if (p instanceof Chave) {
                    hero.coletarChave();
                    umaFase.remove(i--);
                    System.out.println("Herói coletou a chave!");
                    hero.setProntoParaTrocarFase(true);
                    continue;
                }

                if (p.isbMortal()) {
                    if (hero.podeLevarDano()) {
                        hero.perderVida();
                        vidasPersistentes = hero.getVidas(); // Atualiza as vidas globais
                        System.out.println("Herói perdeu uma vida! Vidas: " + hero.getVidas()
                                + " | Pontuação: " + hero.getPontuacao());

                        if (!hero.estaVivo()) {
                            System.out.println("Herói morreu. Reiniciando o jogo...");
                            vidasPersistentes = -1; // Reinicia as vidas globais
                            Tela tela = Desenho.acessoATelaDoJogo();
                            tela.resetarParaFase1();
                            return;
                        }
                    } else {
                        hero.permitirDanoNovamente();
                    }
                    break;
                }
            }
        }

        // Movimento dos Chasers
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem p = umaFase.get(i);
            if (p instanceof Chaser) {
                ((Chaser) p).mover(umaFase, this, hero.getPosicao());
            }
        }
    }

    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p) {
        for (int i = 1; i < umaFase.size(); i++) {
            Personagem pIesimoPersonagem = umaFase.get(i);
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void resetarVidas() {
        vidasPersistentes = -1;
    }
}