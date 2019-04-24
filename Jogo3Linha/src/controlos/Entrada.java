package controlos;

import tipos.Ponto;
import tipos.Tabuleiro;

public class Entrada {

    //converte string para inteiro
    public static int StrToInt(String str){
        int numero=-1;
        try{
            numero=Integer.parseInt(str);
        }catch(Exception e){
            return numero;
        }
        return numero;
    }


    //movimenta as peças do jogador
    public static int Jogador(Ponto origem, Ponto destino, Tabuleiro t){
        short n;
        if(!t.fim()){
            n=t.moverPeca(origem, destino);
            if(n==1) return 1; //não existe peça na origem
            else if(n==2){ 
                n=t.trocarPeca(origem, destino);//existe peça no destino, faz uma troca
                if(n==2) return 2; //não existe peça no destino;
            }
            t.removerOrdenar();
        }
        return 0;
    }

    //movimenta as peças
    public static void PC(Tabuleiro t){
        t.jogaPC();
        t.removerOrdenar();
        t.escolhePecasPC();
    }
    
}