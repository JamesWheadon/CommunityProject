# Community Project - Species Reintroduction 🐒

This project is a futureproof community project completed in 1 month.    
Theme: Life on Earth - Mapping conservation efforts of reintroducing species    

<img width="1280" alt="Screenshot 2022-08-25 at 17 21 53" src="https://user-images.githubusercontent.com/58271566/186718237-649c014a-ad9e-4c95-bb52-4624d419c733.png">

## Description
The app is a directory to search for animals that have been reintroduced to the wild. It is a full-stack app with React on the front-end connecting to Google Maps JavaScript API, and Java11, Spring and Gradle on the back-end. 

# Installation and Usage
Clone or download this repo.    
Navigate to root directory of this repository and open the terminal:   


To start up the client:   

### React 
`cd react-client`   
`npm install`     
`npm start`   

It should load on: http://localhost:3000/

To start up the backend:

### Java
Java 11 - you'll need a JDK installed on your machine.

Its recommended to use a tool to manage the version you have installed such as SDKMan or Homebrew [tool](https://sdkman.io/).       
`brew install openjdk@11`   

Once you have this you can install Java.    
`brew install java11`      

### Gradle
Most projects you will come across will use Gradle as a build tool - you can install it from [here](https://docs.gradle.org/current/userguide/installation.html).    

[Background information](https://azureford.sharepoint.com/sites/SDE/SitePages/Gradle/Gradle_Overview.aspx)     
`brew install gradle`    

`cd backend`    
`./gradlew clean build`    
If you get any permissions error, try `chmod +x gradlew`      
`./gradlew bootRun`    

It should load on: http://localhost:8080/

# Technologies
- HTML, CSS, JavaScript,

### Dependencies: 
   - Backend: Java11, Springboot, Gradle, Cucumber
   
   - Client: react, react-router-dom, materialUI, axios  

# Process 
1. Plan! Come up with an idea using Miro to brainstorm
2. Set up backend, database, and install necessary dependencies 
3. Create react front-end using `create-react-app` 
4. Use fake data to temporarily render data whilst waiting for back-end to be completed
5. Add modals/info window for clicking on map markers
6. Snazzy maps to create a prettier map theme
7. Add sidebar which has a list of all the markers
8. Clicking on item in sidebar list will move map to respective marker
9. Connecting to back-end!

## Bugs
- [x] InforWindow shows a second ugly marker outline - remove React.Strictmode as it is not compatible with React18
- Title isn't sticky
- [ ] Directory InfoWindow onCloseClick not funtioning

# Wins & Challenges

### Wins
- Working on seperate sections means no git conflicts
- Completing the MVP in time for demo

### Challenges  
- Understanding and implementing Google Maps API as it is a new technology! 
- Managing time to complete this project alongside full time job and other commitments 

# Future Features
- Google Places integration
- Would be good to get more info about the species e.g when did they get reintroduced, description etc
- Grouping pins together to make a cleaner front-end look
- Filter results 
