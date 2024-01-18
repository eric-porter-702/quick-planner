# Quick Planner

- [Description and Usage](#description-and-usage)
- [Installation and Set Up](#installation-and-set-up)
    - [IDE](#ide)
    - [JDK](#jdk)
    - [Scene Builder](#scene-builder)
    - [Maven](#maven)
    - [Cloning](#cloning)
    - [Intellij's Built in Git](#intellijs-built-in-git)
- [Project Structure](#project-structure)
  - [Controllers](#controllers)
  - [Workers](#workers)
  - [Resources](#resources)
  - [Additional files](#additional-files)
- [Developers](#developers)
- [Support](#support)

## Description and Usage
The Quick Planner is a school planning application used to create and organize your tasks for courses you are currently taking.

For more information on how to make use of the Quick Planner application, view the user guide.

***

## Installation and Set Up

### IDE
The IDE used to build the Quick Planner project in is IntelliJ. The latest version of IntelliJ can be downloaded here: https://www.jetbrains.com/idea/download/#section=windows.

### JDK
The Quick Planner project uses JDK version 19.0.2, which can be downloaded here: https://jdk.java.net/19/.

### Scene Builder
This project includes FXML files that can be edited using SceneBuilder. You can either use the SceneBuilder that is included with IntelliJ, or, you can use the separate application which can be downloaded here: https://gluonhq.com/products/scene-builder/.

### Maven
This project uses maven dependencies for setting up JavaFX. This dependency is included with the Quick Planner project with the pom.xml file, so no set up for maven dependencies are required. Just allow Intellij to build it with the project.

### Cloning
Link to the git repository for cloning to HTTPS: https://gitlab.cci.drexel.edu/fds23/61/team-7/quick-planner.git

### Intellij's Built in Git
Once you have initialized and cloned the project to the directory of your choosing, you can then use IntelliJ's built-in git features to pull, commit, and push any changes that are made to the project. 


## Project Structure
In the main folder in src, there are two packages as well as a resource folder:

### Controllers
- The quickplanner.application package deals with the front-end of the and includes controller classes. These controller classes are used to create and bind JavaFX functionalities for each individual FXML file.

### Workers
- The quickplanner.workers package deals with the backend of the project.

### Resources
- The resources folder includes additional folders used to store the FXML files, CSS files, and additional images. These files deal with the graphical design, UI, and UX for the front-end, which is done through the use of Scene-builder.

### Additional files
This project also includes .txt files with a short bio for each of the developers.


## Developers
#### Chris Jarocha
#### Eric Porter
#### Jeremy Torres
#### Michael Rivera


## Support
For additional support or questions, you can contact any of the developers through their email:

#### Chris Jarocha
- cj658@drexel.edu

#### Eric Porter
- esp62@drexel.edu

#### Jeremy Torres
- jgt49@drexel.edu

#### Michael Rivera
- mar558@drexel.edu
