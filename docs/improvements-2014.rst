Extending of application functionality
-----------------------------------------------------------------------------------------------------------



**Result of comparion** in **html** and **pdf**
.....................................................................................

::

    It could be great to have a comparison report in `html`.
    Or in `pdf`. Report can be done using JasperReport.

Significant properties **processing**  *speed-up*
...........................................................................................

::
    
    There are a lot of properties that program must collects and normalizes.
    They are stored in ArrayList structure. It is often necessary to select given properties 
    by its own name.

    Implementing some kind of index by property name must speed up an aplication.
    At least ArrayHash by property name could help.
    
Calling of **external java extractors** **speed-up** 
.................................................................................................

::

    Metadata extractors that are written in java can be called in running JVM 
    calling some class methods. It must speed up an application a lot.

    Student will provide problem description at first.

    At second he find out the slowest part of an application and he
    will sudgest a solution.

    At the end he will implement the solution.

Improvement by Rest API
...........................................

::
    
    The intention is to offer common integration pattern. All
    functions of an application will be available throught REST Api.

    The REST Api should offer managing of user profiles too.


Extending of application web GUI
--------------------------------------------------------------------------------------------


User profile for **important properties**
.............................................................................

::

    A user will choose properties that will be shown. An application
    allows him to save it as profile for later use.

    An application applies a user profile and gives an simple result if
    all items of a profile are the same when comparing two images.


see :ref:`detail description <improvements-exclude-properties>`



Update application to Vaadin 7.x version
............................................................................

::

   The application uses Vaadin 6.x version at the moment. The goal is to
   use Vaadin 7.x version.


Well arranged way to choose two images for comparison
.................................................................................................

::
   
   An application will show thumbnails of uploaded images and an easy
   way to choose two of them to compare.

see :ref:`detail <improvements-easy-selection>`

Required knowledges:

- Java
- JUnit testing
- Spring framework
