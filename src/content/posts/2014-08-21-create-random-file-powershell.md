---
layout: post
title: Create A Random File Using Powershell
---
I recently needed to create a file for testing the upload capabilities on a website.  The content of the file didn't matter, only the size.  So I wrote this little powershell script to dump a random series of bytes to disk.

``` powershell
#Create the array of the size you want
$out = New-Object Byte[] 1024
#Fill the array using a System.Random object
(New-Object Random).NextBytes($out)
#Write the array to a file with System.IO.File
[IO.File]::WriteAllBytes('filename', $out)
```
