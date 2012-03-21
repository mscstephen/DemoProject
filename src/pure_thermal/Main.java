//package thermalproject.pure_thermal;
//
//import thermalproject.scheduling_thermal.ScheduleReader;
//import java.util.*;
//import java.io.*;
//
//public class Main {
//
//    public static void main(String[] args) {
//        DataCenter data = new DataCenter();
//        data.showTempArray();
//        System.out.println(data.getThermalCPU("b", 3).getTemp());
////STEP1: GET THE COOLING PLAN AND INTENSITY PLAN.generated_cooling_plan SHOULD BE THE FIRST COOLING PLAN.
//        try {
//
//            int[] array = new int[4];
//            int[] intensityScheduling = new int[40];
//            array = setCoolingArrayForTest();
//            intensityScheduling = setScheduleArrayForTest();
//
//            data.setCoolingPlan(array);
//            //data.showCooling();
//            data.setIntensityPlan(intensityScheduling);
//
////set received intensity plan to current intensity plan.
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        String outputFilename = "thermalmap.txt";
//        String futureOutputname = "futurethermal.txt";
//        try {
//            //will be writing to the database.
//            //IMPORTANT: will need to write temporary entries under 4 headings based on one of UP TO 4 schedules per system epoch.
//            //based on trigger from optimization, commit to one of these four thermal maps. then and only then should the count be incremented.
//            FileWriter writer = new FileWriter(outputFilename);
//            FileWriter futurewriter = new FileWriter(futureOutputname);
//            PrintWriter pw = new PrintWriter(writer);
//            PrintWriter pwf = new PrintWriter(futurewriter);
//            DataCenter future = new DataCenter();
//            future.setCoolingPlan(data.getCoolingPlan());
////PLACEHOLDER. should update array with futureCooling, not yet getting to it.
//            data.setCoolingPlan(data.getCoolingPlan());
////runArray should only be run when we know we will be committing to the actual temperature changes. otherwise, predict.
//            System.out.println(data.getThermalCPU("b", 3).getTemp());
//            data.runArray(1);
//            data.showTempArray();
//            System.out.println(data.getThermalCPU("b", 3).getTemp());
////"1" is the number of epochs the simulator runs for.
//            //  data.showIntensityPlan();
//            double[][] f = new double[4][10];
//            f = data.getTempArray();
//            for (ThermalCPU a : data.getList()) {
//                pw.println(a.getTemp() - 273.15);
//            }
//            pw.println(data.getAirTemp() - 273.15);
//            writer.close();
//            //     System.out.println(data.getAirTemp());
//            data.resetAirTemp();
//            //     System.out.println(data.getAirTemp());
//            ArrayList<String> list;
//            list = data.getHotspots();
//            System.out.println("baseline" + data.getThermalCPU("b", 3).getTemp());
//            //future = new DataCenter();
//            System.out.println("f uture object has been set" + data.getThermalCPU("b", 3).getTemp());
//            try {
//            future.runArray(30);
//
//            System.out.println("predict");
//            System.out.println(data.getThermalCPU("b", 3).getTemp());
//            f = future.getTempArray();
//            for (ThermalCPU a : future.getList()) {
//                pwf.println(a.getTemp() - 273.15);
//            }
//
//            System.out.println("future.getTempArray()");
//
//                System.out.println(data.getThermalCPU("b", 3).getTemp());
//            } catch (NullPointerException e) {
//                //System.out.println("Object is "+future. +" \n Exception: " + e.toString() + "\n" + e.getMessage());
//            }
//            futurewriter.close();
//
//        } catch (Exception i) {
//            System.out.println(i);
//
//        }
//        // data.showTempArray();
//        //  System.out.println(data.getThermalCPU("b", 3).getTemp());
//
//    }
//
//    private static int[] setScheduleArrayForTest() throws IOException, NumberFormatException, FileNotFoundException {
//        int[] intensityScheduling = new int[40];
//        //This code requires input from the scheduling team.
//        String inputSchedule = "schedule.txt";
//        FileInputStream sstream = new FileInputStream(inputSchedule);
//        DataInputStream sin = new DataInputStream(sstream);
//        BufferedReader sr = new BufferedReader(new InputStreamReader(sin));
//        String sLine;
//        int j = 0;
//        while ((sLine = sr.readLine()) != null) {
//            int jobs = Integer.parseInt(sLine);
//            //assume all jobs take 10% CPU processing power for the 10 secs of an epoch.
//            intensityScheduling[j] = jobs * 10;
//            j++;
//        }
//        return intensityScheduling;
//    }
//
//    private static int[] setCoolingArrayForTest() throws IOException, NumberFormatException, FileNotFoundException {
//        //will be replaced with drawing from database
//        //ScheduleReader is to generate a new schedule. Testing purposes only.
//        int[] array = new int[4];
//        ScheduleReader reader = new ScheduleReader();
//        reader.read();
//        String inputFilename = "generated_cooling_plan.txt";
//        FileInputStream fstream = new FileInputStream(inputFilename);
//        DataInputStream in = new DataInputStream(fstream);
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        String strLine;
//        int i = 0;
//        while ((strLine = br.readLine()) != null) {
//            int parsedCooling = Integer.parseInt(strLine);
//            array[i] = parsedCooling;
//            i++;
//        }
//        return array;
//    }
//}
