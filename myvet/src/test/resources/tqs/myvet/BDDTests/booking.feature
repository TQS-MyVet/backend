Feature: Booking a appointment

    Scenario: Booking a appointment remotely
        Given the pet owner is on the User Interface home page
        And clicks on the Login button
        And enters the email and password
        And clicks on the Login submit button
        And clicks on the Booking button
        And is presented with a calendar of available dates
        When the pet owner selects a date
        And fills the form
        And clicks on the Submit button
        Then the appointment is booked

    Scenario: Booking a appointment locally
        Given the receptionist is on the login page
        And logs in with valid credentials
        And clicks on the Book Appointment button
        And the receptionist is presented with a calendar of available dates
        When the receptionist selects a date
        And the receptionist fills the form
        And the receptionist clicks on the Submit button
        Then the appointment is booked by the receptionist