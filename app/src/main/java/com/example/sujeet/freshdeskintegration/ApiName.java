package com.example.sujeet.freshdeskintegration;

/**
 * Created by Sujeet on 03-11-2017.
 */

public class ApiName {
    public static final String token="Q6vCU9ED1WdQ7zzrxp2" + ":" + "X";
    public static final String endPoint="https://z2p.freshdesk.com";
    public static final String createTicket="/api/v2/tickets";
    public static final String allContact="/api/v2/contacts";
    public static final String contantTypeJson="";
    public static final String replyTicket(int ticketId){
        return "/api/v2/tickets/"+ticketId+"/reply";
    }
    public static final String ticketConversation(String ticketId){
        return "/api/v2/tickets/"+ticketId+"/conversations";
    }
}
