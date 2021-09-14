package de.paesserver.iotaApi.message;

import de.paesserver.iotaApi.message.payload.GenericDataPayload;
import de.paesserver.iotaApi.message.payload.Payload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Message {
    public String id;
    public ArrayList<String> strongParents;
    public String issuer;
    public String issuerTime;
    public long sequenceNumber;
    public Payload payload;
    public BigDecimal nonce;
    public String signature;

    public Message(){
        strongParents = new ArrayList<>();
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("Message {\n")
                .append("id: MessageID(").append(id).append(")\n");
        for(int i = 0;i < strongParents.size();i++){
            stringBuilder.append("strongParent").append(i).append(": \"MessageID(").append(strongParents.get(i)).append(")\"\n");
        }
        stringBuilder
                .append("issuer: ").append(issuer).append("\n")
                .append("issuingTime: ").append(issuerTime).append("\n")
                .append("sequenceNumber: ").append(sequenceNumber).append("\n")
                .append("payload: ").append(payload.toString())
                .append("nonce: ").append(nonce).append("\n")
                .append("signature: ").append(signature).append("\n")
                .append("}\n");

        return stringBuilder.toString();
    }

    public static Message parseToMessage(String message){
        StringReader stringReader = new StringReader(message);
        return parseToMessage(new BufferedReader(stringReader));
    }

    public static Message parseToMessage(BufferedReader bufferedReader) {
        try {
            //Beginning reading message

            //first 4 bytes are length defined bytes. Will be ignored
            bufferedReader.read(new char[4],0,4);

            String currentLine;
            //First line should contain the string "message"
            if((currentLine = bufferedReader.readLine()).contains("Message")){
                Message message = new Message();

                currentLine = bufferedReader.readLine();
                message.id = currentLine.substring(currentLine.indexOf("(")+1,currentLine.indexOf(")"));

                while((currentLine = bufferedReader.readLine()).contains("strongParent")){
                    message.strongParents.add(currentLine.substring(currentLine.indexOf("(")+1,currentLine.indexOf(")")));
                }

                message.issuer = currentLine.substring(currentLine.indexOf(":")+2);

                currentLine = bufferedReader.readLine();
                message.issuerTime = currentLine.substring(currentLine.indexOf(":")+2);

                currentLine = bufferedReader.readLine();
                message.sequenceNumber = Integer.parseInt(currentLine.substring(currentLine.indexOf(":")+2));

                currentLine = bufferedReader.readLine();
                if(currentLine.contains("payload: ")){
                    if(currentLine.substring(currentLine.indexOf(":")+2).startsWith("Payload")){
                        message.payload = new Payload();
                        currentLine = bufferedReader.readLine();

                        //check if conflict or not
                        if(currentLine.contains("conflictsLen")){
                            //TODO Conflict



                        }else{
                            currentLine = bufferedReader.readLine();
                            message.payload.instance = Integer.parseInt(currentLine.substring(currentLine.indexOf(":")+2));

                            currentLine = bufferedReader.readLine();
                            message.payload.data = currentLine.substring(currentLine.indexOf(":")+2);
                        }
                    }else{
                        //Case if GenericDataPayloadType
                        GenericDataPayload genericDataPayload = new GenericDataPayload();
                        while (!(currentLine = bufferedReader.readLine()).contains("blob")){}

                        genericDataPayload.blob = currentLine.substring(currentLine.indexOf(":")+2);

                        message.payload = genericDataPayload;
                    }
                }else{
                    System.out.println("Missing payload: " + currentLine);
                }


                //Skipping it to nonce
                while(!(currentLine = bufferedReader.readLine()).contains("nonce")){}

                message.nonce = new BigDecimal(currentLine.substring(currentLine.indexOf(":")+2));


                currentLine = bufferedReader.readLine();
                message.signature = currentLine.substring(currentLine.indexOf(":")+2);

                //needs to be returned
                return message;
            }else {
                System.out.println("Recieved unknown struct: " + currentLine);
            }
        }catch (IOException e){e.printStackTrace();}

        return null;
    }
}

