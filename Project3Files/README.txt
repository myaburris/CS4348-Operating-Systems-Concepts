Project3.py:
    Purpose: This file serves as the main entry point for your process scheduling simulation project.
    Functionality: It contains the main simulation class, Simulation, which coordinates the simulation process. It handles events such as process arrivals, unblocking, and stopping of running processes. It also interacts with the scheduler to initialize and dispatch processes.
    It uses command-line arguments to determine the scheduler algorithm and its specific parameters.
    It reads process data from a file called "processes.txt" and creates Process objects based on the specified scheduler algorithm.
    its instantiates the appropriate scheduler(FCFS, RR, and SPN) based on the command-line arguments and runs the simulation by calling the run() method of the Simulation class.

Sim.py:
    Purpose: This file contains the simulation logic and related classes required for process scheduling.
    Functionality: It defines several classes that represent the simulation components:
    Process: Represents a process with attributes such as arrival time, service time, activities, and statistics.
    Event: Represents an event in the simulation, such as process arrival, unblocking, or stop running.
    EventType: An enumeration that defines the types of events.
    FCFS_Scheduler, RR_Scheduler, and SPN_Scheduler: Scheduler classes implementing specific scheduling algorithms. They define methods to handle process arrivals, unblocking, timeouts, and dispatching of processes.
    Simulation: The main simulation class that coordinates the simulation process by managing the event queue, time, and interaction with the scheduler.
    It also defines helper methods within these classes to update process statistics and perform necessary calculations.


To compile and run this project, run Project3.py on any IDE
