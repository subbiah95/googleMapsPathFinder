# Problem Statment

You are given two locations - A & B. You are provided with latitude & longitude of them (short form LatLngs). You need to calculate ‘real’ points on the road that connects A & B. You need to use data from google directions API .

Sample Input: LatLngs for point A and point B.
A is 12.93175, 77.62872 and B is 12.92662, 77.63696

Sample Output: LatLngs at a constant distance interval of 50 m between A & B on the road.

12.93175,77.62872  
12.93166,77.62852  
12.93125,77.62870 
.....

## Class Description

### RoutePlotter
The main class, which access other class to get the route details.
### GoogleRoutes
The class which access the google's interface to get the details.
### exception.UnExpectedException
The class has defined an custom exception, this will be thrown when something unexpected happenes.
### model.RouteDetails
The class is the model class, which holdes the different routes between Origin and Destination.
### model.LegDetails
The class is the model class, which holdes a list of Step points between Leg's Origin and Leg's Destination.

#### Google's API document - https://developers.google.com/maps/documentation/directions/get-directions