---
layout: post
title: "Eclipse E4 CSS: swt-background-mode"
---
I couldn't find this well documented anywhere, although I did find a few examples.  If you want to set a composite's background mode using the Eclipse E4 CSS you can use the CSS property swt-background-mode like this:

``` css
    #MyCompositesId {
      swt-background-mode: default;
      background-color: gray;
    }
```

According to a [commit message](http://git.eclipse.org/c/platform/eclipse.platform.ui.git/commit/?id=df1c03fce34e53b69b11d512acb76821b5b0f883) in the eclipse git repository, these are the available values.

*  'none' is SWT.INHERIT\_NONE
*  'force' is SWT.INHERIT\_FORCE
*  'default' is SWT.INHERIT\_DEFAULT
