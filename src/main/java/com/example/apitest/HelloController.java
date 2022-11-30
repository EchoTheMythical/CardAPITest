package com.example.apitest;

import com.google.gson.*;

import static functions.functions.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.http.*;
import java.net.URI;
import java.util.*;

public class HelloController {
    @FXML public ImageView imageview;
    @FXML public TextField textfield;
    @FXML public Label cardInfo;
    @FXML public Button goBack;
    @FXML public Button goNext;

    public int crntCard = 0;
    public CardInfo crntSet;
    public static String var;
    public static void sendToVar(String in) { var = in; }

    public void previousCard() {
        if (crntCard > 0) crntCard--;
        Data dat = crntSet.data.get(crntCard);
        imageview.setImage(new Image(dat.card_images.get(0).image_url));
        setCardInfo(dat);
    }

    public void nextCard() {
        if (crntCard + 1 < crntSet.data.size()) crntCard++;
        Data dat = crntSet.data.get(crntCard);
        imageview.setImage(new Image(dat.card_images.get(0).image_url));
        setCardInfo(dat);
    }

    public void trackMouse(MouseEvent mouseEvent) {
        cardInfo.setLayoutX(mouseEvent.getSceneX() + 5);
        cardInfo.setLayoutY(mouseEvent.getSceneY() - cardInfo.getHeight() - 5);
    }

    public void setimage() throws Exception {
        crntCard = 0;
        crntSet = requestCard(textfield.getText());
        Data dat = crntSet.data.get(crntCard);
        imageview.setImage(new Image(dat.card_images.get(0).image_url));
        textfield.setText("");
        setCardInfo(dat);
    }

    public void setCardInfo(Data dat) {
        StringBuilder tempInfo = new StringBuilder("Card ID: " + dat.id + "\n" + dat.name + "\n");
        if (!(dat.type.equals("Spell Card") || dat.type.equals("Trap Card")))
            tempInfo.append("Level: ").append(dat.level).append(" ")
                    .append("   Link: ").append(dat.linkval).append("\n")
                    .append(dat.attribute).append("   ");
        tempInfo.append(dat.race).append("\n").append(dat.type).append("\n");
        if (dat.type.equals("Pendulum Effect Monster") || dat.type.equals("Pendulum Normal Monster"))
            tempInfo.append(dat.scale).append("\n");
        tempInfo.append(dat.desc).append("\n");
        if (!(dat.type.equals("Spell Card") || dat.type.equals("Trap Card")))
            tempInfo.append("ATK ").append(dat.atk)
                    .append("    DEF ").append(dat.def).append("\n");
        for (String s : dat.linkmarkers) tempInfo.append(s).append(" ");
        if (dat.type.equals("Link Monster")) tempInfo.append("\n");
        cardInfo.setText(tempInfo.toString());
    }

    public void hideCardInfoRC(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY && cardInfo.isVisible()) cardInfo.setVisible(false);
        else if (mouseEvent.getButton() == MouseButton.SECONDARY && !cardInfo.isVisible()) cardInfo.setVisible(true);
    }

    public void hideCardInfo() {
        if (!cardInfo.getText().equals("")) cardInfo.setVisible(false);
    }

    public void showCardInfo() {
        if (!cardInfo.getText().equals("")) cardInfo.setVisible(true);
    }

    public static class CardInfo {
        public ArrayList<Data> data = new ArrayList<>();
        public void printList() {
            for (Data dat : data) {
                print("\n" + dat.id + "\n" + dat.name + "\n");
                if (!(dat.type.equals("Spell Card") || dat.type.equals("Trap Card")))
                    print(dat.level + dat.linkval + "\n" + dat.attribute + "\n");
                print(dat.race + "\n" + dat.type + "\n");
                if (dat.type.equals("Pendulum Effect Monster") || dat.type.equals("Pendulum Normal Monster"))
                    print(dat.scale + "\n");
                print(dat.desc + "\n");
                if (!(dat.type.equals("Spell Card") || dat.type.equals("Trap Card")))
                    print(dat.atk + "\n" + dat.def + "\n");
                for (String s : dat.linkmarkers) print(s + " ");
                if (dat.type.equals("Link Monster")) print("\n");
                print(dat.card_images.get(0).image_url + "\n");
            }
        }
    }

    public static class Data {
        public int id, atk, def, level, scale, linkval;
        public String name, type, desc, race, attribute, archetype;
        public ArrayList<String> linkmarkers = new ArrayList<>();
        public ArrayList<CardSet> card_sets = new ArrayList<>();
        public ArrayList<CardImage> card_images = new ArrayList<>();
        public ArrayList<CardPrice> card_prices = new ArrayList<>();
    }

    public static class CardSet {
        public String
             set_name,
             set_code,
             set_price,
             set_rarity,
             set_rarity_code;
    }

    public static class CardImage {
        public int id;
        public String image_url,
                image_url_small;
    }

    public static class CardPrice {
        public String
            coolstuffinc_price,
            cardmarket_price,
            tcgplayer_price,
            amazon_price,
            ebay_price;
    }

    public static CardInfo requestCard(String searchParam) throws Exception {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < searchParam.length(); i++) {
            if (searchParam.charAt(i) == ' ') temp.append("%20");
            else temp.append(searchParam.charAt(i));
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://db.ygoprodeck.com/api/v7/cardinfo.php?&fname=" + temp))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(com.example.apitest.HelloController::sendToVar)
                .join();
        Gson cardHold = new Gson();
        return cardHold.fromJson(var, CardInfo.class);
    }
}