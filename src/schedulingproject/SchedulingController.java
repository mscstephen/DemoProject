/*
 * Change Log
 * =============================================================================
 * Time:	14th/Mar.
 * Changes:	Added the method call of Translator since it added a new method
 *                  to change the active status once the job is finished or
 *                  dumped.
 *              Cleaned up the code little bit.
 *              Changed how the ScheduleController is called since Niall updated
 *                  it.
 *              Do action based on the action that got from the activator's
 *                  checking of optimisation team. Only run if action == 1 or
 *                  action == 2.
 *              When loading from the DB, check the action as well since it may
 *                  only need to load rule if it's a re-schedule.
 * =============================================================================
 * Time:	05th/Mar.
 * Changes:	Modified the data type of parsed rule pRule from int to double
 *                  since the parser modified it.
 * =============================================================================
 * Time:	29th/Feb.
 * Changes:	Added the functionality of writing schedule to the DB by using
 *                  Translator.
 * =============================================================================
 * Time:	28th/Feb.
 * Changes:	Added the functionality of loading resources from the DB by
 *                  using DataLoader.
 *              Added the functionality of setting the Scheduling Team active
 *                  status to the DB by using Activator.
 * =============================================================================
 * Time:	27th/Feb.
 * Changes:	Getters and Setters added.
 * =============================================================================
 */
package schedulingproject;

import java.util.ArrayList;

public class SchedulingController {

    // some variable declarations
    // declare the Parser, Scheduler and Translator
    private Activator activator;
    private ParserController parser;
    private ScheduleController scheduler;
    private Translator translator;
    // declare the fresh resources
    private ArrayList<Job> jobs;
    private Schedule prevSchedule;
    private boolean[][] healthMap;
    private double[][] thermalMap;
    private int rule;
    // declare the parsed resources
    private ArrayList<Job> pJobs;
    private ArrayList<Job> dumpJobs;
    private Schedule pPrevSchedule;
    private boolean[][] pHealthMap = null;
    private double[][] pThermalMap = null;
    private double pRule;
    // declare the non-translated schedule and the translated schedule
    private Schedule schedule;
    private boolean active = false;
    private int action = 1;
    // different finish conditions
    private boolean parseFinished = false;
    private boolean scheduleFinished = false;
    private boolean translateFinished = false;
    private boolean terminated = false;

    public SchedulingController(ArrayList<Job> jobs, Schedule prevSchedule, boolean[][] healthMap,
            double[][] thermalMap, int rule) {
        // create the Parser, InnerScheduler and Translator
        activator = new Activator();
        parser = new ParserController();
        scheduler = new ScheduleController(prevSchedule, healthMap, thermalMap, 0.00);
        translator = new Translator();

        this.healthMap = healthMap;
        this.thermalMap = thermalMap;
        this.jobs = jobs;
        this.prevSchedule = prevSchedule;
        this.rule = rule;

        this.pJobs = new ArrayList<Job>();
        this.pHealthMap = null;
        this.pThermalMap = null;
    }

    public SchedulingController() {
        // create the Parser, InnerScheduler and Translator
        activator = new Activator();
        parser = new ParserController();
        scheduler = new ScheduleController(prevSchedule, healthMap, thermalMap, 0.00);
        translator = new Translator();

        // default values
        this.healthMap = null;
        this.thermalMap = null;
        this.jobs = new ArrayList<Job>();
        this.prevSchedule = new Schedule();
        this.rule = 0;

        this.pJobs = new ArrayList<Job>();
        this.pHealthMap = null;
        this.pThermalMap = null;
    }

    // load required data from the database
    public void loadDataFromDB() {
        DataLoader dataLoader = new DataLoader();
        dataLoader.loadData(action);

        rule = dataLoader.getRule();
        if (action == 1) {
            healthMap = dataLoader.getHealthMap();
            thermalMap = dataLoader.getThermalMap();
            jobs = dataLoader.getJobs();
            prevSchedule = dataLoader.getSchedule();
        }
    }

    // call the parser to parse everything
    public void parse() {
        System.out.println("Scheduling Team - parsing START");
        // TODO shouldn't comment anything, but at the moment the DB data is not real, and if I do this, the jobs will be empty
        pJobs = parser.ParseJobList(jobs);
        pHealthMap = parser.parseHealthMap(healthMap);
        pThermalMap = parser.parseThermalMap(thermalMap);
        prevSchedule = parser.checkRepairSched(prevSchedule);
        pPrevSchedule = parser.provideOldJobs(prevSchedule);
        dumpJobs = parser.getDumpedJobs();
        pRule = parser.parseRule(rule);

        parseFinished = true;
        System.out.println("Scheduling Team - parsing END");
    }

    // call the scheduler to schedule
    public void schedule() {
        System.out.println("Scheduling Team - scheduling START");
        scheduler.setSchedule(pPrevSchedule); // TODO should really pass the parsed previous schedule
        scheduler.setHealthMap(pHealthMap);
        scheduler.setThermalMap(pThermalMap);
        scheduler.setRate(pRule);
        schedule = scheduler.assignJobs(pJobs);

        // TODO need to be removed. this is really hard coded revenu.
        schedule.setRevenue(100.00);

        scheduleFinished = true;
        System.out.println("Scheduling Team - scheduling END");
    }

    // call the translator to translate
    // or write the generated Schedule to the database
    public void translate() {
        System.out.println("Scheduling Team - translating START");
        translator.setSchedule(schedule);
        translator.setdumpJobs(dumpJobs);
        translator.translate();
        translator.ActiveChange();

        translateFinished = true;
        active = false;
        System.out.println("Scheduling Team - translating END");
    }

    // check the trigger for activation
    public void checkTrigger() {
//        // TODO need to check real triggers
//        long start = System.currentTimeMillis();
//        // this should run in a thread
//        while (true) {
//            long current = System.currentTimeMillis();
//            if (current - start >= 2000) {
//                // check activation
//                // if Optimization finished, set trigger on
//            }
//        }
        // TODO need somehow change the triggerOn every few seconds
        boolean triggerOn = true;
        if (triggerOn) {
            action = activator.checkAction();
            if (action == 1 || action == 2) {
                active = true;
            }
        }
    }

    public void run() {
//		while (true) {
        checkTrigger();
        if (active) {
            System.out.println("========================================");
            this.runIn10Secs();
            System.out.println("========================================");
        }
//		}
    }

    public void runNormal() throws InterruptedException {
        System.out.println("Scheduling Team - Started NORMALLY");
        // only do scheduling after optimisation finishes
        if (action != 0) {
            activator.setActive(true); // TODO need to uncomment

            loadDataFromDB(); // TODO need to uncomment
            this.parse(); // TODO need to uncomment
            this.schedule(); // TODO need to uncomment
            this.translate(); // TODO need to uncomment

//		Thread.sleep(5000); // test to see if the runIn10Secs will terminate with in 10 seconds
//		Thread.sleep(10000); // test to see if the runIn10Secs will terminate with in 10 seconds

//		if (parseFinished && scheduleFinished && translateFinished) {
//		if (parseFinished && translateFinished) {
//		if (scheduleFinished) {
//    }

            activator.setActive(false); // TODO need to uncomment
        }
        terminated = true;
        System.out.println("Scheduling Team - Terminated NORMALLY");
    }

    public void runIn10Secs() {
        // should finish everything with in 10 seconds.
        Thread thread = new Thread() {

            public void run() {
                SchedulingController controller = new SchedulingController(jobs, prevSchedule, healthMap, thermalMap, rule);
                try {
                    controller.runNormal();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (controller.terminated) {
                    pRule = controller.pRule;
                    pJobs = controller.pJobs;
                    dumpJobs = controller.dumpJobs;
                    pHealthMap = controller.pHealthMap;
                    pThermalMap = controller.pThermalMap;
                    prevSchedule = controller.prevSchedule;
                    pPrevSchedule = controller.pPrevSchedule;
                    schedule = controller.schedule;

                    terminated = controller.terminated;
                }
            }
        };
        thread.start();
        long start = System.currentTimeMillis();
        long end = start + (long) (8 * 1000); // leave few seconds to do something else (sending data, deal with database etc.)
        long counter = start;
        while (!terminated) {
            if (System.currentTimeMillis() - counter >= 1000) {
                counter = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() >= end) {
                terminated = true;
                System.out.println("Scheduling Team - Terminated because TIME EXCEEDED");
                thread.stop();
                // last try. commit the schedule even the terminated un-normally
                // TODO this might need to be removed
                this.translate();
            }
        }
    }

    // getters and setters for inputs
    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    public Schedule getPrevSchedule() {
        return prevSchedule;
    }

    public void setPrevSchedule(Schedule prevSchedule) {
        this.prevSchedule = prevSchedule;
    }

    public boolean[][] getHealthMap() {
        return healthMap;
    }

    public void setHealthMap(boolean[][] healthMap) {
        this.healthMap = healthMap;
    }

    public double[][] getThermalMap() {
        return thermalMap;
    }

    public void setThermalMap(double[][] thermalMap) {
        this.thermalMap = thermalMap;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    // getters for parsed resources
    public double getParsedRule() {
        return pRule;
    }

    public boolean[][] getParsedHealthMap() {
        return pHealthMap;
    }

    public double[][] getParsedThermalMap() {
        return pThermalMap;
    }

    public Schedule getParsedPrevSchedule() {
        return pPrevSchedule;
    }

    public ArrayList<Job> getParsedJobs() {
        return pJobs;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void printAllJobs() {
        System.out.println("Printing all jobs");
        if (jobs.size() > 0) {
            for (Job job : jobs) {
                System.out.println("\t" + job.toString());
            }
        } else {
            System.out.println("\t" + "No jobs");
        }
    }

    public void printAllParsedJobs() {
        System.out.println("Printing all parsed jobs");
        if (pJobs.size() > 0) {
            for (Job job : pJobs) {
                System.out.println("\t" + job.toString());
            }
        } else {
            System.out.println("\t" + "No parsed jobs");
        }
    }

    public void printAllDumpJobs() {
        System.out.println("Printing all dump jobs");
        if (dumpJobs.size() > 0) {
            for (Job job : dumpJobs) {
                System.out.println("\t" + job.toString());
            }
        } else {
            System.out.println("\t" + "No dump jobs");
        }
    }

    public void printRule() {
        System.out.println("Printing rule");
        System.out.println("\t" + rule);
    }

    public void printParsedRule() {
        System.out.println("Printing parsed rule");
        System.out.println("\t" + pRule);
    }

    public void printHealthMap() {
        System.out.println("Printing health map");
        if (healthMap != null) {
            for (int i = 0; i < healthMap.length; i++) {
                for (int j = 0; j < healthMap[0].length; j++) {
                    System.out.println("\t" + "healthMap[" + i + "][" + j + "]" + healthMap[i][j]);
                }
            }

        } else {
            System.out.println("\t Health map is null");
        }
    }

    public void printParsedHealthMap() {
        System.out.println("Printing parsed health map");
        if (pHealthMap != null) {
            for (int i = 0; i < pHealthMap.length; i++) {
                for (int j = 0; j < pHealthMap[0].length; j++) {
                    System.out.println("\t" + "pHealthMap[" + i + "][" + j + "]" + pHealthMap[i][j]);
                }
            }

        } else {
            System.out.println("\t Parsed health map is null");
        }
    }

    public void printThermalMap() {
        System.out.println("Printing thermal map");
        if (thermalMap != null) {
            for (int i = 0; i < thermalMap.length; i++) {
                for (int j = 0; j < thermalMap[0].length; j++) {
                    System.out.println("\t" + "thermalMap[" + i + "][" + j + "]" + thermalMap[i][j]);
                }
            }

        } else {
            System.out.println("\t Thermal map is null");
        }
    }

    public void printParsedThermalMap() {
        System.out.println("Printing parsed thermal map");
        if (pThermalMap != null) {
            for (int i = 0; i < pThermalMap.length; i++) {
                for (int j = 0; j < pThermalMap[0].length; j++) {
                    System.out.println("\t" + "pThermalMap[" + i + "][" + j + "]" + pThermalMap[i][j]);
                }
            }

        } else {
            System.out.println("\t Parsed thermal map is null");
        }
    }

    public void printPreviousSchedule() {
        System.out.println("Printing previous schedule");
        if (prevSchedule != null) {
            CPU[][] cpus = prevSchedule.getCPUS();
            for (int i = 0; i < cpus.length; i++) {
                for (int j = 0; j < cpus[0].length; j++) {
                    ArrayList<Job> tmpJobs = cpus[i][j].getJobs();
                    System.out.println("\t" + "cpus[" + i + "][" + j + "]");
                    if (tmpJobs.size() > 0) {
                        for (Job job : tmpJobs) {
                            System.out.println("\t\t" + job.toString());
                        }
                    } else {
                        System.out.println("\t\t" + "No jobs in cpu");
                    }
                }
            }

        } else {
            System.out.println("\t Previous schedule is null");
        }
    }

    public void printParsedPreviousSchedule() {
        System.out.println("Printing parsed previous schedule");
        if (pPrevSchedule != null) {
            CPU[][] cpus = pPrevSchedule.getCPUS();
            for (int i = 0; i < cpus.length; i++) {
                for (int j = 0; j < cpus[0].length; j++) {
                    ArrayList<Job> tmpJobs = cpus[i][j].getJobs();
                    System.out.println("\t" + "cpus[" + i + "][" + j + "]");
                    if (tmpJobs.size() > 0) {
                        for (Job job : tmpJobs) {
                            System.out.println("\t\t" + job.toString());
                        }
                    } else {
                        System.out.println("\t\t" + "No jobs in cpu");
                    }
                }
            }

        } else {
            System.out.println("\t Parsed previous schedule is null");
        }
    }

    public void printFinalSchedule() {
        System.out.println("Printing final schedule");
        if (schedule != null) {
            CPU[][] cpus = schedule.getCPUS();
            for (int i = 0; i < cpus.length; i++) {
                for (int j = 0; j < cpus[0].length; j++) {
                    ArrayList<Job> tmpJobs = cpus[i][j].getJobs();
                    System.out.println("\t" + "cpus[" + i + "][" + j + "]");
                    if (tmpJobs.size() > 0) {
                        for (Job job : tmpJobs) {
                            System.out.println("\t\t" + job.toString());
                        }
                    } else {
                        System.out.println("\t\t" + "No jobs in cpu");
                    }
                }
            }

        } else {
            System.out.println("\t Final schedule is null");
        }
    }
}
