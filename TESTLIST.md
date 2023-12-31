# Empty System and defaults.
- ~~No city in the system: System with no cities onboarded. Attempt to book cab in any city, fails with CityNotOnboardedException.~~
- ~~No cab in the city: System with city-1 onboarded. Attempt to book a cab at city-1, fails with CabNotAvailableException.~~
- ~~System with city-1 onboarded. Attempt to book a cab at city-1, fails with CabNotAvailableException. Attempt to book a cab in city-2, fails with CityNotOnboardedException.~~
  - ~~System with city-1, city-2 onboarded. cab-x registered at city-2. Attempt to book a cab in city-1, fails with CabNotAvailableException.~~
  - ~~[CityNotOnboardedException & CityNotOnboardedException already covered in previous tests. May be needed to remove hardcoded exceptions]~~

# add cabs, update cab state and location
- ~~Input <cab-1, IDLE, city-1> added in system. getting cab-1 details returns <cab-1, IDLE, city-1>~~
  - ~~Input two cabs <cab-1, IDLE, city-1> and <cab-2, IDLE, city-1> added in system. getting cab-1 details returns <cab-1, IDLE, city-1> and getting cab-2 details returns <cab-2, IDLE, city-1>~~
- ~~Input <cab-1, IDLE, city-1> added in system. change current city of cab-1 to city-2. getting cab-1 details returns <cab-1, IDLE, city-2>~~

# ON_TRIP location is indeterminate
- ~~Input <cab-1, IDLE, city-1> added in system. change state of cab-1 to ON_TRIP. getting cab-1 details returns <cab-1, ON_TRIP, INDETERMINATE>~~
- ~~Input <cab-1, ON_TRIP, city-1> added in system. getting cab-1 details returns <cab-1, ON_TRIP, INDETERMINATE>~~
- ~~Input <cab-1, ON_TRIP, city-1> added in system. change current city of cab-1 to city-2 fails with OperationNotAllowedWhileOnTripException.~~
- ~~Input <cab-1, ON_TRIP, city-1> added in system. change state of cab-1 to IDLE in city-2. getting cab-1 details returns <cab-1, IDLE, city-2>~~
 
# Test Fixture: city-1 onboarded. cab-x registered at city-1.
- ~~IDLE cab added to city-1. Booking succeeds and cab state changes to ON_TRIP.~~
- ~~ON_TRIP cab added to city-1. Booking fails with CabNotAvailableException.~~
- ~~Change ON_TRIP cab to IDLE. Booking succeeds and cab state changes to ON_TRIP.~~
- ~~Change IDLE cab to ON_TRIP. Booking fails with CabNotAvailableException.~~
- ~~Change IDLE cab location to city-2. Booking for city-1 fails with CabNotAvailableException.~~

# Keep record of cab history of each cab. (A cab history could just be a record of what all states a cab has gone through)
## Test Fixture: city-1 onboarded. cab-x registered at city-1.
- Registration of cab-x at city-1 is recorded in cab history: Cab history of cab-x has one entry with state IDLE and location city-1.
- Change state in state is recorded in cab history: cab-x state is changed to ON_TRIP. Cab history of cab-x has two entries with state IDLE and location city-1, and state ON_TRIP and location INDETERMINATE.
- Change location in state is recorded in cab history: cab-x state is changed to ON_TRIP. Change state of cab-x to IDLE with city-1. Cab history of cab-x has three entries with state IDLE and location city-1, state ON_TRIP and location INDETERMINATE, and state IDLE and location city-1.
- System captures chronology of cab history using timestamp: assert timestamp in above test

# Find out which cab has remained idle the most and assign it.
## Test Fixture: city-1 onboarded. cab-x registered at city-1.
- ~~IDLE cab added to city-1. Immediately on adding idle time is 0, after 1 minute idle time is 1 minute.~~
- ~~ON_TRIP cab added to city-1. Immediately on adding idle tiem is 0, after 1 minute idle time is 0.~~
- ~~Changing from IDLE to ON_TRIP resets idle time to 0: cab-x is IDLE since 1 minute. State is changed to ON_TRIP. cab-x IDLE time is 0. after 1 minute cab-x IDLE time is 0.~~
- ~~Changing from ON_TRIP to IDLE starts tracking idle time: cab-x is ON_TRIP. State is changed to IDLE. cab-x IDLE time is 0. after 1 minute cab-x IDLE time is 1.~~
- ~~Changing city of IDLE cab does not reset idle time: cab-x is IDLE since 1 minute. Change state of cab-x to IDLE with city-2. cab-x IDLE time is 1 minute. after 1 minute cab-x IDLE time is 2 minute.~~
- ~~Book most idle cab among two~~
- ~~When two cabs have same idle time, book one randomly.~~
- Book least idle cab among two.
- When two cabs have same idle time, book one with least number of trips in last 24 hours.
- Book a most idle cab with given rating. (rating is a number between 1 and 5)
- Book a most idle cab with given rating and model. (model is a string)

# validation
- changing city to city not onboarded fails with CityNotOnboardedException

# Refactoring
- Cab current location and state are variable. They collectively form a state of the cab. So, they should be encapsulated in a class.
- Move cab state and location change into another class.

a. Provide insights such as for how much time was a cab idle in a given duration ?
- ~~Cab was idle for whole day. Any hour during that day cab was idle for 60 mins.~~
- Cab was on-trip for whole day. Any hour during that day cab was idle for 0 mins.
- ~~In an hour cab was on-trip twice once for 15 mins, and once for 30 mins. So, cab was idle for 15 mins in that hour.~~
- Overlapping hours: Cab was idle from 6am to 7am, between 7am to 8am it was on-trip. So from 6.30-7.30 it was idle for 30 mins.
b. Keep a record of the cab history of each cab. (A cab history could just be a record of
what all states a cab has gone through)
- ~~When IDLE cab is added to system. Cab history of the cab has one entry for IDLE state.~~
- When ON_TRIP cab is added to system. Cab history of the cab has one entry for ON_TRIP state.
- ~~Add IDLE cab, change state to ON_TRIP, then IDLE. Cab history of the cab has three entries for IDLE, ON_TRIP, IDLE state.~~

c. Find cities where the demand for cabs is high and the time when the demand is highest