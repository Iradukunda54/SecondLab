package service;

import model.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ConcurrencyService — Simulates parallel task updates.
 * Demonstrates: Thread, Runnable, synchronized methods,
 *               ExecutorService, parallel streams (Week 3 Feature 4).
 */
public class ConcurrencyService {

    // ── synchronized counter for thread-safe progress tracking ───────────────
    private final AtomicInteger completedCount = new AtomicInteger(0);

    /**
     * Simulates concurrent task status updates across multiple projects.
     * Each update runs in its own named thread (@spec: Thread-N updating T00X → status)
     *
     * @param projects  Collection of projects to sample tasks from
     */
    public void runConcurrentSimulation(Collection<Project> projects) {
        System.out.println("\n  ╔══════════════ PARALLEL TASK UPDATE SIMULATION ══════════════╗");
        System.out.println("  ║                                                              ║");

        // Gather up to 5 tasks across projects for the simulation
        List<Task[]> updateTargets = buildUpdateTargets(projects, 5);

        if (updateTargets.isEmpty()) {
            System.out.println("  ║  No tasks available for simulation.                          ║");
            System.out.println("  ╚══════════════════════════════════════════════════════════════╝");
            return;
        }

        System.out.printf("  ║  Starting %d threads...%n", updateTargets.size());
        System.out.println("  ║                                                              ║");

        completedCount.set(0);
        ExecutorService executor = Executors.newFixedThreadPool(updateTargets.size());

        String[] statuses = {"In Progress", "Completed", "Completed", "In Progress", "Pending"};

        for (int i = 0; i < updateTargets.size(); i++) {
            final int threadNum  = i + 1;
            final Task task      = updateTargets.get(i)[0];
            final String newStatus = statuses[i % statuses.length];

            executor.submit(() -> simulateUpdate(threadNum, task, newStatus));
        }

        // Shutdown and wait for all tasks to finish
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("  ║                                                              ║");
        System.out.printf("  ║  [✓] All %d threads finished successfully.%n", updateTargets.size());
        System.out.println("  ║      Task updates applied concurrently and safely.           ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Synchronized update method — ensures only one thread updates at a time.
     * Demonstrates thread safety with synchronized.
     */
    private synchronized void simulateUpdate(int threadNum, Task task, String newStatus) {
        // Simulate processing delay
        try { Thread.sleep(150 + (long)(Math.random() * 200)); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        String oldStatus = task.getStatus();
        task.setStatus(newStatus);
        int done = completedCount.incrementAndGet();

        System.out.printf("  ║  Thread-%-2d  updating %-6s %-12s → %-14s  ║%n",
                threadNum, task.getId(), "[" + oldStatus + "]", newStatus);
    }

    /**
     * Gathers tasks from projects (up to maxCount).
     * Returns pairs of [task] for the simulation.
     */
    private List<Task[]> buildUpdateTargets(Collection<Project> projects, int maxCount) {
        List<Task[]> targets = new ArrayList<>();
        for (Project p : projects) {
            for (Task t : p.getTaskList()) {
                targets.add(new Task[]{t});
                if (targets.size() >= maxCount) return targets;
            }
        }
        return targets;
    }

    /**
     * Demonstrates parallel streams for batch status aggregation.
     * Prints counts per status category in parallel.
     */
    public void runParallelStreamDemo(Collection<Project> projects) {
        System.out.println("\n  ── Parallel Stream Status Count ──────────────────────────────");

        List<Task> allTasks = projects.parallelStream()
                .flatMap(p -> p.getTaskList().stream())
                .toList();

        Map<String, Long> statusCounts = allTasks.parallelStream()
                .collect(java.util.stream.Collectors.groupingByConcurrent(
                        Task::getStatus, java.util.stream.Collectors.counting()));

        statusCounts.forEach((status, count) ->
                System.out.printf("     %-15s : %d tasks%n", status, count));
        System.out.println("  ──────────────────────────────────────────────────────────────");
    }
}
