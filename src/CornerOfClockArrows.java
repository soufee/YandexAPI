public class CornerOfClockArrows {

    public static double getCorner(double hours, double mins) {
        if (hours < 0 || hours > 24) return -1;
        if (mins < 0 || mins > 60) return -1;
        hours = 360 / 12 * (hours >= 12 ? hours - 12 : hours);
        mins = 360 / 12 * (mins / 5);
        System.out.println("hours = " + hours + "; mins = " + mins);
        return Math.abs(mins - hours);
    }

    public static void main(String[] args) {
        System.out.println(test1());
        System.out.println(test2());
        System.out.println(test3());
    }

    private static double test1() {
        double result = getCorner(12, 30);
        if (result != 180) System.out.println("Не верно");
        else System.out.println("Верно");
        return result;
    }

    private static double test3() {
        double result = getCorner(21, 30);
        if (result != 90) System.out.println("Не верно");
        else System.out.println("Верно");
        return result;
    }

    private static double test2() {
        double result = getCorner(6, 15);
        if (result != 90) System.out.println("Не верно");
        else System.out.println("Верно");
        return result;
    }


}
