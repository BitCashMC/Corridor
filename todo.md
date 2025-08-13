## Sideboard
For events where there is no clear board condition to predict, what's the most optimal way to deliver a set of boards to check through?

Options:
- <b>Frequent Boards Pool:</b> For boards that get frequently displayed, put those in a list and iterate through them. If none
of the boards are valid, then we can scan the master list.
- <b>Profile Mechanic:</b> When the player is the subject of an ambiguous event (such as joining the server), submit the player and its metadata
to the <code>SideboardHandler</code> for which it will go through a hierarchy
- <b>Stack Based Monitor:</b> Have valid boards get stacked. So if an event gets triggered that results in a board being placed, that board will be
pushed to a stack. Upon a board no longer being valid, it gets popped. 