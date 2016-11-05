---
layout: post
title: Adding Javascript to a Blog Post
---
So I want to be able to add examples of Javascript code to my blog without posting them to JSFiddle or something.  I'd like the Javascript to be able to insert DOM elements in place without having particular HTML IDs.  That's so that if I change Drupal themes in the future and there shouldn't be a conflict.

``` javascript
//Get the current script tag
var scriptTag = document.currentScript || (function(){
    document.getElementsByTagName('script');
    return scriptTag[scriptTag.length - 1];
    })();
//Get the parent element
var parentTag = scriptTag.parentNode;
//Create a new element
var ele = document.createElement('p');
ele.textContent = 'Hello World';
//Append the element into the DOM
parentTag.appendChild(ele);
```

I found this approach for getting the current script element on [this blog post](http://www.2ality.com/2014/05/current-script.html).

Example Run
------------------

<div>
<script>
var scriptTag = document.currentScript || ((function(){
    document.getElementsByTagName('script');
    return scriptTag[scriptTag.length - 1];
    })());
var parentTag = scriptTag.parentNode;
var ele = document.createElement('p');
ele.textContent = 'Hello World';
parentTag.appendChild(ele);
</script>
</div>
