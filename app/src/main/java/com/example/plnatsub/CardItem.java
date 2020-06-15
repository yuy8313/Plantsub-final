package com.example.plnatsub;


import android.graphics.Bitmap;

public class CardItem {

    private String contents;
    private Bitmap img_flower;
    public CardItem(Bitmap img_flower, String contents) {
        this.img_flower = img_flower;
        this.contents = contents;
    }

    public Bitmap getImg_flower(){
        return img_flower;
    }
    public void setImg_flower(Bitmap img_flower){
        this.img_flower = img_flower;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CardItem{");
        sb.append("contents='").append(contents).append('\'');
        sb.append(", image'").append(img_flower).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
