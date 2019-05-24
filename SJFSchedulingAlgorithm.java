/**
 * SJFSchedulingAlgorithm.java
 * <p>
 * A shortest job first scheduling algorithm.
 *
 * @author: Charles Zhu
 * Spring 2016
 */
package com.jimweller.cpuscheduler;

import java.util.*;

public class SJFSchedulingAlgorithm extends BaseSchedulingAlgorithm implements OptionallyPreemptiveSchedulingAlgorithm {

    private boolean preemptive;
    private List<Process> jobs;

    SJFSchedulingAlgorithm() {
        // Fill in this method
        /*------------------------------------------------------------*/
        preemptive = false;
        jobs = new ArrayList<>();
        /*------------------------------------------------------------*/
    }

    // Comparators
    /*------------------------------------------------------------*/
    class SJFSComparatorPreemptive implements Comparator<Process> {
        public int compare(Process p1, Process p2) {
            if (p1.getBurstTime() != p2.getBurstTime()) {
                return p1.getBurstTime() > p2.getBurstTime() ? 1 : -1;
            } else {
                return p1.getPID() > p2.getPID() ? 1 : -1;
            }
        }
    }

    class SJFSComparatorNotPreemptive implements Comparator<Process> {
        public int compare(Process p1, Process p2) {
            if (p1.getInitBurstTime() != p2.getInitBurstTime()) {
                return p1.getInitBurstTime() > p2.getInitBurstTime() ? 1 : -1;
            } else {
                return p1.getPID() > p2.getPID() ? 1 : -1;
            }
        }
    }

    SJFSComparatorPreemptive sp = new SJFSComparatorPreemptive();
    SJFSComparatorNotPreemptive snp = new SJFSComparatorNotPreemptive();
    /*------------------------------------------------------------*/

    /** Add the new job to the correct queue.*/
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
        if (p == activeJob) {
            // if p is the currentJob, make activeJob null
            activeJob = null;
        }
        return jobs.remove(p);
        /*------------------------------------------------------------*/
    }

    /** Transfer all the jobs in the queue of a SchedulingAlgorithm to another, such as
     when switching to another algorithm in the GUI */
    public void transferJobsTo(SchedulingAlgorithm otherAlg) {
        throw new UnsupportedOperationException();
    }

    /** Returns the next process that should be run by the CPU, null if none available.*/
    public Process getNextJob(long currentTime) {

        // Fill in this method
        /*------------------------------------------------------------*/
        if (jobs.isEmpty()) return null;
        if (preemptive) {
            // if preemptive, use sp to sort
            Collections.sort(jobs, sp);
            activeJob = jobs.get(0);
        } else {
            if (isJobFinished()) {
                // if notPreemptive and the currentJob finished, use snp to sort
                Collections.sort(jobs, snp);
                activeJob = jobs.get(0);
            }
        }
        return activeJob;
        /*------------------------------------------------------------*/
    }

    public String getName() {
        return "Shortest Job First";
    }

    /**
     * @return Value of preemptive.
     */
    public boolean isPreemptive() {

        // Fill in this method
        /*------------------------------------------------------------*/
        return preemptive;
        /*------------------------------------------------------------*/
    }

    /**
     * @param v  Value to assign to preemptive.
     */
    public void setPreemptive(boolean v) {

        // Fill in this method
        /*------------------------------------------------------------*/
        preemptive = v;
        /*------------------------------------------------------------*/
    }

}
