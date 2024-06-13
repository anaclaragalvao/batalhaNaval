package Model;

import java.util.Observable;

class Jogador extends Observable {
    int matriz[][];
    int[][] matrizTiro;
    public String nome;
    private Tiro tiros;

    protected Jogador() {
        this.matriz = new int[15][15];
        this.matrizTiro = new int[15][15];
        cria_tabuleiro();
        this.tiros = new Tiro(15);
    }

    protected void setNome(String nome) {
        this.nome = nome;
        setChanged();
        notifyObservers();
    }

    protected String getNome() {
        return nome;
    }

    protected void cria_tabuleiro() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                matriz[i][j] = 0;
                matrizTiro[i][j] = 0;
            }
        }
        setChanged();
        notifyObservers();
    }

    protected boolean inserirNavio(int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
        System.out.println("MODEL");
        int tamanho = tipoNavio;
        if ((tamanho > 5) || (tamanho < 1)) {
            System.out.println("Esse tamanho nao eh valido.");
            return false;
        }

        if (!posicaoDisponivel(linhaInicial, colunaInicial, tamanho, orientacao)) {
            System.out.println("Posicao indisponivel para inserir o navio.");
            return false;
        }

        if (orientacao.equalsIgnoreCase("leste")) {
            if (tipoNavio == 3) {
                matriz[linhaInicial][colunaInicial] = 3;
                matriz[linhaInicial-1][colunaInicial + 1] = 3;
                matriz[linhaInicial][colunaInicial + 2] = 3;
            } else {
                for (int i = 0; i < tamanho; i++) {
                    matriz[linhaInicial][colunaInicial + i] = tipoNavio;
                }
            }
        } else if (orientacao.equalsIgnoreCase("oeste")) {
            if (tipoNavio == 3) {
                matriz[linhaInicial][colunaInicial] = 3;
                matriz[linhaInicial + 1][colunaInicial - 1] = 3;
                matriz[linhaInicial][colunaInicial - 2] = 3;
            } else {
                for (int i = 0; i < tamanho; i++) {
                    matriz[linhaInicial][colunaInicial - i] = tipoNavio;
                }
            }
        } else if (orientacao.equalsIgnoreCase("norte")) {
            if (tipoNavio == 3) {
                matriz[linhaInicial][colunaInicial] = 3;
                matriz[linhaInicial-1][colunaInicial - 1] = 3;
                matriz[linhaInicial - 2][colunaInicial] = 3;
            } else {
                for (int i = 0; i < tamanho; i++) {
                    matriz[linhaInicial - i][colunaInicial] = tipoNavio;
                }
            }
        } else if (orientacao.equalsIgnoreCase("sul")) {
            if (tipoNavio == 3) {
                matriz[linhaInicial][colunaInicial] = 3;
                matriz[linhaInicial + 1][colunaInicial+1] = 3;
                matriz[linhaInicial + 2][colunaInicial] = 3;
            } else {
                for (int i = 0; i < tamanho; i++) {
                    matriz[linhaInicial + i][colunaInicial] = tipoNavio;
                }
            }
        } else {
            System.out.println("Orientação inválida.");
            return false;
        }

        setChanged();
        notifyObservers();
        return true;
    }

    protected boolean posicaoDisponivel(int linhaInicial, int colunaInicial, int tamanho, String orientacao) {
        // Verificar se a posiÃ§Ã£o inicial estÃ¡ dentro dos limites
        if ((linhaInicial < 0) || (linhaInicial >= 15) || (colunaInicial >= 15) || (colunaInicial < 0)) {
            return false;
        }

     // Verificar se a embarcação está completamente dentro dos limites
        if (orientacao.equalsIgnoreCase("leste")) {
            if (colunaInicial + tamanho > 15) {
                return false;
            }
        } else if (orientacao.equalsIgnoreCase("oeste")) {
            if (colunaInicial - tamanho < -1) {
                return false;
            }
        } else if (orientacao.equalsIgnoreCase("norte")) {
            if (linhaInicial - tamanho < -1) {
                return false;
            }
        } else if (orientacao.equalsIgnoreCase("sul")) {
            if (linhaInicial + tamanho > 15) {
                return false;
            }
        } else {
            return false; // Orientação inválida
        }

        // Verificar se todas as células da embarcação estão vazias
        if (orientacao.equalsIgnoreCase("leste")) {
            for (int i = 0; i < tamanho; i++) {
                if (matriz[linhaInicial][colunaInicial + i] != 0) {
                    return false;
                }
            }
        } else if (orientacao.equalsIgnoreCase("oeste")) {
            for (int i = 0; i < tamanho; i++) {
                if (matriz[linhaInicial][colunaInicial - i] != 0) {
                    return false;
                }
            }
        } else if (orientacao.equalsIgnoreCase("norte")) {
            for (int i = 0; i < tamanho; i++) {
                if (matriz[linhaInicial - i][colunaInicial] != 0) {
                    return false;
                }
            }
        } else if (orientacao.equalsIgnoreCase("sul")) {
            for (int i = 0; i < tamanho; i++) {
                if (matriz[linhaInicial + i][colunaInicial] != 0) {
                    return false;
                }
            }
        }

     // Verificar as células vizinhas, incluindo diagonais
        for (int i = -1; i <= tamanho; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    int linha = linhaInicial + (orientacao.equalsIgnoreCase("leste") ? 0 : (orientacao.equalsIgnoreCase("oeste") ? 0 : (orientacao.equalsIgnoreCase("norte") ? -i : i)));
                    int coluna = colunaInicial + (orientacao.equalsIgnoreCase("leste") ? i : (orientacao.equalsIgnoreCase("oeste") ? -i : (orientacao.equalsIgnoreCase("norte") ? 0 : 0)));
                    if (orientacao.equalsIgnoreCase("leste") || orientacao.equalsIgnoreCase("oeste")) {
                        linha = linhaInicial + j;
                        coluna = colunaInicial + i * (orientacao.equalsIgnoreCase("leste") ? 1 : -1);
                    } else {
                        linha = linhaInicial + i * (orientacao.equalsIgnoreCase("norte") ? -1 : 1);
                        coluna = colunaInicial + j;
                    }
                    if (linha >= 0 && linha < 15 && coluna >= 0 && coluna < 15) {
                        if (matriz[linha][coluna] != 0) {
                            return false;
                        }
                    }
                }
            }
        }
        
     // Verificação específica para hidroaviões
        if (tamanho == 3) {
            int[][] offsets;
            if (orientacao.equalsIgnoreCase("leste")) {
                offsets = new int[][]{{0, 0}, {-1, 1}, {0, 2}};
            } else if (orientacao.equalsIgnoreCase("oeste")) {
                offsets = new int[][]{{0, 0}, {1, -1}, {0, -2}};
            } else if (orientacao.equalsIgnoreCase("norte")) {
                offsets = new int[][]{{0, 0}, {-1, -1}, {-2, 0}};
            } else { // sul
                offsets = new int[][]{{0, 0}, {1, 1}, {2, 0}};
            }

            for (int[] offset : offsets) {
                int linha = linhaInicial + offset[0];
                int coluna = colunaInicial + offset[1];
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int novaLinha = linha + i;
                        int novaColuna = coluna + j;
                        if (novaLinha >= 0 && novaLinha < 15 && novaColuna >= 0 && novaColuna < 15) {
                            if (matriz[novaLinha][novaColuna] != 0) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        
        return true;
    }

    protected void carregarMatrizDeArquivo(String caminhoArquivo) {
        this.matriz = RecuperaMatriz.carregarMatriz(caminhoArquivo);
        for(int i = 0; i< 15; i++){
            for(int j = 0; j< 15; j++){
                System.out.println(matriz[i][j]);
            }
        }


    }


    protected int[][] getMatriz() {
        return matriz;
    }

    protected void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

    protected void salvarMatrizEmArquivo(String caminhoArquivo) {
        SalvadorMatriz.salvarMatriz(matriz, caminhoArquivo);
    }


    protected void resetTabuleiro() {
        this.matriz = new int[15][15];
    }

    protected int registrarTiro(int linha, int coluna) {
        int resultado = tiros.atirar(getMatriz(), linha, coluna);
        /*
        if (resultado != -1) {
            setChanged();
            notifyObservers();
        }

         */
        return resultado;
    }

    /*
    public void registrarTirosView(int linha, int coluna){
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
                if(matriz[i][j]==-10){//acertou água
                    return -10 ;
                }
                else if(jogador1.getMatriz()[i][j]==-1){
                    tiro1 = true;
                }
                else if(jogador1.getMatriz()[i][j]==-2){
                    tiro2 = true;
                }
                else if(jogador1.getMatriz()[i][j]==-3){
                    tiro3 = true;
                }
                else if(jogador1.getMatriz()[i][j]==-4){
                    tiro4 = true;
                }
                else if(jogador1.getMatriz()[i][j]==-5){
                    tiro5 = true;
                }

            }
        }

        }

     */

    protected boolean[][] getTiros() {
        return tiros.getTiros();
    }
}