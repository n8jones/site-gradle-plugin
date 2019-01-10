---
layout: post
title: Creating A Sortable Form Element
---
Many times it is useful to have a set of options in a form that can be sorted by the user in a drag and drop fashion.  Whenever possible I like to use standard form elements to report data to the server.  So one way I have found to provide a user with a sortable form element is by using javascript to update text form elements with the relative weights of the list.

Here is an example of what the final product might look like:

<iframe width="100%" height="300" src="http://jsfiddle.net/n8j1s/E4PeR/embedded/result/" allowfullscreen="allowfullscreen" frameborder="0"></iframe>

In production you would probably want to hide the weight column, but this shows how you can make a very simple sortable form element using jQuery UI.  The benefit of this approach is that the weights of the elements will get posted with the rest of the form values if you include this in a form for submission.  Here is the javascript that is the heart of this approach.

``` javascript
//There is a table with id=Sortable, a column with class=handle, and a text field with class=weight.
$('#Sortable tbody').sortable({
    handle: &quot;.handle&quot;,
    stop: function(event, ui) {
        $('#Sortable tbody&gt;tr').each(function(index){
            $(this).find('.weight').val(index);
        });
    }
});
```

We're utilizing the [sortable](http://jqueryui.com/sortable/) plugin in jQuery UI to allow the user to move the elements.  Then we're using the index of the table rows to fill the weight text fields.
