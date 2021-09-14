package de.paesserver.iotaApi.message.payload;

public class Payload {
    public int instance;
    public String data;
    public Conflict conflict;

    //Conflicts
    public int conflictsLen;

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Payload {\n")
                        .append("type: 1\n")
                        .append("instance: ").append(instance).append("\n")
                        .append("data: ").append(data);
        stringBuilder.append("}\n");

        return stringBuilder.toString();
    }
}
