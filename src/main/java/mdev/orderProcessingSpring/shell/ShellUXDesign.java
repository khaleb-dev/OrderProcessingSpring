package mdev.orderProcessingSpring.shell;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@SuppressWarnings("unused")
public enum ShellUXDesign {

    BLACK(0),
    RED(1),
    GREEN(2),
    YELLOW(3),
    BLUE(4),
    PURPLE(5),
    CYAN(6),
    WHITE(7),
    BRIGHT(8);

    private final int value;

    ShellUXDesign(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
