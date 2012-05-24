package clear4j;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:44
 */
public final class Clear {
    private Clear(){}

    public static <T extends Processor> T instruct(T processor) {
        return processor;
    }


}
