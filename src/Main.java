import Controler.Tela;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Use [R] para reiniciar a fase| [S] para salvar | [L] - para dar Load onde voce salvou1 ");
        System.out.println("Escolha a fase:");
        System.out.println("1 - Fase da Loira do Banheiro");
        System.out.println("2 - Estacionamento do Louro");
        System.out.println("3 - Aquela Tabacaria");
        System.out.println("4 - Galeto's Bar");
        System.out.println("5 - Camarim da Ana Maria");
        System.out.print("Digite o número da fase: ");

        int escolha = scanner.nextInt();
        String caminhoMapa = switch (escolha) {
            case 1 -> "./src/maps/fase1.txt";
            case 2 -> "./src/maps/fase2.txt";
            case 3 -> "./src/maps/fase3.txt";
            case 4 -> "./src/maps/fase4.txt";
            case 5 -> "./src/maps/fase5.txt";
            default -> {
                System.out.println("Fase inválida. Carregando fase padrão.");
                yield "maps/fase1.txt";
            }
        };

        String caminhoFinal = caminhoMapa;

        java.awt.EventQueue.invokeLater(() -> {
            Tela tTela = new Tela(caminhoFinal); // agora recebe o caminho
            tTela.setVisible(true);
            tTela.createBufferStrategy(2);
            tTela.go();
        });
    }
}
