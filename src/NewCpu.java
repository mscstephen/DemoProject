
import h.entities.Cpu;
import h.entities.Row;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kac4
 */
public class NewCpu {
int cpu;
int row;

public NewCpu(Cpu c , Row r){
    cpu = c.getCpuid();
    row = r.getRowId();
}

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
