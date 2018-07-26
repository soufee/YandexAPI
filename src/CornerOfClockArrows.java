public class CornerOfClockArrows {

    public static double getCorner(double hours, double mins) {
        if (hours < 0 || hours > 24) throw new IllegalStateException("Введено неверное значение");
        if (mins < 0 || mins > 60) throw new IllegalStateException("Введено неверное значение");
        hours = 360 / 12 * (hours >= 12 ? hours - 12 : hours);
        mins = 360 / 12 * (mins / 5);
        double res = Math.abs(mins - hours);
        return res>180?res-180:res;
    }

    public static void main(String[] args) {

        System.out.println(getCorner(15, 19));
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
