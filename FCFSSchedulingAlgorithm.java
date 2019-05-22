/** FCFSSchedulingAlgorithm.java
 *
 * A first-come first-served scheduling algorithm.
 * The current implementation will work without memory management features
 *
 */
package com.jimweller.cpuscheduler;

import java.util.*;



public class FCFSSchedulingAlgorithm extends BaseSchedulingAlgorithm {

  	private ArrayList<Process> jobs;
    private boolean log;

	// Add data structures to support memory management
	/*------------------------------------------------------------*/
    public class MemInterval {
        long start;
        long end;
        MemInterval(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
    private String memManageType;
    private Map<Process, MemInterval> intervalMapping;
    private TreeSet<MemInterval> intervals;
	/*------------------------------------------------------------*/

    // Comparators
    /*------------------------------------------------------------*/
    class FCFSComparator implements Comparator<Process> {
        public int compare(Process p1, Process p2) {
            if (p1.getArrivalTime() != p2.getArrivalTime()) {
                return Long.signum(p1.getArrivalTime() - p2.getArrivalTime());
            }
            return Long.signum(p1.getPID() - p2.getPID());
        }
    }

    class IntervalComparator implements Comparator<MemInterval> {
        public int compare(MemInterval i1, MemInterval i2) {
            return Long.signum(i1.start - i2.start);
        }
    }

    FCFSComparator comparator = new FCFSComparator();
    IntervalComparator intervalComparator = new IntervalComparator();
    /*------------------------------------------------------------*/



	FCFSSchedulingAlgorithm() {
		activeJob = null;
		jobs = new ArrayList<Process>();
        log = true;

		// Initialize memory
		/*------------------------------------------------------------*/
        intervalMapping = new HashMap<>();
        intervals = new TreeSet<>(intervalComparator);
        addInterval(null, 380, 380, false);
		/*------------------------------------------------------------*/

	}

    public void addInterval(Process p, long start, long end, boolean log) {
        MemInterval newInterval = new MemInterval(start, end);
        intervalMapping.put(p, newInterval);
        intervals.add(newInterval);
        if (log) {
          System.out.println("add process id: " + p.getPID() + " allocated in : " + start + " " + end);
          System.out.println("------------------------------------------");
        }
    }

	/** Add the new job to the correct queue. */
	public void addJob(Process p) {

	// Check if any memory is available
	/*------------------------------------------------------------*/
        long mem = p.getMemSize();
        long start = 0;
        long minStart = 0;
        long min = 381;
        boolean setSuccess = false;
        Iterator<MemInterval> iterator = intervals.iterator();
        while (iterator.hasNext()) {
            MemInterval interval = iterator.next();
            long curChunkSize = interval.start - start;
            if (curChunkSize >= mem) {
                if (memManageType.equals("FIRST")) {
                    addInterval(p, start, start + mem, log);
                    setSuccess = true;
                    break;
                } else if (memManageType.equals("BEST")) {
                    if (curChunkSize < min) {
                        min = curChunkSize;
                        minStart = start;
                    }
                }
            }
            start = interval.end;
        }

        if (memManageType.equals("BEST") && min != 381) {
          addInterval(p, minStart, minStart + mem, log);
          setSuccess = true;
        }
  	/*------------------------------------------------------------*/

        if (!setSuccess) {
            if (log) {
                System.out.println("process " + p.getPID() + " is been igonred");
                System.out.println("------------------------------------------");
            }
            p.setIgnore(true);
            return;
        }

        jobs.add(p);
        Collections.sort(jobs, comparator);
	}

	/** Returns true if the job was present and was removed. */
	public boolean removeJob(Process p) {
		if (p == activeJob)
			activeJob = null;

		// In case memory was allocated, free it
		/*------------------------------------------------------------*/
        if (!intervalMapping.containsKey(p)) return false;
        if (log) {
            System.out.println("remove process id: " + p.getPID() + " mem size is : " + p.getMemSize());
            System.out.println("------------------------------------------");
        }
        intervals.remove(intervalMapping.get(p));
        intervalMapping.remove(p);
		/*------------------------------------------------------------*/

		return jobs.remove(p);
	}

	/**
	 * Transfer all the jobs in the queue of a SchedulingAlgorithm to another, such
	 * as when switching to another algorithm in the GUI
	 */
	public void transferJobsTo(SchedulingAlgorithm otherAlg) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the next process that should be run by the CPU, null if none
	 * available.
	 */
	public Process getNextJob(long currentTime) {
		Process earliest = null;

		if (!isJobFinished())
			return activeJob;
		if (jobs.size() > 0)
			earliest = jobs.get(0);
		activeJob = earliest;
		return activeJob;
	}

	public String getName() {
		return "First-Come First-Served";
	}

	public void setMemoryManagment(String v) {
		// Modify class to suppor memory management
        memManageType = v;
	}
}
