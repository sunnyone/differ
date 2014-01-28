*** Settings ***
Library  Selenium2Library
Library  Dialogs

Test Setup  Open browser   http://differ.nkp.cz   firefox   timeout=10s
Test Teardown  Close all browsers

*** Test Cases ***

Registration of an user
    Wait Until Page Contains  Register
    Capture Page Screenshot    home-page.png
    Click Link               Register

