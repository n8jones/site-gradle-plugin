---
layout: post
title: Simple Maze Example
---
Threw together a simple javascript maze game tonight to practice using the html5 canvas tag.  You can check it out embedded below or at [JSFiddle](http://jsfiddle.net/n8j1s/4y22135r/).  The object of the game is to navigate the blue dot through the maze to the yellow X.  Nothing happens when you win other than your deep sense of accomplishment :-).

In this example I use a 2D array to store the game board.  In the array 1 represents a wall, 0 is free space, and -1 is the goal.  The draw function is called once on load and then is called every time the user presses a key.

*Click in the maze window then use the arrow keys to navigate the maze with the blue dot.*
<iframe width="100%" height="500" src="http://jsfiddle.net/n8j1s/4y22135r/embedded/result,js,html,css/" allowfullscreen="allowfullscreen" frameborder="0"></iframe>

There's a bit of a scrolling issue when embedding the game.  To view without the scrolling issue go to [JSFiddle](http://jsfiddle.net/n8j1s/4y22135r/).
