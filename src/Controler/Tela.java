package Controler;

import Modelo.Personagem;
import Modelo.Caveira;
import Modelo.Hero;
import Modelo.Chaser;
import Modelo.BichinhoVaiVemHorizontal;
import Modelo.Parede;
import Modelo.Moeda;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.LeitorMapa;
import Modelo.BichinhoVaiVemVertical;
import Modelo.ZigueZague;
import auxiliar.Posicao;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JButton;


public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private int faseAtualNumero = 1;
    private boolean trocarFase = false;
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        this.addKeyListener(this);

        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        faseAtual = LeitorMapa.carregarMapa("src/maps/fase1.txt");

        if (!faseAtual.isEmpty() && faseAtual.get(0) instanceof Hero) {
            hero = (Hero) faseAtual.get(0);
        } else {
            System.err.println("Erro: mapa não possui um herói na posição 0!");
            System.exit(1);
        }

        this.atualizaCamera();
    }
    
    public ArrayList<Personagem> getFaseAtual() {
        return faseAtual;
    }



    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    @Override
    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);

        // Desenha cenário de fundo
        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "blackTile.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        // Desenha personagens
        if (!this.faseAtual.isEmpty()) {
            this.cj.desenhaTudo(faseAtual);

            if (faseAtual.get(0) instanceof Hero hero) {
                int y = Consts.RES * Consts.CELL_SIDE - 10;
                g2.setColor(java.awt.Color.WHITE);
                g2.fillRect(0, y - 20, getWidth(), 30);
                g2.setColor(java.awt.Color.BLACK);
                g2.drawString("Vidas: " + hero.getVidas() + " | Pontuacao: " + hero.getPontuacao(), 10, y);
            }
        }

        // ✅ Desenha o mapa textual
        desenharMapa(g2);

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
        
        this.cj.processaTudo(faseAtual);

        if (hero.isProntoParaTrocarFase()) {
            carregarProximaFase();
        }
    }

    private void desenharMapa(Graphics g) {
        char[][] mapa = LeitorMapa.gerarMapaTextual(faseAtual);

        g.setColor(java.awt.Color.WHITE);
        g.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));

        int startX = 10;
        int startY = Consts.RES * Consts.CELL_SIDE + 15; // abaixo do mapa gráfico

        for (int i = 0; i < Consts.MUNDO_ALTURA; i++) {
            StringBuilder linha = new StringBuilder();
            for (int j = 0; j < Consts.MUNDO_LARGURA; j++) {
                linha.append(mapa[i][j]).append(' ');
            }
            g.drawString(linha.toString(), startX, startY + (i * 12));
        }
    }

    private void atualizaCamera() {
        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
    }

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            this.faseAtual.clear();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            hero.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            hero.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            hero.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            hero.moveRight();
        }
        this.atualizaCamera();
        this.setTitle("-> Cell: " + (hero.getPosicao().getLinha()) + ", "
                + (hero.getPosicao().getColuna()));
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        this.setTitle("X: " + x + ", Y: " + y
                + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

        this.hero.getPosicao().setPosicao(y / Consts.CELL_SIDE, x / Consts.CELL_SIDE);

        repaint();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    
    private void carregarProximaFase() {
        faseAtualNumero++;
        String caminho = "src/maps/fase" + faseAtualNumero + ".txt";

        ArrayList<Personagem> novaFase = LeitorMapa.carregarMapa(caminho);

        if (!novaFase.isEmpty() && novaFase.get(0) instanceof Hero novoHero) {
            faseAtual = novaFase;
            hero = novoHero;
            hero.setProntoParaTrocarFase(false);
            this.atualizaCamera();
            System.out.println("Fase " + faseAtualNumero + " carregada.");
        } else {
            System.out.println("Fim do jogo ou fase inválida.");
            System.exit(0);
        }
    }

}
