package com.finports.took_pos_demo.Model;

public class NotificationModel {
    public String to;
    public Notification notification = new Notification();
    public Data data = new Data();

    public static class Notification{
        public String title;
        public String text ;
    }
    public static class Data{
        public int price;
        public String payment_code;
        public String fcm_id;
    }
}
