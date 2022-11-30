package com.example.apitest;

import com.google.gson.*;

import static functions.functions.*;
import javafx.fxml.FXML;
import java.net.http.*;
import java.net.URI;
import java.util.*;

public class HelloController {

    public static String var;
    public static void sendToVar(String in) { var = in; }

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
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://db.ygoprodeck.com/api/v7/cardinfo.php?&fname=" + searchParam))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(com.example.apitest.HelloController::sendToVar)
                .join();
        Gson cardHold = new Gson();
        return cardHold.fromJson(var, CardInfo.class);
    }
}