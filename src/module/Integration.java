/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module;

import h.entities.Costplan;
import h.entities.Cpu;
import h.entities.Mode;
import h.entities.Row;
import h.entities.Teams;
import h.entities.Thermalmap;
import h.entities.Totalpower;
import H.utility.NewHibernateUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author sjr1
 */
public class Integration {

    public static void setTableRows(String tableName, List rows) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.getTransaction();
        trans.begin();
        for (Object o : rows) {
            session.merge(o);
        }
        trans.commit();
    }

    public static void setScheduleRows(List<h.entities.Pschedule> pscheduleRows) {
        List<h.entities.Schedule> scheduleRows = new LinkedList<h.entities.Schedule>();
        for (h.entities.Pschedule pschedule : pscheduleRows) {
            h.entities.Schedule schedule = new h.entities.Schedule(pschedule.getCpu(),
                    pschedule.getTime(), pschedule.getTotalRevenue(), pschedule.getPlants());
            schedule.setScid(pschedule.getPscid());
            scheduleRows.add(schedule);
        }
        setTableRows("Schedule", scheduleRows);
    }

    public static final int ACTIVE = 0;
    public static final int INACTIVE = 1;
    public static final int WAITING = 2;

    public static void setOptimisationActiveNumber(Integer activeNumber) {
        Teams teamsRow = getTeamsRow(OPTIMISATION_TEAM_ID);
        teamsRow.setActive(activeNumber);
        setTableRows("Teams", Collections.singletonList(teamsRow));
    }

    // plan must be an array of length 4
    public static void setCoolingPlan(CoolingSetting[] plan) {
        List<Row> cpuRows = getTableRows("Row");
        assert cpuRows.size() == plan.length;
        for (int i = 0; i < plan.length; i++) {
            Mode mode = (Mode) getTableRows("Mode", "Mode.modeId = " + plan[i].getCoolingCode()).get(0);
            cpuRows.get(i).setMode(mode);
        }
        setTableRows("Row", cpuRows);
    }

    public static List getTableRows(String tableName) {
        return getTableRows(tableName, null);
    }

    public static List getTableRows(String tableName, String whereClause) {
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.getTransaction();
        trans.begin();
        Query q = session.createQuery("From " + tableName + " as " + tableName + (whereClause == null ? "" : " where " + whereClause));
        List rows = q.list();
        session.close();
        return rows;
    }
    private static int OPTIMISATION_TEAM_ID = 1;

    public static Teams getTeamsRow(int teamId) {
        return (Teams) getTableRows("Teams", "Teams.teamId = " + teamId).get(0);
    }

    public static Integer getOptimisationActiveNumber() {
        return getTeamsRow(OPTIMISATION_TEAM_ID).getActive();
    }

    public static List<h.entities.Pschedule> getPscheduleRows() {
        return getTableRows("Pschedule");
    }

    public static List<h.entities.Schedule> getScheduleRows() {
        return getTableRows("Schedule");
    }

    public static Costplan getCostPlan() {
        return (Costplan) getTableRows("Costplan").get(0);
    }

    public static Totalpower getTotalPower() {
        return (Totalpower) getTableRows("Totalpower").get(0);
    }

    public static List<h.entities.Job> getJobRows(int cpuId) {
        return getTableRows("Job", "Job.cpuid = " + cpuId);
    }

    public static float[][] getTemperatures() {
        // Create ordered map, mapping row numbers to temperature lists
        Map<Integer, List<BigDecimal>> cpuRows = new TreeMap<Integer, List<BigDecimal>>();
        // Every row in the Thermapmap table represents a cpu temperature
        // Add each temperature to the temperature list for the correct temperature row
        for (Thermalmap thermalMapRow : (List<Thermalmap>) getTableRows("Thermalmap")) {
            Integer cpuRowId = thermalMapRow.getRow().getRowId();
            if (!cpuRows.containsKey(cpuRowId)) {
                cpuRows.put(cpuRowId, new ArrayList<BigDecimal>());
            }
            cpuRows.get(cpuRowId).add(thermalMapRow.getCurrentTemp());
        }
        float[][] temperatures = new float[cpuRows.size()][];
        int currentRowIndex = 0;
        for (List<BigDecimal> cpuRow : cpuRows.values()) {
            temperatures[currentRowIndex] = new float[cpuRow.size()];
            for (int i = 0; i < cpuRow.size(); i++) {
                temperatures[currentRowIndex][i] = cpuRow.get(i).floatValue();
            }
            currentRowIndex++;
        }
        return temperatures;
    }

    public static enum CoolingSetting {

        COOLING_OFF(1), COOLING_LOW(2), COOLING_MEDIUM(3), COOLING_HIGH(4);
        private int code;

        CoolingSetting(int code) {
            this.code = code;
        }

        int getCoolingCode() {
            return code;
        }
    }
    private static final Map<Integer, CoolingSetting> coolingSettings;

    static {
        Map<Integer, CoolingSetting> mutableCoolingSettingsMap =
                new HashMap<Integer, CoolingSetting>();
        for (CoolingSetting setting : CoolingSetting.values()) {
            mutableCoolingSettingsMap.put(setting.getCoolingCode(), setting);
        }
        coolingSettings = Collections.unmodifiableMap(mutableCoolingSettingsMap);
    }

    public static CoolingSetting[] getCoolingPlan() {
        CoolingSetting[] plan = new CoolingSetting[4];
        List<Row> cpuRows = getTableRows("Row");
        int i = 0;
        for (Object o : cpuRows) {
            Row cpuRow = (Row) o;
            plan[i] = coolingSettings.get(cpuRow.getMode().getModeId());
            i++;
        }
        return plan;
    }

    public static void main(String... args) {

        CoolingSetting[] coolingPlan = getCoolingPlan();
        List<Integer> coolingPlanList = new LinkedList<Integer>();
        for (int i = 0; i < coolingPlan.length; i++) {
            coolingPlanList.add(coolingPlan[i].getCoolingCode());
        }
        System.out.println("Cooling Plan: " + coolingPlanList);
        System.out.println("Cooling Plan: " + java.util.Arrays.asList(getCoolingPlan()));

        Teams[] teams = new Teams[4];
        for (int i = 0; i < teams.length; i++) {
            teams[i] = getTeamsRow(i + 1);
        }
        System.out.println("\nTEAM 1 to " + teams.length);
        System.out.printf("%6s   %-15s%8s\n", "TeamId", "Name", "Active");
        for (int i = 0; i < teams.length; i++) {
            System.out.printf("%6s   %-15s%8s\n", teams[i].getTeamId(),
                    teams[i].getName(), teams[i].getActive());
        }

        List<Cpu> cpuRows = getTableRows("Cpu");
        System.out.println("\nJOB");
        System.out.printf("%3s%8s%10s%10s%10s%10s\n",
                "Jid", "Cpuid", "Revenue", "Duration", "Time", "Priority");
        for (Cpu cpu : cpuRows) {
            for (h.entities.Job job : getJobRows(cpu.getCpuid())) {
                System.out.printf("%3s%8s%10s%10s%10s%10s\n",
                        job.getJid(), job.getCpuid(), job.getRevenue(),
                        job.getDuration(), job.getTime(), job.getPriority());
            }
        }

        List<h.entities.Pschedule> pscheduleRows = getPscheduleRows();
        for (h.entities.Pschedule pscheduleRow : pscheduleRows) {
            pscheduleRow.setTotalRevenue(new BigDecimal("10.000"));
        }

        setScheduleRows(pscheduleRows);
        System.out.println("\nPSCHEDULE");
        System.out.printf("%5s%10s%15s%10s\n",
                "Scid", "Time", "Total Revenue", "CPU ID");
        for (h.entities.Pschedule scheduleRow : pscheduleRows) {
            System.out.printf("%5s%10s%15s%10s\n",
                    scheduleRow.getPscid(), scheduleRow.getTime(),
                    scheduleRow.getTotalRevenue(), scheduleRow.getCpu().getCpuid());
        }

        List<h.entities.Schedule> scheduleRows = getScheduleRows();
        System.out.println("\nSCHEDULE");
        System.out.printf("%5s%10s%15s%10s\n",
                "Scid", "Time", "Total Revenue", "CPU ID");
        for (h.entities.Schedule scheduleRow : scheduleRows) {
            System.out.printf("%5s%10s%15s%10s\n",
                    scheduleRow.getScid(), scheduleRow.getTime(),
                    scheduleRow.getTotalRevenue(), scheduleRow.getCpu().getCpuid());
        }

        Costplan costPlan = getCostPlan();
        System.out.println("\nCOST PLAN");
        System.out.printf("%6s%15s%15s%15s\n",
                "CostId", "Job Revenue", "Damage Cost", "Tpower Cost");
        System.out.printf("%6s%15s%15s%15s\n",
                costPlan.getCostId(), costPlan.getJobRevenue(),
                costPlan.getDamageCost(), costPlan.getTpowerCost());

        Totalpower totalPower = getTotalPower();
        System.out.println("\nTOTAL POWER");
        System.out.printf("%4s%11s%15s\n", "Tpid", "Pue", "totalPower");
        System.out.printf("%4s%11s%15s\n",
                totalPower.getTpid(), totalPower.getPue(),
                totalPower.getTotalPower());

        System.out.println("\nTEMPERATURE");
        float[][] temperatures = getTemperatures();
        for (int i = 0; i < temperatures.length; i++) {
            StringBuilder rowStringBuilder = new StringBuilder();
            rowStringBuilder.append("[");
            for (int j = 0; j < temperatures[i].length; j++) {
                if (j > 0) {
                    rowStringBuilder.append(", ");
                }
                rowStringBuilder.append(temperatures[i][j]);
            }
            rowStringBuilder.append("]");
            System.out.println(rowStringBuilder);
        }
    }
}
