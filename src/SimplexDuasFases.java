import java.util.Scanner;

public class SimplexDuasFases {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //Variáveis para -> x1, x2, ..., xn
        System.out.print("Informe o Número de Variáveis: ");
        int n = sc.nextInt();

        //Quantidade de Restricoes
        System.out.print("Informe o Número de Restrições: ");
        int m = sc.nextInt();
        System.out.println();

        //Restricoes
        double[][] a = new double[m][n];

        //Valor das restricoes
        double[] b = new double[m];

        //Funcao Objetivo
        double[] z = new double[n];

        //Tipo das Restricao
        String[] tipo = new String[m];

        //Contadores
        int qtdFolga = 0;
        int qtdExcesso = 0;
        int qtdArtificial = 0;

        //Montando a Função Objetivo (z)
        for (int i = 0; i < n; i++) {
            System.out.print("Informe o coeficiente x" + (i + 1) + ": ");
            z[i] = sc.nextDouble();
        }

        System.out.print("Função objetivo: z = ");
        for (int i = 0; i < n; i++) {
            if (i == n - 1) {
                System.out.print("(" + z[i] + ")x" + (i + 1));
            } else {
                System.out.print("(" + z[i] + ")x" + (i + 1) + " + ");
            }
        }
        System.out.println();

        //Montando as Restricoes
        for (int i = 0; i < m; i++) {
            System.out.println("\nRestrição " + (i + 1));

            for (int j = 0; j < n; j++) {
                System.out.print("Informe o coeficiente x" + (j + 1) + ": ");
                a[i][j] = sc.nextDouble();
            }

            System.out.print("Tipo (<=, >=, =): ");
            tipo[i] = sc.next();

            System.out.print("Resultado (b): ");
            b[i] = sc.nextDouble();

            //contagem da quantidade de variaveis auxiliares
            if (tipo[i].equals("<=")) {
                qtdFolga++;

            } else if (tipo[i].equals(">=")) {
                qtdExcesso++;
                qtdArtificial++;

            } else {
                qtdArtificial++;
            }
        }

        //variavel basica
        String[] var = new String[m];

        //Tamanho da tabela
        int totalColunas = n + qtdFolga + qtdExcesso + qtdArtificial + 1;

        //Tableau (+1 linha para W)
        double[][] tableau = new double[m + 2][totalColunas];

        int colFolga = n;
        int colExcesso = n + qtdFolga;
        int colArtificial = n + qtdFolga + qtdExcesso;

        int f = 0;
        int e = 0;
        int art = 0;

        //Montando o Tableu
        //Preenchendo a tabela com as Restricoes
        for (int i = 0; i < m; i++) {
            //variáveis normais
            for (int j = 0; j < n; j++) {
                tableau[i][j] = a[i][j];
            }

            //folga
            if (tipo[i].equals("<=")) {
                tableau[i][colFolga + f] = 1;
                var[i] = "f" + (f + 1);
                f++;
            }

            //excesso e artificial
            else if (tipo[i].equals(">=")) {
                tableau[i][colExcesso + e] = -1;
                tableau[i][colArtificial + art] = 1;

                var[i] = "a" + (art + 1);

                e++;
                art++;
            }

            //apenas artificial
            else {
                tableau[i][colArtificial + art] = 1;
                var[i] = "a" + (art + 1);
                art++;
            }

            //resultado
            tableau[i][totalColunas - 1] = b[i];
        }

        //Funcao Objetivo
        for (int j = 0; j < n; j++) {
            tableau[m][j] = -z[j];
        }

        //Função W
        for (int j = 0; j < qtdArtificial; j++) {
            tableau[m + 1][colArtificial + j] = -1;
        }

        //Como as variáveis artificiais já estão na base, precisamos zerar seus coeficientes na linha W
        for (int i = 0; i < m; i++) {
            if (var[i].startsWith("a")) {
                for (int j = 0; j < totalColunas; j++) {
                    tableau[m + 1][j] += tableau[i][j];
                }
            }
        }

        //Mostrando a Primeira Tabela
        System.out.println("Tabela Inicial: ");
        imprimirTableau(tableau, var, n, qtdFolga, qtdExcesso, qtdArtificial);

        //Resultado Final
        resolverTableu(tableau, var, n, qtdFolga, qtdExcesso, qtdArtificial);
    }

    public static void resolverTableu(double[][] tableau, String[] var, int n, int qtdFolga, int qtdExcesso, int qtdArtificial) {
        int m = tableau.length - 2;
        int totalColunas = tableau[0].length;

        //FASE 1
        if (qtdArtificial > 0) {
            //Loop de iterações da Fase 1
            while (true) {

                int colunaPivo = -1;
                double maiorW = 0;

                //Buscando maior valor positivo da linha W
                for (int j = 0; j < totalColunas - 1; j++) {
                    if (tableau[m + 1][j] > maiorW) {
                        maiorW = tableau[m + 1][j];
                        colunaPivo = j;
                    }
                }

                //Se não houver nenhum valor positivo maior que zero, a Fase 1 terminou
                if (colunaPivo == -1 || maiorW <= 1e-9) {
                    break;
                }

                //Linha do menor Pivo
                int linhaPivo = -1;
                double menorRazao = Double.MAX_VALUE;

                for (int i = 0; i < m; i++) {
                    double elemento = tableau[i][colunaPivo];

                    //evita divisão por zero ou negativos
                    if (elemento > 1e-9) {
                        double razao = tableau[i][totalColunas - 1] / elemento;
                        if (razao < menorRazao) {
                            menorRazao = razao;
                            linhaPivo = i;
                        }
                    }
                }

                //problema ilimitado
                if (linhaPivo == -1) {
                    System.out.println("Solução ilimitada.");
                    return;
                }

                //Muda a variável básica
                atualizarVariavelBasica(var, linhaPivo, colunaPivo, n, qtdFolga, qtdExcesso);

                //Normalizando a linha do pivo
                double pivo = tableau[linhaPivo][colunaPivo];

                for (int j = 0; j < totalColunas; j++) {
                    tableau[linhaPivo][j] /= pivo;
                }

                //Zerando coluna do pivo
                for (int i = 0; i < tableau.length; i++) {
                    if (i != linhaPivo) {
                        double fator = tableau[i][colunaPivo];
                        for (int j = 0; j < totalColunas; j++) {
                            tableau[i][j] -= fator * tableau[linhaPivo][j];
                        }
                    }
                }
            }

            //Verifica se W é igual a zero
            if (Math.abs(tableau[m + 1][totalColunas - 1]) > 1e-5) {
                System.out.println("O problema não possui solução viável (W final != 0).");
                return;
            }

            //Zerando coluna das variáveis artificiais
            int inicioArtificiais = n + qtdFolga + qtdExcesso;
            for (int i = 0; i < tableau.length; i++) {
                for (int j = inicioArtificiais; j < inicioArtificiais + qtdArtificial; j++) {
                    tableau[i][j] = 0;
                }
            }
        }

        //FASE 2
        //Como a base mudou na Fase 1, precisamos garantir que as variáveis básicas atuais estejam zeradas na linha de Z
        for (int i = 0; i < m; i++) {
            String vBasica = var[i];
            int colunaBasica = -1;

            //Descobre o índice da coluna da variável básica atual
            if (vBasica.startsWith("x")) {
                colunaBasica = Integer.parseInt(vBasica.substring(1)) - 1;
            } else if (vBasica.startsWith("f")) {
                colunaBasica = n + Integer.parseInt(vBasica.substring(1)) - 1;
            } else if (vBasica.startsWith("e")) {
                colunaBasica = n + qtdFolga + Integer.parseInt(vBasica.substring(1)) - 1;
            }
            if (colunaBasica != -1 && Math.abs(tableau[m][colunaBasica]) > 1e-9) {
                double fator = tableau[m][colunaBasica];
                for (int j = 0; j < totalColunas; j++) {
                    tableau[m][j] -= fator * tableau[i][j];
                }
            }
        }

        //Execução do Simplex 
        while (true) {

            int colunaPivo = 0;
            double menor = tableau[m][0];

            //Procura o menor valor negativo na linha Z
            int limite = n + qtdFolga + qtdExcesso;

            for (int j = 1; j < limite; j++) {
                if (tableau[m][j] < menor) {
                    menor = tableau[m][j];
                    colunaPivo = j;
                }
            }

            //Critério de parada
            if (menor >= -1e-9) {
                break;
            }

            //Encontra a linha pivô pela menor razão
            int linhaPivo = -1;
            double menorRazao = Double.MAX_VALUE;

            for (int i = 0; i < m; i++) {
                double elemento = tableau[i][colunaPivo];
                if (elemento > 1e-9) {
                    double razao = tableau[i][totalColunas - 1] / elemento;
                    if (razao < menorRazao) {
                        menorRazao = razao;
                        linhaPivo = i;
                    }
                }
            }

            //problema ilimitado
            if (linhaPivo == -1) {
                System.out.println("Solução ilimitada detectada na Fase 2.");
                return;
            }

            //Troca a variável da base
            atualizarVariavelBasica(var, linhaPivo, colunaPivo, n, qtdFolga, qtdExcesso);

            //Normalização da linha pivô
            double pivo = tableau[linhaPivo][colunaPivo];

            for (int j = 0; j < totalColunas; j++) {
                tableau[linhaPivo][j] /= pivo;
            }

            //Eliminação de Gauss-Jordan para zerar a coluna pivô
            for (int i = 0; i < m + 1; i++) {
                if (i != linhaPivo) {
                    double fator = tableau[i][colunaPivo];
                    for (int j = 0; j < totalColunas; j++) {
                        tableau[i][j] -= fator * tableau[linhaPivo][j];
                    }
                }
            }
        }

        //Tabela Final
        System.out.println("Tabela Final:");
        imprimirTableau(tableau, var, n, qtdFolga, qtdExcesso, qtdArtificial);
        System.out.println();

        //Solução Ótima
        System.out.println("Solução ótima encontrada:");

        //Variáveis de decisão (x1, x2, ..., xn)
        double[] valoresX = new double[n];

        for(int i = 0; i < m; i++){
            if(var[i].startsWith("x")){
                int indice = Integer.parseInt(var[i].substring(1)) - 1;
                valoresX[indice] = tableau[i][totalColunas - 1];
            }
        }

        //Mostra todas as variáveis de decisão
        for(int i = 0; i < n; i++){
            System.out.printf("x%d = %.2f\n", i + 1, valoresX[i]);
        }

        System.out.printf("Z = %.2f\n", tableau[m][totalColunas - 1]);
    }

    //Método para muda a variavel básica na tabela final
    private static void atualizarVariavelBasica(String[] var, int linhaPivo, int colunaPivo, int n, int qtdFolga, int qtdExcesso) {
        if (colunaPivo < n) {
            var[linhaPivo] = "x" + (colunaPivo + 1);
        } else if (colunaPivo < n + qtdFolga) {
            var[linhaPivo] = "f" + (colunaPivo - n + 1);
        } else if (colunaPivo < n + qtdFolga + qtdExcesso) {
            var[linhaPivo] = "e" + (colunaPivo - n - qtdFolga + 1);
        } else {
            var[linhaPivo] = "a" + (colunaPivo - n - qtdFolga - qtdExcesso + 1);
        }
    }

    public static void imprimirTableau(double[][] tableau, String[] var, int n, int qtdFolga, int qtdExcesso, int qtdArtificial) {
        System.out.printf("| %-18s ", "Variaveis Basicas");

        //Variaveis
        for (int i = 0; i < n; i++) {
            System.out.printf("| %-8s ", "x" + (i + 1));
        }

        //Variaveis de folga
        for (int i = 0; i < qtdFolga; i++) {
            System.out.printf("| %-8s ", "f" + (i + 1));
        }

        //Variaveis de excesso
        for (int i = 0; i < qtdExcesso; i++) {
            System.out.printf("| %-8s ", "e" + (i + 1));
        }

        //Variaveis artificial
        for (int i = 0; i < qtdArtificial; i++) {
            System.out.printf("| %-8s ", "a" + (i + 1));
        }

        //Coluna de valores das restricoes
        System.out.printf("| %-8s |\n", "b");
        int m = tableau.length - 2;

        //Linhas das restrições
        for (int i = 0; i < m; i++) {
            System.out.printf("| %-18s ", var[i]);
            for (int j = 0; j < tableau[0].length; j++) {
                System.out.printf("| %-8.2f ", tableau[i][j]);
            }
            System.out.println("|");
        }

        //Linha da função W
        System.out.printf("| %-18s ", "W");
        for (int j = 0; j < tableau[0].length; j++) {
            System.out.printf("| %-8.2f ", tableau[m + 1][j]);
        }
        System.out.println("|");


        //Linha da função objetivo
        System.out.printf("| %-18s ", "Z");
        for (int j = 0; j < tableau[0].length; j++) {
            System.out.printf("| %-8.2f ", tableau[m][j]);
        }
        System.out.println("|");
    }
}