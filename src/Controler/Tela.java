package Controler;

import Modelo.Personagem;
import Modelo.Chico;
import Modelo.Fase;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.LeitorMapa;
import Modelo.AnaMaria;
import Modelo.BichinhoContrarioHorizontal;
import Modelo.BichinhoContrarioVertical;
import Modelo.BichinhoVaiVemHorizontal;
import Modelo.BichinhoVaiVemVertical;
import Modelo.Louro;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import java.util.zip.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.imageio.ImageIO;



public class Tela extends JFrame implements MouseListener, KeyListener {

    private Chico hero;
    private Fase fase;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;

    public Tela(String caminhoMapa) {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        this.addKeyListener(this);

        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        this.fase = new Fase(extrairNumeroFase(caminhoMapa), caminhoMapa);
        this.faseAtual = fase.getPersonagens();

        if (!faseAtual.isEmpty() && faseAtual.get(0) instanceof Chico) {
            hero = (Chico) faseAtual.get(0);
        } else {
            System.err.println("Erro: mapa não possui um herói na posição 0!");
            System.exit(1);
        }

        this.atualizaCamera();
        
        new DropTarget(this, new DropTargetAdapter() {
    @Override
    public void drop(DropTargetDropEvent fileDrop) {
        try {
            Point mousePosition = fileDrop.getLocation();
            fileDrop.acceptDrop(DnDConstants.ACTION_COPY);
            Transferable transferable = fileDrop.getTransferable();

            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                @SuppressWarnings("unchecked")
                List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                for (File file : files) {
                    if (!file.getName().endsWith(".zip")) continue;

                    int colunaMouse = (int) mousePosition.getX() / Consts.CELL_SIDE;
                    int linhaMouse = (int) mousePosition.getY() / Consts.CELL_SIDE;
                    Posicao p = new Posicao(linhaMouse, colunaMouse);

                    Tela.this.loadCharacter(file, p); // Você vai implementar esse método
                }
            }

            fileDrop.dropComplete(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            fileDrop.dropComplete(false);
        }
    }
});

    }

    private int extrairNumeroFase(String caminho) {
        try {
            String numero = caminho.replaceAll("\\D+", "");
            return Integer.parseInt(numero);
        } catch (Exception e) {
            return 1;
        }
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

        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "chaoAzulejo.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        if (!this.faseAtual.isEmpty()) {
            this.cj.desenhaTudo(faseAtual);

            if (faseAtual.get(0) instanceof Chico hero) {
                int y = Consts.RES * Consts.CELL_SIDE - 10;
                g2.setColor(java.awt.Color.WHITE);
                g2.fillRect(0, y - 20, getWidth(), 30);
                g2.setColor(java.awt.Color.BLACK);
                g2.drawString("Vidas: " + hero.getVidas() + " | Pontuacao: " + hero.getPontuacao(), 10, y);
            }
        }

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
        int startY = Consts.RES * Consts.CELL_SIDE + 15;

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
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_C) {
            this.faseAtual.clear();
        } else if (key == KeyEvent.VK_UP) {
            hero.moveUp();
        } else if (key == KeyEvent.VK_DOWN) {
            hero.moveDown();
        } else if (key == KeyEvent.VK_LEFT) {
            hero.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            hero.moveRight();
        } else if (key == KeyEvent.VK_S) {
            cj.salvarFase(fase.getPersonagens());
        } else if (key == KeyEvent.VK_R) {
            fase.recarregar();
            faseAtual = fase.getPersonagens();
            if (!faseAtual.isEmpty() && faseAtual.get(0) instanceof Chico h) {
                hero = h;
            }
            this.atualizaCamera();
            System.out.println("Fase reiniciada!");
        } else if (key == KeyEvent.VK_L) {
            ArrayList<Personagem> faseCarregada = cj.carregarFase();
            if (faseCarregada != null) {
                faseAtual = faseCarregada;
                if (!faseAtual.isEmpty() && faseAtual.get(0) instanceof Chico h) {
                    hero = h;
                }
                this.atualizaCamera();
                System.out.println("Fase carregada do save!");
            }
        }

        this.atualizaCamera();
        this.setTitle("-> Cell: " + hero.getPosicao().getLinha() + ", " + hero.getPosicao().getColuna());
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
        int proximoNumero = fase.getNumero() + 1;
        String caminho = "src/maps/fase" + proximoNumero + ".txt";
        Fase novaFase = new Fase(proximoNumero, caminho);
        ArrayList<Personagem> novaLista = novaFase.getPersonagens();

        if (!novaLista.isEmpty() && novaLista.get(0) instanceof Chico novoHero) {
            this.fase = novaFase;
            this.faseAtual = novaLista;
            this.hero = novoHero;
            this.hero.setProntoParaTrocarFase(false);
            this.atualizaCamera();
            System.out.println("Fase " + proximoNumero + " carregada.");
        } else {
            System.out.println("Fim do jogo");
            System.exit(0);
        }
    }

    public Personagem getPersonagemNaPosicao(Posicao p) {
        for (Personagem personagem : this.getFaseAtual()) {
            if (personagem.getPosicao().igual(p)) {
                return personagem;
            }
        }
        return null;
    }

    public void resetarParaFase1() {
        ControleDeJogo.resetarVidas();
        this.fase = new Fase(1, "src/maps/fase1.txt");
        this.faseAtual = fase.getPersonagens();

        if (!faseAtual.isEmpty() && faseAtual.get(0) instanceof Chico novoHero) {
            hero = novoHero;
            hero.setProntoParaTrocarFase(false);
            this.atualizaCamera();
            System.out.println("GAME OVER!!!");
        } else {
            System.err.println("Erro ao reiniciar.");
            System.exit(1);
        }
    }
    
    public void loadCharacter(File zipFile, Posicao posicao) {
    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
        ZipEntry entry;
        BufferedImage imagem = null;
        String classe = null;
        String nomeImagem = null;

        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().toLowerCase().endsWith(".json")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                String linha;
                while ((linha = reader.readLine()) != null) {
                    linha = linha.trim();
                    if (linha.contains("\"classe\"")) {
                        classe = linha.split(":")[1].replace("\"", "").replace(",", "").trim();
                    } else if (linha.contains("\"imagem\"")) {
                        nomeImagem = linha.split(":")[1].replace("\"", "").replace(",", "").trim();
                    }
                }
            } else if (entry.getName().toLowerCase().endsWith(".png")) {
                imagem = ImageIO.read(zis);
                // Salva a imagem fisicamente no projeto (se necessário)
                File out = new File("." + Consts.PATH + entry.getName());
                ImageIO.write(imagem, "png", out);
            }
            zis.closeEntry();
        }

        if (classe != null && nomeImagem != null) {
            Personagem p = criarPersonagemComportamental(classe, nomeImagem);
            if (p != null) {
                p.setPosicao(posicao.getLinha(), posicao.getColuna());
                this.addPersonagem(p);
                System.out.println("Personagem da classe '" + classe + "' adicionado na posição " +
                    posicao.getLinha() + ", " + posicao.getColuna());
            } else {
                System.err.println("Classe não reconhecida: " + classe);
            }
        } else {
            System.err.println("Classe ou imagem não especificada no JSON.");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public Personagem criarPersonagemComportamental(String classe, String imagem) {
    return switch (classe) {
        case "AnaMaria" -> new AnaMaria(imagem);
        case "Chico" -> new Chico(imagem);
        case "Louro" -> new Louro(imagem);
        case "BichinhoVaiVemHorizontal" -> new BichinhoVaiVemHorizontal(imagem, 3, 3);
        case "BichinhoVaiVemVertical" -> new BichinhoVaiVemVertical(imagem, 3, 3);
        case "BichinhoContrarioHorizontal" -> new BichinhoContrarioHorizontal(imagem, 3, 3);
        case "BichinhoContrarioVertical" -> new BichinhoContrarioVertical(imagem, 3, 3);


        default -> null; 
    };
}




}
