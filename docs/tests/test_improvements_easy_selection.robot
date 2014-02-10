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

Choosing two images to compare
    Wait Until Page Contains   You are not logged in.
    Log In
    See small thumbnails of uploaded images
    Click on a checkbox at the first thumbnail
    Click on a checkbox at the second thumbnail
    Submit Compare
    Maximize Browser Window
    Sleep  20s
    See Comparision of two images

Choosing one image to proceed
    Wait Until Page Contains   You are not logged in.
    Log In
    See small thumbnails of uploaded images
    Click on a checkbox at the first thumbnail
    Submit Proceed
    Maximize Browser Window
    Sleep  20s
    See Analysis of an image

Remove two images from selection
    Wait Until Page Contains   You are not logged in.
    Log In
    See small thumbnails of uploaded images
    Click on a checkbox at the first thumbnail
    Click on a checkbox at the second thumbnail
    Submit Remove
    The first thumbnail dissappeared   
    The second thumbnail dissappeared

