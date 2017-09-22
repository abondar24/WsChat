package org.abondar.experimental.gameofthree.server;

public class Move {

    private String playerFrom;
    private String playerTo;
    private Integer addedNumber;
    private Integer resultingNumber;

    public Move(){}

    public Move(String playerFrom, String playerTo, Integer addedNumber, Integer resultingNumber) {
        this.playerFrom = playerFrom;
        this.playerTo = playerTo;
        this.addedNumber = addedNumber;
        this.resultingNumber = resultingNumber;
    }

    public String getPlayerFrom() {
        return playerFrom;
    }

    public void setPlayerFrom(String playerFrom) {
        this.playerFrom = playerFrom;
    }

    public String getPlayerTo() {
        return playerTo;
    }

    public void setPlayerTo(String playerTo) {
        this.playerTo = playerTo;
    }

    public Integer getAddedNumber() {
        return addedNumber;
    }

    public void setAddedNumber(Integer addedNumber) {
        this.addedNumber = addedNumber;
    }

    public Integer getResultingNumber() {
        return resultingNumber;
    }

    public void setResultingNumber(Integer resultingNumber) {
        this.resultingNumber = resultingNumber;
    }

    @Override
    public String toString() {
        return "Move{" +
                "playerFrom='" + playerFrom + '\'' +
                ", playerTo='" + playerTo + '\'' +
                ", addedNumber=" + addedNumber +
                ", resultingNumber=" + resultingNumber +
                '}';
    }
}
