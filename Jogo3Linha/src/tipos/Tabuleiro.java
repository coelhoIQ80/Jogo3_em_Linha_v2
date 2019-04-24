package tipos;


public class Tabuleiro {
    private Peca[][] tabuleiro;
    private int pontuacao;
    private Peca[] pecasPC;
    //metodo para criar o tabuleiro
    public Tabuleiro(){
        pecasPC =new Peca[Dados.COLUNAS];
        tabuleiro=new Peca[Dados.LINHAS][Dados.COLUNAS];
        limpar();
    }

    //coloca todas as peças a branco
    public void limpar(){
        for(int i=0; i<Dados.LINHAS; i++)
            for(int j=0; j<Dados.COLUNAS; j++)
                tabuleiro[i][j]=new Peca(Dados.VAZIA,new Ponto(i,j));
        pontuacao=0;
        escolhePecasPC();
    }

    //retorna o tabuleiro com as peças
    public Peca[][] getTabuleiro(){ return tabuleiro;}

    //retorna a pontuação do jogo
    public int getPontuacao(){ return pontuacao;}


    //retorna a as peças do PC para jogar
    public Peca[] getPecasPC(){ return pecasPC;}

    //troca a peca no ponto o com o ponto d
    public void trocaPeca(Ponto o, Ponto d){
        char c=tabuleiro[o.getX()][o.getY()].getLetra();
        tabuleiro[o.getX()][o.getY()].setLetra(tabuleiro[d.getX()][d.getY()].getLetra());
        tabuleiro[d.getX()][d.getY()].setLetra(c);
    }
    //retorna verdadeiro se existe peça no ponto p senão retorna falso
    public boolean existePeca(Ponto p){
        return tabuleiro[p.getX()][p.getY()].getLetra() != Dados.VAZIA;
    }

    //retorna true se a linha n existir
    public boolean existeLinha(int n){
        return n>=0 && n<Dados.LINHAS;
    }

    //retorna true se a coluna n existir
    public boolean existeColuna(int n){
        return n>=0 && n<Dados.COLUNAS;
    }

    //retorna true se a posicao na linha n e coluna k existir
    public boolean existePosicao(int n, int k){
        return existeLinha(n) && existeColuna(k);
    }

    //move a peça do ponto de origem para o ponto de destino vazio
    public short moverPeca(Ponto origem, Ponto destino){
        if (!existePeca(origem)) return 1; //não existe peça
        if (existePeca(destino)) return 2; //existe peça
        trocaPeca(origem,destino);
        return 0;
    }

    //troca a peça do ponto de origem com o ponto de destino
    public short trocarPeca(Ponto origem, Ponto destino){
        if (!existePeca(origem)) return 1; //não existe peça
        if (!existePeca(destino)) return 2; //não existe peça
        trocaPeca(origem,destino);
        return 0;
    }

    //assina as pecas a remover na linha n antes da coluna k inclusive
    public void remove3NaLinha(int n, int k){
        tabuleiro[n][k].remover();
        tabuleiro[n][k-1].remover();
        tabuleiro[n][k-2].remover();
    }

    //assinala as pecas a remover na coluna k antes da linha n inclusive
    public void remove3NaColuna(int n, int k){
        tabuleiro[n][k].remover();
        tabuleiro[n-1][k].remover();
        tabuleiro[n-2][k].remover();
    }

    //assinala as peças a remover para a peça explosiva
    //n - linha
    //k - coluna
    public void removeExplosiva(int n, int k){
        if(existePosicao(n-1,k-1)) tabuleiro[n-1][k-1].remover();
        if(existePosicao(n,k-1)) tabuleiro[n][k-1].remover();
        if(existePosicao(n+1,k-1)) tabuleiro[n+1][k-1].remover();

        if(existePosicao(n-1,k)) tabuleiro[n-1][k].remover();
        if(existePosicao(n+1,k)) tabuleiro[n+1][k].remover();

        if(existePosicao(n-1,k+1)) tabuleiro[n-1][k+1].remover();
        if(existePosicao(n,k+1)) tabuleiro[n][k+1].remover();
        if(existePosicao(n+1,k+1)) tabuleiro[n+1][k+1].remover();
    }

    //assinala as peças a remover para a peça raio
    //n - linha
    //k - coluna
    public void removeRaio(int n, int k){
        for(int i=0; i<Dados.COLUNAS; i++){//remove as pecas da linha
            if(existePeca(new Ponto(n,k)))
                tabuleiro[n][i].remover();
        }
        for(int i=0; i<Dados.LINHAS; i++){//remove as pecas da coluna
            if(existePeca(new Ponto(n,k)))
                tabuleiro[i][k].remover();
        }
    }
    
    public void mudaCor(int n, int k){
        if(existePosicao(n-1,k-1)) tabuleiro[n-1][k-1].mudaCor();
        if(existePosicao(n,k-1)) tabuleiro[n][k-1].mudaCor();
        if(existePosicao(n+1,k-1)) tabuleiro[n+1][k-1].mudaCor();

        if(existePosicao(n-1,k)) tabuleiro[n-1][k].mudaCor();
        if(existePosicao(n+1,k)) tabuleiro[n+1][k].mudaCor();

        if(existePosicao(n-1,k+1)) tabuleiro[n-1][k+1].mudaCor();
        if(existePosicao(n,k+1)) tabuleiro[n][k+1].mudaCor();
        if(existePosicao(n+1,k+1)) tabuleiro[n+1][k+1].mudaCor();
    }
    
    //verifica se tem 3 ou mais peças iguais seguidas na linha n
    public boolean verifica3NaLinha(int n){
        boolean remover=false;
        int iguais=0;
        for(int cor=0; cor<3; cor++){//começa por ver as AMARELAS
            for(int i=0; i<Dados.COLUNAS-2; i++){
                if (cor!=tabuleiro[n][i].getCor()) continue;//se não for da mesma cor passa para a poxima coluna
                iguais=1;
                for(int k=i+1; k<Dados.COLUNAS; k++){
                    if (cor==tabuleiro[n][k].getCor()) iguais++;
                    else iguais=0;
                    if (iguais>=3){
                        remove3NaLinha(n,k);
                        remover=true;
                    }
                }
            }
        }
        return remover;
    }

    //verifica se tem 3 ou mais peças iguais seguidas na coluna n
    public boolean verifica3NaColuna(int n){
        boolean remover=false;
        int iguais=0;
        for(int cor=0; cor<3; cor++){//começa por ver as AMARELAS
            for(int i=0; i<Dados.LINHAS-2; i++){
                if (cor!=tabuleiro[i][n].getCor()) continue;//se não for da mesma cor passa para a poxima linha
                iguais=1;
                for(int k=i+1; k<Dados.LINHAS; k++){
                    if (cor==tabuleiro[k][n].getCor()) iguais++;
                    else iguais=0;
                    if (iguais>=3){
                        remove3NaColuna(k,n);
                        remover=true;
                    }
                }
            }
        }
        return remover;
    }

    //verifica as linhas e as colunas que tem 3 em linha
    //retorna true se existem peças para remover
    public boolean verifica3EmLinha(){
        boolean remover_linha=false,remover_coluna=false;
        for(int i=0; i<Dados.LINHAS; i++) 
            if(verifica3NaLinha(i)) remover_linha=true;
        for(int i=0; i<Dados.COLUNAS; i++) 
            if(verifica3NaColuna(i)) remover_coluna=true;
        return remover_linha || remover_coluna;
    }

    //verifica as peças de acordo com as especiais
    public void verificaPecasEspeciais(){
        Peca p;
        for(int i=0; i<Dados.LINHAS; i++)
            for(int j=0; j<Dados.COLUNAS; j++){
                p=tabuleiro[i][j];
                if (p.getRemover()){
                    if(p.explo()) removeExplosiva(i,j);
                    else if(p.raio())removeRaio(i,j);
                    else if(p.mCor()) mudaCor(i,j);
                }
            }
    }

    //remove as pecas que estão assinaladas para remover
    //retorna false quando não tem peças para remover
    public boolean removerPecas(){
        boolean remover=verifica3EmLinha();
        if(!remover) return false;
        verificaPecasEspeciais();
        for(int i=0; i<Dados.LINHAS; i++)
            for(int j=0; j<Dados.COLUNAS; j++)
                if (tabuleiro[i][j].removido()){
                    tabuleiro[i][j].setLetra(Dados.VAZIA);
                    System.out.println(i + " , " + j);
                    pontuacao++;
                }
        return true;
    }

    //recoloca as pecas para a posicoes que estão vazias depois de removidas as peças
    public void ordenar(){
        Ponto origem,destino;
        for(int i=Dados.LINHAS-2; i>=0; i--){
            for(int j=0; j<Dados.COLUNAS; j++){
                origem=tabuleiro[i+1][j].getPonto();
                destino=tabuleiro[i][j].getPonto();
                moverPeca(origem,destino);
            }
        }
    }

    //remove e ordena até não haver peças para remover
    public void removerOrdenar(){
        while(removerPecas()) ordenar();
    }

    //PC escolhe as peças para o jogo
    public void escolhePecasPC(){
        int cor,tipo;
        char letra;
        for(int i=0; i<Dados.COLUNAS; i++){
            cor=(int)(Math.random()*3);//escolhe a cor
            tipo=(int)(Math.random()* Dados.P_AMARELA.length);//escolhe o tipo
            letra=Dados.PECAS[cor][tipo];
            pecasPC[i]= new Peca(letra,new Ponto(Dados.LINHAS-1,i));
        }
    }

    //lançamento das peças do PC
    public void jogaPC(){
        for(int i=0; i<Dados.COLUNAS; i++){
            tabuleiro[Dados.LINHAS-1][i]=pecasPC[i];
        }
        ordenar();
    }


    //retorna true se o jogo chegou ao fim
    public boolean fim(){
         for(int i=0; i<Dados.COLUNAS; i++){
             if(tabuleiro[Dados.LINHAS-1][i].getLetra()!=Dados.VAZIA)
                 return true;
         }
         return false;
    }
}
