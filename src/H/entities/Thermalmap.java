package h.entities;
// Generated 29-Feb-2012 13:51:12 by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Thermalmap generated by hbm2java
 */
public class Thermalmap  implements java.io.Serializable {


     private Integer tmapId;
     private Row row;
     private Cpu cpu;
     private BigDecimal currentTemp;
     private BigDecimal futureTemp;
     private Date time;
     private Set plants = new HashSet(0);

    public Thermalmap() {
    }

    public Thermalmap(Row row, Cpu cpu, BigDecimal currentTemp, BigDecimal futureTemp, Date time, Set plants) {
       this.row = row;
       this.cpu = cpu;
       this.currentTemp = currentTemp;
       this.futureTemp = futureTemp;
       this.time = time;
       this.plants = plants;
    }
   
    public Integer getTmapId() {
        return this.tmapId;
    }
    
    public void setTmapId(Integer tmapId) {
        this.tmapId = tmapId;
    }
    public Row getRow() {
        return this.row;
    }
    
    public void setRow(Row row) {
        this.row = row;
    }
    public Cpu getCpu() {
        return this.cpu;
    }
    
    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }
    public BigDecimal getCurrentTemp() {
        return this.currentTemp;
    }
    
    public void setCurrentTemp(BigDecimal currentTemp) {
        this.currentTemp = currentTemp;
    }
    public BigDecimal getFutureTemp() {
        return this.futureTemp;
    }
    
    public void setFutureTemp(BigDecimal futureTemp) {
        this.futureTemp = futureTemp;
    }
    public Date getTime() {
        return this.time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
    public Set getPlants() {
        return this.plants;
    }
    
    public void setPlants(Set plants) {
        this.plants = plants;
    }




}

