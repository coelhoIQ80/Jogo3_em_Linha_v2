package tipos;

public class Peca {
    private char letra;
    private Ponto ponto;
    private boolean remover;
    private int cor;

    public Peca(char letra, Ponto ponto){
        this.letra=letra;
        this.ponto=ponto;
        remover=false;
    }

    public void setLetra(char letra){
        this.letra=letra;
        setCor();
    }
    public void setPonto(Ponto ponto){ this.ponto=ponto;}
    public void remover(){ 
        if (letra!=Dados.VAZIA) remover=true;
    }
    public void setCor(){
        if(letra=='a' || letra=='à' || letra=='á' || letra=='â')
            cor=Dados.AMARELA;
        else if(letra=='u' || letra=='ù' || letra=='ú' || letra=='û')
            cor=Dados.AZUL;
        else if(letra=='e' || letra=='è' || letra=='é' || letra=='ê')
            cor=Dados.VERMELHA;
        else
            cor=Dados.SEM_COR;
    }
    //muda aleatoriamente a cor da peça
    public void mudaCor(){
        if (letra!=Dados.VAZIA){
            cor=(int)(Math.random()*3);//escolhe a cor
            int tipo=tipo();//indice do tipo de letra
            letra=Dados.PECAS[cor][tipo];
        }
    }
    
    //retorna o indice do tipo de letra
    public int tipo(){
        for(int k=0; k<3; k++)
            for(int i=0; i<Dados.P_AMARELA.length; i++)
                if (letra==Dados.PECAS[k][i])
                    return i;
        return 0;
    }

    public char getLetra(){ return letra;}
    public Ponto getPonto(){ return ponto;}
    public int getCor(){ return cor;}
    public boolean getRemover(){return remover;}

    public boolean removido(){
        if (remover){
            remover=false;
            return true;
        }
        return false;
    }

    //retorna true se for peça explosica
    public boolean explo(){
        if(letra=='à' || letra=='ù' || letra=='è')
            return true;
        return false;
    }

    //retorna true se for peça muda cor
    public boolean mCor(){
        if(letra=='â' || letra=='â' || letra=='ê')
            return true;
        return false;
    }

    //retorna true se for peça raio
    public boolean raio(){
        if(letra=='á' || letra=='ú' || letra=='é')
            return true;
        return false;
    }
    
}