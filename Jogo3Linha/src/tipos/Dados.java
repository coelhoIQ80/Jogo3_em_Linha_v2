package tipos;


public  class Dados {
    public static final int LINHAS=10, COLUNAS=5;
    public static final int SEM_COR=-1;//sem cor
    public static final int AMARELA=0;//cor amarela
    public static final int AZUL=1;//cor azul
    public static final int VERMELHA=2;//cor vermelha
    public static final char[] P_AMARELA={'a','à','á','â'};//simples,explosiva,raio,muda cor
    public static final char[] P_AZUL={'u','ù','ú','û'};//simples,explosiva,raio,muda cor
    public static final char[] P_VERMELHA={'e','è','é','ê'};//simples,explosiva,raio,muda cor
    public static final char VAZIA=' ';
    public static final char[][] PECAS={P_AMARELA,P_AZUL,P_VERMELHA};

}
