import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.math.RoundingMode;

class Pessoa {
    String nome;
    LocalDate dataNascimento;

    Pessoa(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }
}

class Funcionario extends Pessoa {
    BigDecimal salario;
    String funcao;

    Funcionario(String nome, LocalDate dataNascimento, String salario, String funcao) {
        super(nome, dataNascimento);
        this.salario = new BigDecimal(salario);
        this.funcao = funcao; 
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        Locale brasil = Locale.forLanguageTag("pt-BR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(brasil);

        return (
            "Nome: " + nome + 
            " | Data de Nascimento: " + dataNascimento.format(formatter) + 
            " | Salário: " + currencyFormatter.format(salario) + 
            " | Funcção: " + funcao
        );
    }
    
}

public class Teste {
    public static void main(String[] args) {
        List<Funcionario> funcionarios = new ArrayList<>();
        
        funcionarios.addAll(Arrays.asList(
            new Funcionario("Maria", LocalDate.of(2000, 10, 18), "2009.44", "Operador"),
            new Funcionario("João", LocalDate.of(1990, 5, 12), "2284.38", "Operador"),
            new Funcionario("Caio", LocalDate.of(1961, 5, 2), "9836.14", "Coordenador"),
            new Funcionario("Miguel", LocalDate.of(1988, 10, 14), "19119.88", "Diretor"),
            new Funcionario("Alice", LocalDate.of(1995, 1, 5), "2234.68", "Rececpionista"),
            new Funcionario("Heitor", LocalDate.of(1999, 11, 19), "1582.72", "Operador"),
            new Funcionario("Arthur", LocalDate.of(1993, 3, 31), "4071.84", "Contador"),
            new Funcionario("Laura", LocalDate.of(1994, 7, 8), "3017.45", "Gerente"),
            new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), "1606.85", "Eletricista"),
            new Funcionario("Helena", LocalDate.of(1996, 9, 2), "2799.93", "Gerente")
        ));

        System.out.println("Lista inicial de Funcionários:");
        listarFuncionarios(funcionarios);

        removerFuncionario(funcionarios, "João");

        aumentoSalario(funcionarios, new BigDecimal(10));

        System.out.println("\nLista atualizada (após remoção de um funcionário e aumento de salário):"); 
        listarFuncionarios(funcionarios);
        
        agruparPorFuncao(funcionarios);

        agruparPorMesAniversario(funcionarios, 10);

        agruparPorMesAniversario(funcionarios, 12);

        maiorIdade(funcionarios);

        ordenarPorNome(funcionarios);

        calcularSalarioTotal(funcionarios);

        calcularSalarioMinimo(funcionarios, new BigDecimal(1212.00));
    }

    // lista os funcionáriso no console
    static void listarFuncionarios(List<Funcionario> funcionarios) {
        for (Funcionario f : funcionarios) {
            System.out.println(f);
        }
    }

    // remove o funcionário plo nome
    public static void removerFuncionario(List<Funcionario> funcionarios, String nome) {
        funcionarios.removeIf(f -> f.nome.equals(nome));
    }

    // aumenta o salário de todos os func. com determinado valor em %
    public static void aumentoSalario(List<Funcionario> funcionarios, BigDecimal aumentoPercentual) {
        for (Funcionario f: funcionarios) {
            BigDecimal novoSalario;
            novoSalario = f.salario.add(f.salario.multiply(aumentoPercentual.divide(new BigDecimal(100))));
            f.salario = novoSalario.setScale(2, RoundingMode.HALF_UP);
        }
    }

    // agrupa e imprime a lista por função
    public static void agruparPorFuncao(List<Funcionario> funcionarios) {
        Map<String, List<Funcionario>> funcionariosPorFuncao = new HashMap<>();

        for (Funcionario funcionario : funcionarios) {
            funcionariosPorFuncao
                .computeIfAbsent(funcionario.funcao, k -> new ArrayList<>())
                .add(funcionario);
        }
        for (Map.Entry<String, List<Funcionario>> entry : funcionariosPorFuncao.entrySet()) {
            System.out.println("\nTipo de função: " + entry.getKey());
            for (Funcionario f : entry.getValue()) {
                System.out.println(f.toString());
            }
        }
    }
    
    // agrupa e imprime a lista por mês de aniversário especificado
    public static void agruparPorMesAniversario(List<Funcionario> funcionarios, int mes) {
        Map<Integer, List<Funcionario>> funcionarioPorMes = new HashMap<>();
        for (Funcionario funcionario: funcionarios) {
            if (funcionario.dataNascimento.getMonthValue() != mes) {
                continue;
            }
            funcionarioPorMes
                .computeIfAbsent(mes, k -> new ArrayList<>())
                .add(funcionario);
        }
        for (Map.Entry<Integer, List<Funcionario>> entry: funcionarioPorMes.entrySet()) {
            System.out.println("\nMês de aniversário: " + entry.getKey());
            for (Funcionario f: entry.getValue()) {
                System.out.println(f.toString());
            }
        }
    }

    // encontra quem tem maior idade e imprime o nome e a idade
    static void maiorIdade(List<Funcionario> funcionarios) {
        Funcionario maisVelho = funcionarios.get(0);
        for (Funcionario f : funcionarios) {
            if (f.dataNascimento.isBefore(maisVelho.dataNascimento)) {
                maisVelho = f;
            }
        }
        int idade = Period.between(maisVelho.dataNascimento, LocalDate.now()).getYears();
        System.out.println(
            "\nFuncionário com maior idade é "+ maisVelho.nome + " e tem " + idade + " anos"
        );
    }

    // ordena a lista de funcionários por nome
    public static void ordenarPorNome(List<Funcionario> funcionarios) {
        funcionarios.sort((f1, f2) -> f1.nome.compareTo(f2.nome));
        System.out.println("\nFuncionários em ordem alfabética:");
        listarFuncionarios(funcionarios);
    }

    // calcula o salário total de todos os funcionários
    public static BigDecimal calcularSalarioTotal(List<Funcionario> funcionariosLista) {
        Locale brasil = Locale.forLanguageTag("pt-BR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(brasil);

        BigDecimal total = new BigDecimal(0);

        for (Funcionario f : funcionariosLista) {
            total = total.add(f.salario);
        }
        System.out.println("\nSalário total: " + currencyFormatter.format(total));
        return total;
    }

    // calcula quantos salários mínimos cada funcionário recebe
    public static void calcularSalarioMinimo(List<Funcionario> funcionarios, BigDecimal salarioMinimo) {
        System.out.println("\nSalários mínimos de cada funcionário:");
        for (Funcionario f : funcionarios) {
            BigDecimal quantidadeSalarios = f.salario.divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.println(f.nome + " recebe " + quantidadeSalarios + " salários mínimos.");
        }
    }
}
