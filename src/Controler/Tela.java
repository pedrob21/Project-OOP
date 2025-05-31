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
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        /*mouse*/

        this.addKeyListener(this);
        /*teclado*/
 /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        faseAtual = new ArrayList<Personagem>();

        /*Cria faseAtual adiciona personagens*/
        hero = new Hero("Robbo.png");
        hero.setPosicao(3, 3);
        this.addPersonagem(hero);
        this.atualizaCamera();       
        
        for (int x = 2; x <= 25; x++) {
            for (int y = 2; y <= 25; y++) {
                if (x == 2 || x == 25 || y == 2 || y == 25) {
                    Parede p = new Parede("bricks.png");
                    p.setPosicao(x, y);
                    this.addPersonagem(p);
                }
            }
        }

        for (int y = 3; y <= 18; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(5, y);
            this.addPersonagem(p);
        }
        
        for (int x = 5; x <= 9; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 18);
            this.addPersonagem(p);
        }
        
        for (int x = 3; x <= 9; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 21);
            this.addPersonagem(p);
        }
        
        // retangulo 1
        for (int y = 21; y <= 25; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(9, y);
            this.addPersonagem(p);
        }
        
        for (int y = 15; y <= 17; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(9, y);
            this.addPersonagem(p);
        }
        
        for (int y = 15; y <= 18; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(11, y);
            this.addPersonagem(p);
        }
        
        for (int y = 21; y <= 25; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(11, y);
            this.addPersonagem(p);
        }
        
        for (int x = 9; x <= 11; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 14);
            this.addPersonagem(p);
        }
        
        // retangulo 2
        
        for (int x = 12; x <= 16; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 18);
            this.addPersonagem(p);
        }
        
        for (int x = 12; x <= 16; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 21);
            this.addPersonagem(p);
        }
        
        for (int y = 15; y <= 18; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(16, y);
            this.addPersonagem(p);
        }
        
        for (int y = 21; y <= 25; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(16, y);
            this.addPersonagem(p);
        }
        
        for (int y = 15; y <= 18; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(18, y);
            this.addPersonagem(p);
        }
        
        for (int y = 21; y <= 25; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(18, y);
            this.addPersonagem(p);
        }
        
        for (int x = 16; x <= 18; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 14);
            this.addPersonagem(p);
        }
        
        // fim r2
        
        for (int x = 19; x <= 24; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 21);
            this.addPersonagem(p);
        }
        
        for (int x = 19; x <= 22; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 18);
            this.addPersonagem(p);
        }
        
        for (int y = 11; y <= 17; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(22, y);
            this.addPersonagem(p);
        }
        
        for (int y = 5; y <= 9; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(22, y);
            this.addPersonagem(p);
        }
        
        for (int x = 19; x <= 21; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 9);
            this.addPersonagem(p);
        }
        
        for (int x = 19; x <= 21; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 11);
            this.addPersonagem(p);
        }
        
        for (int y = 9; y <= 11; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(19, y);
            this.addPersonagem(p);
        }
        
        for (int x = 15; x <= 21; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 5);
            this.addPersonagem(p);
        }
        
        for (int y = 6; y <= 17; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(15, y);
            this.addPersonagem(p);
        }
        
        for (int y = 6; y <= 17; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(12, y);
            this.addPersonagem(p);
        }
        
        for (int x = 8; x <= 12; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 5);
            this.addPersonagem(p);
        }
        
        for (int x = 5; x <= 8; x++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(x, 17);
            this.addPersonagem(p);
        }
        
        for (int x = 3; x <= 8; x++) {
            for (int y = 22; y <=24; y++){
                Parede p = new Parede("bricks.png");
                p.setPosicao(x, y);
                this.addPersonagem(p);
            }
        }
        
        for (int x = 12; x <= 15; x++) {
            for (int y = 22; y <=24; y++){
                Parede p = new Parede("bricks.png");
                p.setPosicao(x, y);
                this.addPersonagem(p);
            }
        }
        
        for (int x = 19; x <= 24; x++) {
            for (int y = 22; y <= 24; y++){
                Parede p = new Parede("bricks.png");
                p.setPosicao(x, y);
                this.addPersonagem(p);
            }
        }
        
        for (int x = 16; x <= 21; x++) {
            for (int y = 6; y <= 8; y++){
                Parede p = new Parede("bricks.png");
                p.setPosicao(x, y);
                this.addPersonagem(p);
            }
        }
        
        for (int x = 19; x <= 21; x++) {
            for (int y = 12; y <= 17; y++){
                Parede p = new Parede("bricks.png");
                p.setPosicao(x, y);
                this.addPersonagem(p);
            }
        }
        
        for (int x = 16; x <= 18; x++) {
            for (int y = 9; y <= 13; y++){
                Parede p = new Parede("bricks.png");
                p.setPosicao(x, y);
                this.addPersonagem(p);
            }
        }
        
        for (int y = 3; y <= 16; y++) {
            Parede p = new Parede("bricks.png");
            p.setPosicao(6, y);
            this.addPersonagem(p);
        }
        
        BichinhoVaiVemVertical bvv1 = new BichinhoVaiVemVertical("Robbo.png", 2, 2);
        bvv1.setPosicao(9, 7);
        this.addPersonagem(bvv1);
        
        BichinhoVaiVemVertical bvv2 = new BichinhoVaiVemVertical("Robbo.png", 2, 2);
        bvv2.setPosicao(9, 9);
        this.addPersonagem(bvv2);
        
        BichinhoVaiVemVertical bvv3 = new BichinhoVaiVemVertical("Robbo.png", 2, 2);
        bvv3.setPosicao(9, 11);
        this.addPersonagem(bvv3);
        
        BichinhoVaiVemVertical bvv4 = new BichinhoVaiVemVertical("Robbo.png", 2, 2);
        bvv4.setPosicao(9, 13);
        this.addPersonagem(bvv4);
        
        BichinhoVaiVemHorizontal bvh1 = new BichinhoVaiVemHorizontal("Robbo.png", 4, 4);
        bvh1.setPosicao(10, 20);
        this.addPersonagem(bvh1);
        
        BichinhoVaiVemHorizontal bvh2 = new BichinhoVaiVemHorizontal("Robbo.png", 4, 4);
        bvh2.setPosicao(17, 20);
        this.addPersonagem(bvh2);
        
        BichinhoVaiVemVertical bvv5 = new BichinhoVaiVemVertical("Robbo.png", 1, 0);
        bvv5.setPosicao(14, 9);
        this.addPersonagem(bvv5);
        
        BichinhoVaiVemVertical bvv6 = new BichinhoVaiVemVertical("Robbo.png", 1, 0);
        bvv6.setPosicao(14, 12);
        this.addPersonagem(bvv6);
        
        BichinhoVaiVemVertical bvv7 = new BichinhoVaiVemVertical("Robbo.png", 1, 0);
        bvv7.setPosicao(14, 6);
        this.addPersonagem(bvv7);
        
        BichinhoVaiVemVertical bvv8 = new BichinhoVaiVemVertical("Robbo.png", 1, 0);
        bvv8.setPosicao(14, 15);
        this.addPersonagem(bvv8);
        
        BichinhoVaiVemHorizontal bvh3 = new BichinhoVaiVemHorizontal("Robbo.png", 7, 10);
        bvh3.setPosicao(23, 10);
        this.addPersonagem(bvh3);
        
        Moeda m1 = new Moeda("Caveira.png");
        m1.setPosicao(7, 16);
        this.addPersonagem(m1);
        
        Moeda m2 = new Moeda("Caveira.png");
        m2.setPosicao(8, 16);
        this.addPersonagem(m2);
        
        Moeda m3 = new Moeda("Caveira.png");
        m3.setPosicao(10, 15);
        this.addPersonagem(m3);
        
        Moeda m4 = new Moeda("Caveira.png");
        m4.setPosicao(17, 15);
        this.addPersonagem(m4);
        
        Moeda m5 = new Moeda("Caveira.png");
        m5.setPosicao(20, 10);
        this.addPersonagem(m5);
        
        Moeda m6 = new Moeda("Caveira.png");
        m6.setPosicao(13, 17);
        this.addPersonagem(m6);
        
        Moeda m7 = new Moeda("Caveira.png");
        m7.setPosicao(14, 17);
        this.addPersonagem(m7);
             
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

    // ***********Desenha cenário de fundo***********
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

    // Desenha todos os personagens e processa lógica
    if (!this.faseAtual.isEmpty()) {
        this.cj.desenhaTudo(faseAtual);
        this.cj.processaTudo(faseAtual);

        // >>>> Desenha o número de vidas no rodapé da tela <<<<
        if (faseAtual.get(0) instanceof Modelo.Hero hero) {
            int y = Consts.RES * Consts.CELL_SIDE - 10;
            g2.setColor(java.awt.Color.WHITE);
            g2.fillRect(0, y - 20, getWidth(), 30); // faixa branca
            g2.setColor(java.awt.Color.BLACK);
            g2.drawString("Vidas: " + hero.getVidas(), 10, y);
        }
    }

    g.dispose();
    g2.dispose();
    if (!getBufferStrategy().contentsLost()) {
        getBufferStrategy().show();
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
        this.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", "
                + (hero.getPosicao().getLinha()));

        //repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
    }

    public void mousePressed(MouseEvent e) {
        /* Clique do mouse desligado*/
        int x = e.getX();
        int y = e.getY();

        this.setTitle("X: " + x + ", Y: " + y
                + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

        this.hero.getPosicao().setPosicao(y / Consts.CELL_SIDE, x / Consts.CELL_SIDE);

        repaint();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
    }// </editor-fold>                        
    // Variables declaration - do not modify                     
    // End of variables declaration                   

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
