from sim import *
import argparse

# First Come First Serve (FCFS) Scheduler
class FCFS_Scheduler:
    def __init__(self, procs):
        self.procs = procs
        self.ready_queue = []

    def initialize(self, sim):
        for p in self.procs:
            sim.addArrival(p)

    def timeout(self, sim):
        pass

    def stopRunning(self, sim):
        pass

    def arrive(self, p, sim):
        self.ready_queue.append(p)

        if len(self.ready_queue) == 1:
            self.dispatch(sim)

    def unblock(self, p, sim):
        self.ready_queue.append(p)

        if len(self.ready_queue) == 1:
            self.dispatch(sim)

    def idle(self, sim):
        pass

    def dispatch(self, sim):
        if self.ready_queue:
            p = self.ready_queue.pop(0)
            p.updateStatistics("start_time", sim.time)
            sim.addRunningEvent(p, p.service_time)

# Round Robin (RR) Scheduler
class RR_Scheduler:
    def __init__(self, procs, quantum):
        self.procs = procs
        self.quantum = quantum
        self.ready_queue = []
        self.timer_interrupt = False

    def initialize(self, sim):
        for p in self.procs:
            sim.addArrival(p)

    def timeout(self, sim):
        self.timer_interrupt = True

    def stopRunning(self, sim):
        if self.timer_interrupt:
            self.ready_queue.append(sim.running_process)
        else:
            sim.addUnblockEvent(sim.running_process, sim.running_process.service_time)

        self.dispatch(sim)

    def arrive(self, p, sim):
        self.ready_queue.append(p)

        if len(self.ready_queue) == 1:
            self.dispatch(sim)

    def unblock(self, p, sim):
        self.ready_queue.append(p)

        if len(self.ready_queue) == 1:
            self.dispatch(sim)

    def idle(self, sim):
        pass

    def dispatch(self, sim):
        if self.ready_queue:
            p = self.ready_queue.pop(0)
            p.updateStatistics("start_time", sim.time)

            if p.getStatistics("remaining_time") > self.quantum:
                sim.addRunningEvent(p, self.quantum)
                self.timer_interrupt = False
            else:
                sim.addRunningEvent(p, p.getStatistics("remaining_time"))

# Shortest Process Next (SPN) Scheduler
class SPN_Scheduler:
    def __init__(self, procs, service_given, alpha):
        self.procs = procs
        self.service_given = service_given
        self.alpha = alpha
        self.ready_queue = []

    def initialize(self, sim):
        for p in self.procs:
            sim.addArrival(p)

    def timeout(self, sim):
        pass

    def stopRunning(self, sim):
        pass

    def arrive(self, p, sim):
        self.ready_queue.append(p)
        self.ready_queue.sort(key=lambda x: x.getStatistics("remaining_time"))

        if len(self.ready_queue) == 1:
            self.dispatch(sim)

    def unblock(self, p, sim):
        self.ready_queue.append(p)
        self.ready_queue.sort(key=lambda x: x.getStatistics("remaining_time"))

        if len(self.ready_queue) == 1:
            self.dispatch(sim)

    def idle(self, sim):
        pass

    def dispatch(self, sim):
        if self.ready_queue:
            p = self.ready_queue.pop(0)
            p.updateStatistics("start_time", sim.time)
            p.updateStatistics("start_time", sim.time)
            sim.addRunningEvent(p, p.getStatistics("remaining_time"))

# Main Simulation Class
class Simulation:
    def __init__(self, scheduler):
        self.scheduler = scheduler
        self.time = 0
        self.event_queue = []

    def addArrival(self, p):
        self.event_queue.append(Event(p.arrival_time, EventType.ARRIVE, p))

    def addUnblockEvent(self, p, duration):
        self.event_queue.append(Event(self.time + duration, EventType.UNBLOCK, p))

    def addRunningEvent(self, p, duration):
        self.event_queue.append(Event(self.time + duration, EventType.STOP_RUNNING, p))
        p.updateStatistics("remaining_time", p.getStatistics("remaining_time") - duration)

    def run(self):
        self.scheduler.initialize(self)

        while self.event_queue:
            event = min(self.event_queue, key=lambda x: x.time)
            self.time = event.time

            if event.event_type == EventType.ARRIVE:
                self.scheduler.arrive(event.process, self)
            elif event.event_type == EventType.UNBLOCK:
                self.scheduler.unblock(event.process, self)
            elif event.event_type == EventType.STOP_RUNNING:
                self.scheduler.stopRunning(self)

            self.event_queue.remove(event)

        self.printStatistics()

    
    def printStatistics(self):
        total_turnaround_time = 0
        total_normalized_turnaround_time = 0
        total_response_time = 0

        for p in self.scheduler.procs:
            turnaround_time = p.getStatistics("finish_time") - p.arrival_time
            normalized_turnaround_time = turnaround_time / p.service_time
            response_time = p.getStatistics("start_time") - p.arrival_time

            p.updateStatistics("turnaround_time", turnaround_time)
            p.updateStatistics("normalized_turnaround_time", normalized_turnaround_time)
            p.updateStatistics("response_time", response_time)

            total_turnaround_time += turnaround_time
            total_normalized_turnaround_time += normalized_turnaround_time
            total_response_time += response_time

        num_procs = len(self.scheduler.procs)
        mean_turnaround_time = total_turnaround_time / num_procs
        mean_normalized_turnaround_time = total_normalized_turnaround_time / num_procs
        mean_response_time = total_response_time / num_procs

        print("Mean Turnaround Time:", mean_turnaround_time)
        print("Mean Normalized Turnaround Time:", mean_normalized_turnaround_time)
        print("Mean Response Time:", mean_response_time)

# Main
# ...

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Process Scheduling Simulator")
    parser.add_argument('scheduler', choices=['FCFS', 'RR', 'SPN'], help='scheduler algorithm', nargs='?', default='FCFS')
    parser.add_argument("quantum", type=int, nargs="?", default=0, help="Time quantum for RR scheduler")
    parser.add_argument("service_given", choices=["true", "false"], nargs="?", default="true",
                        help="Flag indicating whether service time is given or not for SPN scheduler")
    parser.add_argument("alpha", type=float, nargs="?", default=0.5, help="Weight factor for SPN scheduler")

    args = parser.parse_args()

    procs = []

    # Load processes from file
    with open("processes.txt", "r") as file:
        for line in file:
            parts = line.split()
            arrival_time = int(parts[0])
            service_time = 0
            activities = []

            if args.scheduler == "SPN":
                service_time = int(parts[1]) if args.service else None
                activities = [int(duration) for duration in parts[2:]]
            else:
                service_time = int(parts[1])
                activities = [int(duration) for duration in parts[2:]]

            proc = Process(arrival_time, service_time, activities)
            procs.append(proc)

    scheduler = None

    if args.scheduler == "FCFS":
        scheduler = FCFS_Scheduler(procs)
    elif args.scheduler == "RR":
        scheduler = RR_Scheduler(procs, args.quantum)
    elif args.scheduler == "SPN":
        scheduler = SPN_Scheduler(procs, args.service_given, args.alpha)

    if scheduler is None:
        print("Invalid scheduler specified.")
        exit(1)

    simulation = Simulation(scheduler)
    simulation.run()
