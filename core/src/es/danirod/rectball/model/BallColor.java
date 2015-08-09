package es.danirod.rectball.model;

public enum BallColor {

    BLUE("blue.png"),

    RED("red.png"),

    GREEN("green.png"),

    YELLOW("yellow.png");

    private String path;

    BallColor(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

};
