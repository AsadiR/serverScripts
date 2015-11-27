
public class TestFloatDouble {
    public static void main(String[] args) throws Exception {
        double ad = 4.000001d;
        float af = 4.000001f;
        double b = 4.000000d;
        System.out.println((ad-b) +" "+ (af-b));
    }
}
