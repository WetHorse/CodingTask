# Football World Cup Socre Board library

# Description
**Football World Cup Socre Board library** - a simple library for controlling and exploring results of abstract World Championship. It allows to strat / finish matches,
update scores, provides a typical operations for repository, obtain sorted final results by providing filter reqirements (total number of scores).

# Main functional
- Start match (0 - 0 beginning score)
- Update score
- Finish match
- Obtaining final filtred results by:
  . Total number of scores (decreasing)
  . Freshing of addition (using ID's)
- Full test-coverage 
- Typical operations for repository (findById, findAll etc.)
- In-memory storage of data

# Requirements
- Java 19 or higher
- Gradle / Maven

# Gradle dependencies 
- testImplementation(platform("org.junit:junit-bom:5.10.0"))
- testImplementation("org.junit.jupiter:junit-jupiter")
