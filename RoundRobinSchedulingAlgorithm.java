/** RoundRobinSchedulingAlgorithm.java
 *
 * A scheduling algorithm that randomly picks the next job to go.
 *
 * @author: Kyle Benson
 * Winter 2013
 *
 */
package com.jimweller.cpuscheduler;

import java.util.*;

public class RoundRobinSchedulingAlgorithm extends BaseSchedulingAlgorithm {

    /** the time slice each process gets */
    private int quantum = 10;
    private List<Process> jobs;
    private int currentIndex;
    private int quantumCounter;

    RoundRobinSchedulingAlgorithm() {
        // Fill in this method
        /*------------------------------------------------------------*/
        quantumCounter = 0;
        jobs = new ArrayList<>();
        currentIndex = 0;
        /*------------------------------------------------------------*/
    }

    /** Add the new job to the correct queue. */
    public void addJob(Process p) {

        // Fill in this method
        /*------------------------------------------------------------*/
        jobs.add(p);
        /*------------------------------------------------------------*/
    }

    /** Returns true if the job was present and was removed. */
    public boolean removeJob(Process p) {

        // Fill in this method
        /*------------------------------------------------------------*/
        int removeIndex = jobs.indexOf(p);
        boolean returnValue = jobs.remove(p);
        if (returnValue) {
            if (currentIndex > removeIndex) {
                currentIndex--;
            }
            return true;
        }
        return false;
        /*------------------------------------------------------------*/
    }



    /** Transfer all the jobs in the queue of a SchedulingAlgorithm to another, such as
    when switching to another algorithm in the GUI */
    public void transferJobsTo(SchedulingAlgorithm otherAlg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the value of quantum.
     *
     * @return Value of quantum.
     */
    public int getQuantum() {
        return quantum;
    }

    /**
     * Set the value of quantum.
     *
     * @param v
     *            Value to assign to quantum.
     */
    public void setQuantum(int v) {
        this.quantum = v;
    }

    /**
     * Returns the next process that should be run by the CPU, null if none
     * available.
     */
    public Process getNextJob(long currentTime) {

        // Fill in this method
        /*------------------------------------------------------------*/
        if (jobs.size() == 0) return null;
        if (activeJob == null || isJobFinished() || quantum == quantumCounter) {
            if (currentIndex >= jobs.size()) currentIndex = 0;
            activeJob = jobs.get(currentIndex++);
            quantumCounter = 0;
        }
        quantumCounter++;
        return activeJob;
        /*------------------------------------------------------------*/

    }

    public String getName() {
        return "Round Robin";
    }

}
