*** Settings ***
Library  Selenium2Library
Library  Dialogs

Test Setup  Open browser   http://differ.nkp.cz   firefox   timeout=10s
Test Teardown  Close all browsers


*** Test Cases ***

Problém s barvením hodnot 
    Pause Execution
    Choose File        xpath=//input[@name="PID25_file"]      /opt/differ/docs/images/TIFF/1002186430_000015.tif
    Choose File        xpath=//input[@name="PID32_file"]      /opt/differ/docs/images/JP2/1002186430_000015 kvalita 100.jpf
    Pause Execution
    Click Element             css=#main.button.compare
    Pause Execution
