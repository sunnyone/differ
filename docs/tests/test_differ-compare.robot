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

Compare two files
    Wait Until Page Contains   You are not logged in.
    Choose File        xpath=//input[@name="PID25_file"]      /opt/differ/docs/images/TIFF/1002186430_000015.tif
    Choose File        xpath=//input[@name="PID32_file"]      /opt/differ/docs/images/JP2/1002186430_000015 kvalita 100.jpf
    Sleep  15s
    Capture Page Screenshot    differ-compare-01.png
    Submit compare
    Maximize Browser Window
    Sleep  20s
    Capture Page Screenshot    differ-compare-02.png
