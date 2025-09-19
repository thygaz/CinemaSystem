import java.util.Map;
import java.util.Random;

public class Assento {

    // Atributos
    private Map<Character, Integer> assento;

    // Construtor
    public Assento() {
        // Esse random é para criar a letra do assento de forma aleatória e automática
        char inicio = 'A';
        char fim = 'H';

        Random random = new Random();

        int tamanho = fim - (inicio + 1);
        Character fila = (char) (inicio + random.nextInt(tamanho));

        // Esse random é para criar a número do assento de forma aleatória e automática
        int numeroAssento = random.nextInt(35) + 1;

        this.assento.put(fila, numeroAssento);
    }

    // MétodU para obter os assentos disponíveis
//    public static void assentosDisponiveis() {
//        int quantidadeAssentos = random.nextInt(100);
//    }
}
