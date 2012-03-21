package h.entities;
// Generated 29-Feb-2012 13:51:12 by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Processingrate generated by hbm2java
 */
public class Processingrate  implements java.io.Serializable {


     private Integer proId;
     private String name;
     private BigDecimal processingPower;
     private Set cpus = new HashSet(0);

    public Processingrate() {
    }

    public Processingrate(String name, BigDecimal processingPower, Set cpus) {
       this.name = name;
       this.processingPower = processingPower;
       this.cpus = cpus;
    }
   
    public Integer getProId() {
        return this.proId;
    }
    
    public void setProId(Integer proId) {
        this.proId = proId;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getProcessingPower() {
        return this.processingPower;
    }
    
    public void setProcessingPower(BigDecimal processingPower) {
        this.processingPower = processingPower;
    }
    public Set getCpus() {
        return this.cpus;
    }
    
    public void setCpus(Set cpus) {
        this.cpus = cpus;
    }




}

