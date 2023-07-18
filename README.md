# AppointmentSchedulingApp

AUTHOR: Diego Rodriguez
Application Version: 1.0
Submission Date: 04/09/2023

PURPOSE:
The purpose of this application is to provide a GUI based scheduling application system that
allows users to manage appointment scheduling in a company. Users are able to create, update, and delete customers.
As well as schedule, update, and appointments for customers. All actions are reflected in the SQL database.

IDE & Java Versions:
IDE version: IntelliJ IDEA 2021.1.3 (Community Edition)
JDK version: 17.0.1
JFX SKD version: openjfx-17.0.1
JDBC: mysql-connector-java-8.0.25

Additional Report:
The additional report adds the feature to see total customers in each division.
It only displays divisions that currently have customers. If a certain division doesn't have any customers,
the program will not display that division.

HOW TO RUN PROGRAM:
1. Open Intellij Idea and click on 'File' then 'Open' to open this project.
2. Add correct JDK, JavaFX SDK, and JDBC to project under 'File' -> 'Project Structure' -> 'Libraries'.
3. Input correct information in src/wgu/main/JDBC.java to successfully connect to your mysql DB.
4. Set program Run Configuration to Main.java
5. Click run/play button and application will start.


HOW TO USE PROGRAM:
When the program starts the user is presented with a login screen. If the user does not enter the correct username/password
the program will not grant entry. Once the user enters the correct information, they are now able to create and modify customers,
as well as appointments.
