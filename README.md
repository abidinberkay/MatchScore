### **Notes**

- **Red-Green-Refactor-Repeat TDD pattern is applied.**
- Tests can be run by command **mvn test**. Alternatively **mvn install/package** commands can be used.
- App does not include main method due to being just a simple library.


### **Assumptions**

- **While trying to start a match**, team names cannot be the same. When a team has an ongoing match, a second match can't be started for the same team. Team names cannot be blank.

- **While updating a match**, both team names must exist and be correct.

- **While ending a match**, the match ID must exist and be correct.

- The code is prepared as simply as possible and can be modified according to app requirement.

- AtomicLong is added to start time LocalDateTime.now() to ensure unique start time is used.

- MatchRepository class is not tested separately due to not having complex logic for this task.
But mock of it can be used in ScoreBoard class as well.

###Tech Stack###
- Java 21
- Spring 3.3.4
- Maven
- Lombok
- Javadoc
- Junit