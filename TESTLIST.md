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
- Input <cab-1, ON_TRIP, city-1> added in system. change current city of cab-1 to city-2 fails with OperationNotAllowedWhileOnTripException.
- Input <cab-1, ON_TRIP, city-1> added in system. change state of cab-1 to IDLE in city-2. getting cab-1 details returns <cab-1, IDLE, city-2>
 
# Test Fixture: city-1 onboarded. cab-x registered at city-1.
- ON_TRIP cab is not available for on the spot booking: cab-x state is changed to ON_TRIP. Then booking in city-1 fails with CabNotAvailableException.
- ON_TRIP location is indeterminate: cab-x state is changed to ON_TRIP. Then get location of cab-x returns a constant "INDETERMINATE". 
- ON_TRIP location is indeterminate, can't be changed: cab-x state is changed to ON_TRIP. Then changing location of cab-x fails with OperationNotAllowedWhileOnTripException.
- Always capture location along with IDLE state, otherwise how system knows where the cab is available?
  - city-2 is onboarded. cab-x state is changed to ON_TRIP. Change state of cab-x to IDLE with city-2. Now cab is IDLE in city-2.
- Cab booking changes state: Attempt to book a cab in city-1. cab-x is booked and its state is ON_TRIP.
- Booked cab becomes unavailable for next booking: Attempt to book a cab in city-1. cab-x is booked and ON_TRIP. Another attempt to book a cab in city-1, fails with CabNotAvailableException.
  - [Not required: What happens when the only cab in the city goes to ON_TRIP is already covered in "ON_TRIP cab cannot be booked"]
- ON_TRIP to IDLE makes cab available for booking: Attempt to book a cab in city-1. cab-x is booked and is ON_TRIP. Change state of the cab to IDLE with location. Attempt to book a cab in city-1, books cab-x.
- Changing location, makes cab available in another city: city-2 is onboarded. Book cab from city-2 fails with CabNotAvailableException. Change location of the cab to city-2, then book cab from city-2 books cab-x.
- Attempt to change location to non-onboarded city fails with CityNotOnboardedException.

# Keep record of cab history of each cab. (A cab history could just be a record of what all states a cab has gone through)
## Test Fixture: city-1 onboarded. cab-x registered at city-1.
- Registration of cab-x at city-1 is recorded in cab history: Cab history of cab-x has one entry with state IDLE and location city-1.
- Change state in state is recorded in cab history: cab-x state is changed to ON_TRIP. Cab history of cab-x has two entries with state IDLE and location city-1, and state ON_TRIP and location INDETERMINATE.
- Change location in state is recorded in cab history: cab-x state is changed to ON_TRIP. Change state of cab-x to IDLE with city-1. Cab history of cab-x has three entries with state IDLE and location city-1, state ON_TRIP and location INDETERMINATE, and state IDLE and location city-1.
- System captures chronology of cab history using timestamp: assert timestamp in above test

# Find out which cab has remained idle the most and assign it.
## Test Fixture: city-1 onboarded. cab-x registered at city-1.
- track IDLE time of a Cab: cab-x state is changed to ON_TRIP. Change state of cab-x to IDLE with city-1. Now cab is IDLE in city-1. cab-x IDLE time is 0.
- track IDLE time of a Cab: cab-x state is changed to ON_TRIP. Change state of cab-x to IDLE with city-1. Now cab is IDLE in city-1. cab-x IDLE time is 0. After a minute IDLE time is 1 minute. After two minutes, IDLE time is two minutes.
- IDLE time of ON_TRIP cab: cab-x state is changed to ON_TRIP. cab-x IDLE time is -1.
- cab-y registered at city-1. cab-x and cab-y are ON_TRIP. cab-x state is changed to IDLE with city-1. After a minute cab-y state is changed to IDLE with city-1. cab-x IDLE time is 1 minute. cab-y IDLE time is 0.
  - Extend above test: Booking cab in city-1 books cab-y. 
- cab-y registered at city-1. cab-x and cab-y are ON_TRIP. both cabs state changed to IDLE with city-1 at the same time. cab-x and cab-y IDLE time is 0.
  Booking cab in city-1 books cab-x or cab-y randomly.

# Refactoring
- Cab current location and state are variable. They collectively form a state of the cab. So, they should be encapsulated in a class.