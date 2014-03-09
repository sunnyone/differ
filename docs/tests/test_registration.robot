*** Settings ***
Library  Selenium2Library
Library  Dialogs

Test Setup  Open browser   http://differ.nkp.cz   firefox   timeout=10s
Test Teardown  Close all browsers

*** Keywords ***

Input User Name
    [Arguments]    ${username}
    Input Text          css=.v-window-contents > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > input:nth-child(1)  ${username}

Click on Button Register
    Click Element       css=.v-window-contents > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1) > span:nth-child(1)    

Input Login Username       
    [Arguments]    ${username}
    Input Text     css=.v-window-contents > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > input:nth-child(1)  ${username}

Input Login Password
    [Arguments]    ${password}
    Input Text     css=.v-window-contents > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2) > input:nth-child(1)   ${password}

Click on Button Login
    Click Element     css=.v-window-contents > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > span:nth-child(1) > span:nth-child(1)
    
*** Test Cases ***

Registration of an user
    Wait Until Page Contains   You are not logged in.
    Capture Page Screenshot    home-page.png
    Sleep   2s
    Click Element              xpath=//span[text()="Register"]
    Wait Until Page Contains   Register User    
    Input User Name            testuser03
    Input Text                 css=input[type="password"]  atE87cuv
    Click on Button Register       
    Sleep    2s
        
Login into an application
    Wait Until Page Contains   You are not logged in.
    Sleep    2s
    Click Element              xpath=//span[text()="Login"]
    Input Login Username       testuser01    
    Input Login Password       atE87cuv
    Capture Page Screenshot    login.png
    Click on Button Login
    Wait Until Page Contains   You are now logged in as
    Sleep   1s
    Capture Page Screenshot    login-succeed.png
