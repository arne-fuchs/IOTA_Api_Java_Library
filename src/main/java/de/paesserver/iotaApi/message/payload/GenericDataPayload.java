package de.paesserver.iotaApi.message.payload;

public class GenericDataPayload extends  Payload{
    public String type = "GenericDataPayloadType(0)";
    public String blob;

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("GenericDataPayload { \n")
                .append("type: ").append(type).append("\n")
                .append("blob: ").append(blob).append("\n}\n");

        return stringBuilder.toString();
    }
}
