package jogofx;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.JOptionPane;

import tipos.Dados;
import tipos.Ponto;
import tipos.Tabuleiro;

import controlos.Entrada;

public class Jogo3Linha extends Application {
    private Timeline timeline;
    private static Tabuleiro t=new Tabuleiro();;
    private List<Peca> pecas; //lista de peças do tabuleiro
    private List<Peca> pecasPC; //lista de peças do PC
    private Ponto origem=null,destino=null;
    private Button PCButton,trocarButton,novoButton;
    
    Stage primaryStage1;
    private void init(Stage primaryStage) {
        if(t.fim()){
            msgBox("O Jogo Terminou");
            novoJogo();
        }
        Group root= new Group();
        primaryStage.setScene(new Scene(root));
        primaryStage1=primaryStage;
        tipos.Peca[][] pecas1 = t.getTabuleiro();
        tipos.Peca[] pecasPC1=t.getPecasPC(); // lista das letras para o tabulrito
        int nColunas = Dados.COLUNAS;
        int nLinhas = Dados.LINHAS;
        // criar o topo do tabuleiro
        final TopDesk top = new TopDesk(nColunas);
        pecasPC  = new ArrayList<Peca>();
        for (int col = 0; col < nColunas; col++) {
            Peca piece = new Peca(pecasPC1[col].getLetra(),pecasPC1[col].getPonto() );
            pecasPC.add(piece);
        }
        top.getChildren().addAll(pecasPC);
        // create tabuleiro
        final Desk desk = new Desk(nColunas, nLinhas);
        // criar pecas do tabuleito
        pecas  = new ArrayList<Peca>();
        for (int col = 0; col < nColunas; col++) {
            for (int row = 0; row < nLinhas; row++) {
                Peca piece = new Peca(pecas1[row][col].getLetra(),pecas1[row][col].getPonto() );
                pecas.add(piece);
            }
        }
        desk.getChildren().addAll(pecas);
        // criar botão
        PCButton = new Button("Joga PC");
        PCButton.setStyle("-fx-font-size: 25pt;");
        PCButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                Entrada.PC(t);
                init(primaryStage1);
                origem=destino=null;
                PCButton.setDisable(true);
                trocarButton.setDisable(true);
            }
        });
        trocarButton = new Button("Trocar");
        trocarButton.setDisable(true);
        trocarButton.setStyle("-fx-font-size: 25pt;");
        trocarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                int n=Entrada.Jogador(origem, destino, t);
                if(n==0){
                    init(primaryStage1);
                    PCButton.setDisable(false);
                    trocarButton.setDisable(true);
                    origem=destino=null;
                }else{
                    msgBox("Selecione a Origem e o Destino");
                    origem=destino=null;
                    trocarButton.setDisable(true);
                }
            }
        });
        
        novoButton = new Button("Novo Jogo");
        novoButton.setStyle("-fx-font-size: 17pt;");
        novoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                novoJogo();
                init(primaryStage1);
            }
        });
        
        String pontos="Pontuação: " + t.getPontuacao();
        Button pLabel=new Button(pontos);
        pLabel.setDisable(true);
        pLabel.setStyle("-fx-font-size: 15pt;");
        //cria 
        HBox pBox = new HBox(8);
        pBox.getChildren().addAll(pLabel,novoButton);
        //cria box para os butoes
        HBox buttonBox = new HBox(8);
        buttonBox.getChildren().addAll(PCButton, trocarButton);
        // cria box para tudo
        VBox vb = new VBox(10);
        vb.getChildren().addAll(top,desk,pBox,buttonBox);
        root.getChildren().addAll(vb);
        mostraPecas();
    }
    
    private void novoJogo(){
        t=new Tabuleiro();
        origem=destino=null;
    }
    
    private void msgBox(String s){
        JOptionPane.showMessageDialog(null,s,"Jogo 3 Em Linha",JOptionPane.INFORMATION_MESSAGE);
    }
    
    //mostra as pecas no tabuleiro
    private void mostraPecas(){
        if (timeline != null) timeline.stop();
        timeline = new Timeline();
        for (final Peca piece : pecasPC) {
            timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                    new KeyValue(piece.translateXProperty(), piece.getX()),
                    new KeyValue(piece.translateYProperty(), 0)));
        }
        for (final Peca piece : pecas) {
            if(piece.getLetra()==Dados.VAZIA) piece.setInactive();
            else piece.setActive();
            timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                    new KeyValue(piece.translateXProperty(), piece.getX()),
                    new KeyValue(piece.translateYProperty(), piece.getY())));
        }
        timeline.playFromStart();
    }

    /**
     * cria a area do tabuleiro
     */
    public static class Desk extends Pane {
        Desk(int nColunas, int nLinhas) {
            setStyle("-fx-background-color: #cccccc; " +
                    "-fx-border-color: #464646; " +
                    "-fx-effect: innershadow( two-pass-box , rgba(0,0,0,0.8) , 15, 0.0 , 0 , 4 );");
            double DESK_WIDTH = Peca.SIZE * nColunas;
            double DESK_HEIGHT = Peca.SIZE * nLinhas;
            setPrefSize(DESK_WIDTH,DESK_HEIGHT);
            setMaxSize(DESK_WIDTH, DESK_HEIGHT);
            autosize();
            // cria o caminho para a linhs
            Path grid = new Path();
            grid.setStroke(Color.rgb(70, 70, 70));
            getChildren().add(grid);
            // cria as linhas verticais
             for (int col = 0; col < nColunas - 1; col++) {
                 grid.getElements().addAll(
                     new MoveTo(Peca.SIZE + Peca.SIZE * col, 5),
                     new LineTo(Peca.SIZE + Peca.SIZE * col, Peca.SIZE * nLinhas - 5)
                 );
            }
            // cria as linhas horizontais
            for (int row = 0; row < nLinhas - 1; row++) {
                 grid.getElements().addAll(
                     new MoveTo(5, Peca.SIZE + Peca.SIZE * row),
                     new LineTo(Peca.SIZE * nColunas - 5, Peca.SIZE + Peca.SIZE * row)
                 );
            }
        }
        @Override protected void layoutChildren() {}
    }

    public static class TopDesk extends Pane {
        TopDesk(int nColunas) {
            setStyle("-fx-background-color: #999999; " +
                    "-fx-border-color: #464646; " +
                    "-fx-effect: innershadow( two-pass-box , rgba(0,0,0,0.8) , 15, 0.0 , 0 , 4 );");
            double DESK_WIDTH = Peca.SIZE * nColunas;
            double DESK_HEIGHT = Peca.SIZE;
            setPrefSize(DESK_WIDTH,DESK_HEIGHT);
            setMaxSize(DESK_WIDTH, DESK_HEIGHT);
            autosize();
        }
        @Override protected void layoutChildren() {}
    }
    
    
    /**
     * Representa uma peça do tabuleiro
     */
    public class Peca extends Parent {
        private char letra;//tem o caracter da peça
        public static final int SIZE = 50;
        private Ponto ponto;
        
        private ImageView imageView = new ImageView();

        public Peca(char letra, Ponto ponto) {
            this.ponto=ponto;
            this.letra=letra;
            
            // cria a imagem na peça
            imageView.setImage(setImage());
            
            setFocusTraversable(true);
            getChildren().addAll(imageView);
            // turn on caching so the jigsaw piece is fasr to draw when dragging
            //setCache(true);
            // coloca a peca inactiva
            setInactive();
            // add listeners to support dragging
            setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent me) {
                    if(origem==null && getLetra()!=Dados.VAZIA){ 
                        origem=getPonto();
                    }else if(origem!=null){
                        destino=getPonto();
                        trocarButton.setDisable(false);
                    }
                    toFront();
                    // coloca a peca inactiva
                    setInactive();
                }
            });
        }

        public void setLetra(char letra) {
            this.letra = letra;
            setImage();
        }

        public void setPonto(Ponto ponto) {
            this.ponto = ponto;
        }

        public char getLetra() {
            return letra;
        }

        private Image setImage(){
            if(letra=='a') return new Image(getClass().getResourceAsStream("amarela.png"));
            else if(letra=='à') return new Image(getClass().getResourceAsStream("amarela_exp.png"));
            else if(letra=='á') return new Image(getClass().getResourceAsStream("amarela_rai.png"));
            else if(letra=='â') return new Image(getClass().getResourceAsStream("amarela_cor.png"));
            else if(letra=='u') return new Image(getClass().getResourceAsStream("azul.png"));
            else if(letra=='ù') return new Image(getClass().getResourceAsStream("azul_exp.png"));
            else if(letra=='ú') return new Image(getClass().getResourceAsStream("azul_rai.png"));
            else if(letra=='û') return new Image(getClass().getResourceAsStream("azul_cor.png"));
            else if(letra=='e') return new Image(getClass().getResourceAsStream("vermelha.png"));
            else if(letra=='è') return new Image(getClass().getResourceAsStream("vermelha_exp.png"));
            else if(letra=='é') return new Image(getClass().getResourceAsStream("vermelha_rai.png"));
            else if(letra=='ê') return new Image(getClass().getResourceAsStream("vermelha_cor.png"));
            else return new Image(getClass().getResourceAsStream("vazia.png"));
        }
        
        public void setActive() {
            //setDisable(false);
            setEffect(new DropShadow());
            toFront();
        }

        public void setInactive() {
            setEffect(null);
            //setDisable(true);
            toBack();
        }

        public double getY() { return (Dados.LINHAS-1-ponto.getX()) * SIZE; }
        public double getX() { return ponto.getY() * SIZE; }
        public Ponto getPonto(){ return ponto; }
    }

    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }
    public static void main(String[] args) { 
        launch(args); 
    }
}
