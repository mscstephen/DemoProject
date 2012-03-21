package pure_thermal;

import java.util.*;

public class Thermal {

    public static void HotspotDisplay(DataCenter d) {
        ArrayList<String> hotspots = d.getHotspots();
        for (String a : hotspots) {
            System.out.println(a);
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("enter cooling plan for rack A(0-3): ");
        int a = scan.nextInt();
        System.out.print("enter cooling plan for rack B(0-3): ");
        int b = scan.nextInt();
        System.out.print("enter cooling plan for rack C(0-3): ");
        int c = scan.nextInt();
        System.out.print("enter cooling plan for rack D(0-3): ");
        int d = scan.nextInt();
        int[] coolingarray = {a, b, c, d};
        int[] futurecooling = {0, 0, 0, 0};
        DataCenter data = new DataCenter();
        // data.showTempArray();
        // HotspotDisplay(data);
        // data.showTempArray();
        // HotspotDisplay(data);
        // this.runArray(data, coolingarray, 1);
        //   data.showTempArray();
        for (int i = 0; i < 30; i++) {
            data.updateArray(coolingarray);
        }

        HotspotDisplay(data);
//    prediction(data, 20000);
        //   HotspotDisplay(data);
        data.showTempArray();
        //  data.updateArray();
        //  data.showTempArray();
        //   prediction(data, 10);
        //  prediction(data, 10);
    }
}

