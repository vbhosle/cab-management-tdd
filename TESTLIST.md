1. Onboard various cities where cab services are provided.
   - if no city is onboarded, then book fails with NoCabServiceInTheCity 
   - if a city is onboarded, and cab is not registered, then book fails with CabNotAvailableException 
   - if a city is onboarded, and cab is registered and ON_TRIP, then book fails with CabNotAvailableException 
   - if a city is onboarded, and cab is registered and IDLE, then booking succeeds. 
   - trying to onboard a city that is already onboarded, fails with CityAlreadyOnboardedException 

2. Change current city (location) of any cab. 
   - trying to change city of non-registered cabId fails with CabDoesNotExistException
   - change in location of cab to the same location of it's current location, doesn't do anything.
   - change in location while ON_TRIP fails with OperationNotAllowedWhileOnTrip
   - only cab in system. location changes from texas to newyork. location change is audited. +booking in texas fails. +booking in newyork succeeds.
   - changing location to non-registered city fails with CityNotOnboardedException

3. Change state of any cab. For this you will have to define a state machine for the cab ex:
   - a cab must have at least these two basic states; IDLE and ON_TRIP
   - default state is IDLE on registration
   - one change in state of cab post registration has two audit logs. one for registration and one for state change.
   - changing state from IDLE to IDLE doesn't add any new audit log.
   - changing state from ON_TRIP to ON_TRIP doesn't add any new audit log.
   - only cab in system. state ON_TRIP then booking fails. state changes to IDLE then booking succeeds.

4. Book cabs based on their availability at a certain location. In case more than one cab are
   available , use the following strategy;
   a. Find out which cab has remained idle the most and assign it.
   b. In case of clash above, randomly assign any cab

a. Find out which cab has remained idle the most and assign it.
   - cab state is IDLE post registration.
   - 1 minute post registration, cab IDLE time is 1 minute.
   - cab IDLE time increases as time passes. 1 min, 2 min
   - when cab state is ON_TRIP idle time is 0
   - when cab state change from ON_TRIP to IDLE. and state change passes 1 minute then cab is IDLE for 1 minute
   - two cabs in a system in ON_TRIP state. cab-1 goes to IDLE, one minute after that cab-2 goes IDLE. Then cab-1 is the most IDLE cab.
   - two cabs in a system in ON_TRIP state. both go to IDLE at the same time. Then most IDLE cab is randomly chosen.
   - three cabs in a system. IDLE for 1,2,3 min. first booking books cab-3, second cab-2, third cab-1

a. Provide insights such as for how much time was a cab idle in a given duration ?
What insights?
   - total, average, min, max idle time in given duration
   - if cab was idle 1,2,3 mins for given hour then total is 6, avarage 2, min 1, max 3
   - if cab was idle 10 mins for given hour then total, average, min, max is 10
   - if cab was on trip for given hour then otal, average, min, max idle time is 0
   - if cab was not registered for given hour, then return no metric
   - for furture hour, no metric

