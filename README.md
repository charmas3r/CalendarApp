# CalendarApp
Simple Calendar App

How to use:
Enter an event on the first screen.
Your end time must be after your start time for the event.

The weekview will display your event.

You can delete your event by long-pressing on it

On rotate your event will be saved to SavedInstanceState outbundle and retrieved onRestoreInstanceState. 

BUGS:
The weekview calendar library being used here has a serious bug which calls the onMonthChange 3 times consecutively. I had to work around this issue and set up eventFlags and implement a hashmap to store events. Even though I implemented the sharedPreferences to save and retreive after the app is closed down, because of the bug in onMonthChange in this library I was unable to get the retreived values into the library. There is a work around which consists of rewriting the on onMonthChange method and sending in Calendar dates. This was beyond the scope of time i think this project required as I already spent 12 hours on this project, trying to get events displayed correctly. 

In retrospect, I should have examined the library more closely before I used it but I did learn alot by doing this assignment. 




