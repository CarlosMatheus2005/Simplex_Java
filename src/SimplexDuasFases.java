import java.util.Scanner;

public class SimplexDuasFases {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Informe o número de variáveis: ");
        int n = sc.nextInt();

        System.out.print("Informe o número de restrições: ");
        int m = sc.nextInt();

        System.out.println();

        // =========================
        // MATRIZ DAS RESTRIÇÕES
        // =========================
        double[][] a = new double[m][n];

        // =========================
        // RESULTADOS DAS RESTRIÇÕES
        // =========================
        double[] b = new double[m];

        // =========================
        // FUNÇÃO OBJETIVO
        // =========================
        double[] z = new double[n];

        // =========================
        // TIPOS DAS RESTRIÇÕES
        // =========================
        String[] tipo = new String[m];

        // =========================
        // CONTADORES
        // =========================
        int qtdFolga = 0;
        int qtdExcesso = 0;
        int qtdArtificial = 0;

        // =========================
        // FUNÇÃO OBJETIVO
        // =========================
        System.out.println("Função Objetivo:");

        for(int i = 0; i < n; i++){

            System.out.print("Coeficiente x" + (i + 1) + ": ");
            z[i] = sc.nextDouble();
        }

        // =========================
        // LEITURA DAS RESTRIÇÕES
        // =========================
        for(int i = 0; i < m; i++){

            System.out.println("\nRestrição " + (i + 1));

            for(int j = 0; j < n; j++){

                System.out.print("Coeficiente x" + (j + 1) + ": ");
                a[i][j] = sc.nextDouble();
            }

            System.out.print("Tipo (<=, >=, =): ");
            tipo[i] = sc.next();

            System.out.print("Resultado: ");
            b[i] = sc.nextDouble();

            //contagem das variáveis auxiliares
            if(tipo[i].equals("<=")){
                qtdFolga++;

            }else if(tipo[i].equals(">=")){
                qtdExcesso++;
                qtdArtificial++;

            }else{
                qtdArtificial++;
            }
        }

        // =========================
        // MATRIZES AUXILIARES
        // =========================
        double[][] folga = new double[m][qtdFolga];

        double[][] excesso = new double[m][qtdExcesso];

        double[][] artificial = new double[m][qtdArtificial];

        // =========================
        // VARIÁVEIS BÁSICAS
        // =========================
        String[] var = new String[m];

        // =========================
        // PREENCHENDO MATRIZES
        // =========================
        int f = 0;
        int e = 0;
        int art = 0;

        for(int i = 0; i < m; i++){

            if(tipo[i].equals("<=")){

                folga[i][f] = 1;
                var[i] = "f" + (f + 1);

                f++;

            }else if(tipo[i].equals(">=")){

                excesso[i][e] = -1;
                artificial[i][art] = 1;

                var[i] = "a" + (art + 1);

                e++;
                art++;

            }else{

                artificial[i][art] = 1;

                var[i] = "a" + (art + 1);

                art++;
            }
        }

        // =========================
        // TAMANHO DO TABLEAU
        // =========================
        int totalColunas =
                n +
                qtdFolga +
                qtdExcesso +
                qtdArtificial +
                1;

        // =========================
        // TABLEAU
        // =========================
        double[][] tableau = new double[m + 1][totalColunas];

        // =========================
        // MONTANDO RESTRIÇÕES
        // =========================
        for(int i = 0; i < m; i++){

            //variáveis normais
            for(int j = 0; j < n; j++){
                tableau[i][j] = a[i][j];
            }

            //folga
            for(int j = 0; j < qtdFolga; j++){
                tableau[i][n + j] = folga[i][j];
            }

            //excesso
            for(int j = 0; j < qtdExcesso; j++){
                tableau[i][n + qtdFolga + j] = excesso[i][j];
            }

            //artificial
            for(int j = 0; j < qtdArtificial; j++){

                tableau[i][
                        n +
                        qtdFolga +
                        qtdExcesso +
                        j
                ] = artificial[i][j];
            }

            //resultado
            tableau[i][totalColunas - 1] = b[i];
        }

        // =========================
        // FUNÇÃO OBJETIVO
        // =========================
        for(int j = 0; j < n; j++){
            tableau[m][j] = -z[j];
        }

        // =========================
        // TABELA INICIAL
        // =========================
        System.out.println("\nTABLEAU INICIAL:\n");

        imprimirTableau(
                tableau,
                var,
                n,
                qtdFolga,
                qtdExcesso,
                qtdArtificial
        );
    }

    public static void imprimirTableau(
            double[][] tableau,
            String[] var,
            int n,
            int qtdFolga,
            int qtdExcesso,
            int qtdArtificial
    ){

        System.out.printf("| %-15s ", "Base");

        //x
        for(int i = 0; i < n; i++){
            System.out.printf("| %-8s ", "x" + (i + 1));
        }

        //folga
        for(int i = 0; i < qtdFolga; i++){
            System.out.printf("| %-8s ", "f" + (i + 1));
        }

        //excesso
        for(int i = 0; i < qtdExcesso; i++){
            System.out.printf("| %-8s ", "e" + (i + 1));
        }

        //artificial
        for(int i = 0; i < qtdArtificial; i++){
            System.out.printf("| %-8s ", "a" + (i + 1));
        }

        //b
        System.out.printf("| %-8s |\n", "b");

        int m = tableau.length - 1;

        //restrições
        for(int i = 0; i < m; i++){

            System.out.printf("| %-15s ", var[i]);

            for(int j = 0; j < tableau[0].length; j++){

                System.out.printf("| %-8.2f ", tableau[i][j]);
            }

            System.out.println("|");
        }

        //linha z
        System.out.printf("| %-15s ", "Z");

        for(int j = 0; j < tableau[0].length; j++){

            System.out.printf("| %-8.2f ", tableau[m][j]);
        }

        System.out.println("|");
    }
}