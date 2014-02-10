*** Settings ***
Library  Selenium2Library
Library  Dialogs

Test Setup  Open browser   http://differ.nkp.cz   firefox   timeout=10s
Test Teardown  Close all browsers

Resource   common-keywords.robot

*** Keywords ***

Submit compare
    Click Element   css=.compare-button-caption
    
*** Test Cases ***

Apply properties profile when comparing two images
    Wait Until Page Contains   You are not logged in.
    Log In
    Select one image
    Select other image    
    See available properties profiles
    Select one profile
    Submit Compare
    Maximize Browser Window
    Sleep  20s
    See Comparision of two images
    See just significant properties listed in the profile
    See True or False result of a profile

Apply properties profile when processing one image
    Wait Until Page Contains   You are not logged in.
    Log In
    Select one image
    See available properties profiles   
    Select one profile
    Submit Proceed
    Maximize Browser Window
    Sleep  20s
    See Processing of an image
    See just significant properties listed in the profile
    See True or False result of a profile

Create properties profile for one image proceed
    Wait Until Page Contains   You are not logged in.
    Log In
    Select one image
    See available properties profiles
    Click on button Create profile
    See properties tree
    Click Validation
    Select a few validation properties
    Click Characterization
    Select a few characterization properties
    Click Identification
    Select a few identification properties
    Click Button Save profile
    Input Profile name
    Submit Button Save
    Select created profile
    Click Button Proceed
    See Analysis of an image


Create properties profile for two images comparision
    Wait Until Page Contains   You are not logged in.
    Log In
    Select two images
    See available properties profiles
    Click on button Create profile
    See properties tree
    Click Validation
    Select a few validation properties
    Click Characterization
    Select a few characterization properties
    Click Identification
    Select a few identification properties
    Click Button Save profile
    Input Profile name
    Submit Button Save
    Select created profile
    Click Button Compare
    See Comparions of the two images