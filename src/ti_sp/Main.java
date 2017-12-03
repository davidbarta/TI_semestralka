package ti_sp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Hlavni trida programu
 * @author David Barta, Jan Rychlik
 * @version 1
 */
public class Main extends Application {

    /** Misto, kde se maji zobrazovat obrazky */
    private ImageView imageView;

    /** Napis tlacicka on_offAnimBtn pro zapnuti */
    private final String ON_STRING = "Turn on lights";

    /** Napis tlacitka on_offAnimBtn pro vypnuti */
    private final String OFF_STRING = "Pause light";

    /** Napis tlacitka on_offTrafficBtn pro zapnuti */
    private final String TRAFFIC_ON_STRING = "Turn on traffic";

    /** Napis tlacitka on_offTrafficBtn pro vypnuti */
    private final String TRAFFIC_OFF_STRING = "Turn off traffic";

    /** Pauza programu 2 vteriny */
    private final int WAITING_TIME_2 = 2000;

    /** Pauza programu 4 vteriny */
    private final int WAITING_TIME_4 = 4000;

    /** Pauza programu 10 vterin */
    private final int WAITING_TIME_10 = 10000;

    /** Obrazek pro stav K_VYP0 */
    private final Image IMG_K_VYP0 = new Image("img/K-VYP0.png");

    /** Obrazek pro stav K_VYP1 */
    private final Image IMG_K_VYP1 = new Image("img/K-VYP1.png");

    /** Obrazek pro stav K_S0 */
    private final Image IMG_K_S0 = new Image("img/K-S0.png");

    /** Obrazek pro stav K_S1PS */
    private final Image IMG_K_S1PS = new Image("img/K-S1PS.png");

    /** Obrazek pro stav K_S1 */
    private final Image IMG_K_S1 = new Image("img/K-S1.png");

    /** Obrazek pro stav K_S2P */
    private final Image IMG_K_S2P = new Image("img/K-S2P.png");

    /** Obrazek pro stav K_S2 */
    private final Image IMG_K_S2 = new Image("img/K-S2.png");

    /** Obrazek pro stav K_S3P */
    private final Image IMG_K_S3P = new Image("img/K-S3P.png");

    /** Obrazek pro stav K_S3 */
    private final Image IMG_K_S3 = new Image("img/K-S3.png");

    /** Obrazek pro stav K_S1P */
    private final Image IMG_K_S1P = new Image("img/K-S1P.png");

    /** Obrazek pro stav K_S2T */
    private final Image IMG_K_S2T = new Image("img/K-S2T.png");

    /** Obrazek pro stav K_S3T */
    private final Image IMG_K_S3T = new Image("img/K-S3T.png");

    /** Promenna s cekaci dobou 2 vteriny */
    private int time2 = WAITING_TIME_2;

    /** Promenna s cekaci dobou 4 vteriny */
    private int time4 = WAITING_TIME_4;

    /** Promenna s cekaci dobou 10 vterin */
    private int time10 = WAITING_TIME_10;

    /** Pocatecni stav */
    private State state = State.K_VYP0;

    /** Dalsi stav */
    private State nextState = State.K_VYP1;

    /** Tlacitko pro zapnuti nebo vypnuti (pozastaveni) animace */
    private Button on_offAnimBtn;

    /** Tlacitko pro zapnuti nebo vypnuti provozu */
    private Button on_offTrafficBtn;

    /** Casovac */
    private Timer timer;

    /** Boolean pro zjisteni, zda bylo stisknuto tlacitko pro chodce 1 */
    private Boolean pedestrian1 = false;

    /** Boolean pro zjisteni, zda bylo stisknuto tlacitko pro chodce 2 */
    private Boolean pedestrian2 = false;

    /** Boolean pro zjisteni, zda jede tramvaj ci nikoliv */
    private Boolean tram = true;

    /**
     * Hlavni metoda programu
     * @param args parametry prikazove radky (nevyuzity)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metoda vykreslujici platno
     * @param primaryStage platno k vykresleni
     */
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setWidth(800);
        primaryStage.setMaxWidth(800);
        primaryStage.setMinWidth(800);

        primaryStage.setHeight(685);
        primaryStage.setMaxHeight(685);
        primaryStage.setMinHeight(685);

        primaryStage.setTitle("SP-TI David Bárta, Jan Rychlík");

        primaryStage.setScene(new Scene(getRoot()));

        primaryStage.show();
    }

    /**
     * Vraci platnu komponenty k vykresleni
     * @return komponenty k vykresleni
     */
    private Parent getRoot() {
        BorderPane root = new BorderPane();

        root.getStylesheets().add("style/styles.css");
        root.setBottom(getButtons());
        root.setCenter(getImage());

        return root;
    }


    /**
     * Vraci komponentu obsahujici tlacitka pro ovladani programu
     * @return komponenta s tlacitky
     */
    private Node getButtons() {
        GridPane gridPane = new GridPane();
        Separator sep1 = new Separator();
        Separator sep2 = new Separator();

        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(15);

        on_offAnimBtn = new Button(this.ON_STRING);
        on_offAnimBtn.setOnAction(event -> onOffAnim());
        on_offAnimBtn.getStyleClass().add("green");
        gridPane.add(on_offAnimBtn, 0, 0);

        on_offTrafficBtn = new Button(this.TRAFFIC_ON_STRING);
        on_offTrafficBtn.setOnAction(event -> onOffTraffic());
        on_offTrafficBtn.getStyleClass().add("green");
        gridPane.add(on_offTrafficBtn, 1, 0);

        sep1.setOrientation(Orientation.VERTICAL);
        sep1.getStyleClass().add("separator");
        gridPane.add(sep1, 2, 0);

        Button speed1Btn = new Button("Speed 1x");
        speed1Btn.setOnAction(event -> speed1());
        speed1Btn.getStyleClass().add("green");
        gridPane.add(speed1Btn, 3, 0);

        Button speed2Btn = new Button("Speed 2x");
        speed2Btn.setOnAction(event -> speed2());
        speed2Btn.getStyleClass().add("green");
        gridPane.add(speed2Btn, 4, 0);

        Button speed3Btn = new Button("Speed 3x");
        speed3Btn.setOnAction(event -> speed3());
        speed3Btn.getStyleClass().add("green");
        gridPane.add(speed3Btn, 5, 0);

        sep2.setOrientation(Orientation.VERTICAL);
        sep2.getStyleClass().add("separator");
        gridPane.add(sep2, 6, 0);

        Button pedestrian1Btn = new Button("Pedestrian 1");
        pedestrian1Btn.setOnAction(event -> goPedestrian1());
        pedestrian1Btn.getStyleClass().add("green");
        gridPane.add(pedestrian1Btn, 7, 0);

        Button pedestrian2Btn = new Button("Pedestrian 2");
        pedestrian2Btn.setOnAction(event -> goPedestrian2());
        pedestrian2Btn.getStyleClass().add("green");
        gridPane.add(pedestrian2Btn, 8, 0);

        Button tramBtn = new Button("Tram");
        tramBtn.setOnAction(event -> goTram());
        tramBtn.getStyleClass().add("green");
        gridPane.add(tramBtn, 9, 0);

        gridPane.getStyleClass().add("grid");
        gridPane.setAlignment(Pos.CENTER);

        return gridPane;
    }

    /**
     * Vraci komponentu obsahujici obrazek k vykresleni
     * @return komponenta s obrazkem
     */
    private Node getImage() {

        this.imageView = new ImageView();

        this.imageView.setImage(IMG_K_VYP0);

        return this.imageView;
    }

    /**
     * Spusti nebo pozastavi animaci.
     * Po opetovnem spusteni animace program pokracuje z mista, ve kterem byl pozastaven.
     */
    private void onOffAnim() {
        if(on_offAnimBtn.getText().equalsIgnoreCase(ON_STRING)) {
            on_offAnimBtn.setText(OFF_STRING);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        nextImage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 10, 10);
        } else {
            on_offAnimBtn.setText(ON_STRING);
            timer.cancel();
        }
    }

    /**
     * Spusti nebo vypne semafory.
     * Ve vypnutem stavu na semaforech pro auta stridave blika oranzove svetlo.
     * V zapnutem stavu svetla normalne funguji a po vypnuti opet blika oranzove svetlo.
     */
    private void onOffTraffic() {
        if (on_offTrafficBtn.getText().equalsIgnoreCase(TRAFFIC_ON_STRING)) {
            nextState = State.K_S0;
            on_offTrafficBtn.setText(TRAFFIC_OFF_STRING);
        } else {
            on_offTrafficBtn.setText(TRAFFIC_ON_STRING);
            nextState = State.K_VYP0;
        }
    }

    /**
     * Prepne automat do dalsiho stavu
     * @throws InterruptedException vyhodi vyjimku pokud jecekaci doba v nejakem stavu externe prerusena
     */
    private void nextImage() throws InterruptedException{
        state = nextState;

        switch (state) {

            case K_VYP0: {
                imageView.setImage(IMG_K_VYP0);
                nextState = State.K_VYP1;
                testTram();
                Thread.sleep(time2);
                break;
            }
            case K_VYP1: {
                imageView.setImage(IMG_K_VYP1);
                nextState = State.K_VYP0;
                testTram();
                Thread.sleep(time2);
                break;
            }
            case K_S0: {
                imageView.setImage(IMG_K_S0);
                nextState = State.K_S1PS;
                testTram();
                if(pedestrian1 || pedestrian2) {
                    Thread.sleep(time4);
                } else {
                    Thread.sleep(time10);
                }
                break;
            }
            case K_S1PS: {
                imageView.setImage(IMG_K_S1PS);
                nextState = State.K_S1;
                testTram();
                Thread.sleep(time2);
                break;
            }
            case K_S1: {
                imageView.setImage(IMG_K_S1);
                nextState = State.K_S2P;
                testTram();
                if(pedestrian1 || pedestrian2) {
                    Thread.sleep(time4);
                } else {
                    Thread.sleep(time10);
                }
                break;
            }
            case K_S2P: {
                imageView.setImage(IMG_K_S2P);
                nextState = State.K_S2;
                testTram();
                Thread.sleep(time2);
                break;
            }
            case K_S2: {
                imageView.setImage(IMG_K_S2);
                nextState = State.K_S3P;
                testTram();
                if(pedestrian2) {
                    pedestrian2 = false;
                    Thread.sleep(time10);
                    break;
                } else if (pedestrian1) {
                    Thread.sleep(time4);
                } else {
                    Thread.sleep(time10);
                }
                break;
            }
            case K_S3P: {
                pedestrian2 = false;
                imageView.setImage(IMG_K_S3P);
                nextState = State.K_S3;
                testTram();
                Thread.sleep(time2);
                break;
            }
            case K_S3: {
                imageView.setImage(IMG_K_S3);
                nextState = State.K_S1P;
                testTram();
                pedestrian1 = false;
                pedestrian2 = false;
                Thread.sleep(time10);
                break;
            }
            case K_S1P: {
                pedestrian1 = false;
                pedestrian2 = false;
                imageView.setImage(IMG_K_S1P);
                nextState = State.K_S1;
                testTram();
                Thread.sleep(time2);
                break;
            }
            case K_S2T: {
                imageView.setImage(IMG_K_S2T);
                nextState = State.K_S1;
                testTram();
                Thread.sleep(time2);
            }
            case K_S3T: {
                imageView.setImage(IMG_K_S3T);
                nextState = State.K_S1;
                testTram();
                Thread.sleep(time2);
            }
            default: {
                break;
            }
        }
    }

    /**
     * Prepne automat do dalsiho stavu tak, aby co nejdrive pustil tramvaj
     */
    private void tramPass() {
        switch (state) {

            case K_VYP0: {
                break;
            }
            case K_VYP1: {
                break;
            }
            case K_S0: {
                imageView.setImage(IMG_K_S0);
                nextState = State.K_S1PS;
                break;
            }
            case K_S1PS: {
                imageView.setImage(IMG_K_S1PS);
                nextState = State.K_S1;
                break;
            }
            case K_S1: {
                tram = false;
                imageView.setImage(IMG_K_S1);
                nextState = State.K_S2P;
                break;
            }
            case K_S2P: {
                imageView.setImage(IMG_K_S2P);
                nextState = State.K_S2;
                break;
            }
            case K_S2: {
                imageView.setImage(IMG_K_S2);
                nextState = State.K_S2T;
                break;
            }
            case K_S2T: {
                imageView.setImage(IMG_K_S2T);
                nextState = State.K_S1;
                break;
            }
            case K_S3P: {
                imageView.setImage(IMG_K_S3P);
                nextState = State.K_S3;
                break;
            }
            case K_S3: {
                imageView.setImage(IMG_K_S3);
                nextState = State.K_S3T;
                break;
            }
            case K_S3T: {
                imageView.setImage(IMG_K_S3T);
                nextState = State.K_S1;
                break;
            }
            case K_S1P: {
                imageView.setImage(IMG_K_S1P);
                nextState = State.K_S1;
                break;
            }
        }
    }

    /**
     * Testuje, zda bylo zmacknuto tlacitko tramvaje,
     * pokud ano, zavola metodu pro prijezd tramvaje
     */
    private void testTram() {
        if(tram) {
            tramPass();
        }
    }

    /**
     * Nastavi dobu cekani odpovidajici zakladni rychlosti
     */
    private void speed1() {
        time2 = WAITING_TIME_2;
        time4 = WAITING_TIME_4;
        time10 = WAITING_TIME_10;
    }

    /**
     * Nastavi dobu cekani na dvojnasobek zakladni rychlosti
     */
    private void speed2() {
        time2 = WAITING_TIME_2 / 2;
        time4 = WAITING_TIME_4 / 2;
        time10 = WAITING_TIME_10 / 2;
    }

    /**
     * Nastavi dobu cekani na trojnasobek zakladni rychlosti
     */
    private void speed3() {
        time2 = WAITING_TIME_2 / 3;
        time4 = WAITING_TIME_4 / 3;
        time10 = WAITING_TIME_10 / 3;
    }

    /**
     * Nastavi hodnotu Booleanu pedestrian1 na "true" po zmacknuti tlacitka
     */
    private void goPedestrian1() {
        this.pedestrian1 = true;
    }

    /**
     * Nastavi hodnotu Booleanu pedestrian2 na "true" po zmacknuti tlacitka
     */
    private void goPedestrian2() {
        this.pedestrian2 = true;
    }

    /**
     * Nastavi hodnotu Booleanu tram na "true" po zmacknuti tlacitka
     */
    private void goTram() {
        this.tram = true;
    }

    /**
     * Po kliknuti na krizek v okne programu ukonci program
     */
    @Override
    public void stop() {
        Platform.exit();
        System.exit(1);
    }
}