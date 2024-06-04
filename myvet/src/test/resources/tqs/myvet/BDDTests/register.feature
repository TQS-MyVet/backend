Feature: Register a pet owner and their pet

    Scenario: Register a pet owner and their pet
        Given the receptionist is on the login page
        And logs in with valid credentials
        When the receptionist clicks on the Create Account button
        And fills in the pet owner's information and their pet's information
        And clicks on the Create Account button
        Then the pet owner and their pet are registered
