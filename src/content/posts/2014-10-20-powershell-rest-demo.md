---
layout: post
title: Powershell Rest Demo
---
Have you ever wondered if the number of siblings that a person has makes them more or less likely to be president?  This is a demo I wrote to show usage of REST APIs from Powershell.  This example uses the [Invoke-RestMethod](http://technet.microsoft.com/en-us/library/hh849971.aspx) in order to call the [Freebase API](https://www.freebase.com/).  First we search for all US Presidents.  Then we retrieve the list of their siblings.  The script displays the number of siblings that each president had and then prints the average number of siblings over all of them.

``` powershell
$search = Invoke-RestMethod 'https://www.googleapis.com/freebase/v1/search?filter=(all type:/government/us_president)&amp;limit=100'
$total = 0;
foreach($row in $search.result){
  $siblings = Invoke-RestMethod ('https://www.googleapis.com/freebase/v1/topic{0}?filter=/people/person/sibling_s' -f $row.id)
  $count = $siblings.property.'/people/person/sibling_s'.count;
  Write-Host ('President {0} had {1} siblings' -f $row.name, $count)
  $total += $count
}
$avg = $total / $search.result.count

Write-Host ('Average Number of Siblings = {0:f2}' -f $avg)
```

The results of this script look like this:

```
President Barack Obama had 8.0 siblings
President George W. Bush had 5.0 siblings
President Abraham Lincoln had 2.0 siblings
President Ronald Reagan had 1.0 siblings
President Bill Clinton had 2.0 siblings
President John F. Kennedy had 8.0 siblings
President Franklin D. Roosevelt had 1.0 siblings
President Richard Nixon had 4.0 siblings
President George Washington had 7.0 siblings
President Dwight D. Eisenhower had 3.0 siblings
President Thomas Jefferson had 9.0 siblings
President Theodore Roosevelt had 3.0 siblings
President Lyndon B. Johnson had 4.0 siblings
President Jimmy Carter had 3.0 siblings
President George H. W. Bush had 4.0 siblings
President Harry S. Truman had 0 siblings
President Ulysses S. Grant had 0 siblings
President Woodrow Wilson had 2.0 siblings
President Andrew Jackson had 0 siblings
President Gerald Ford had 6.0 siblings
President John Adams had 2.0 siblings
President Herbert Hoover had 1.0 siblings
President Warren G. Harding had 1.0 siblings
President James Madison had 9.0 siblings
President William Howard Taft had 4.0 siblings
President William McKinley had 8.0 siblings
President John Quincy Adams had 5.0 siblings
President Grover Cleveland had 1.0 siblings
President Calvin Coolidge had 0 siblings
President James A. Garfield had 0 siblings
President Andrew Johnson had 0 siblings
President James Monroe had 0 siblings
President James Buchanan had 0 siblings
President James K. Polk had 1.0 siblings
President Martin Van Buren had 2.0 siblings
President William Henry Harrison had 1.0 siblings
President Chester A. Arthur had 8.0 siblings
President John Tyler had 7.0 siblings
President Franklin Pierce had 0 siblings
President Rutherford B. Hayes had 0 siblings
President Zachary Taylor had 1.0 siblings
President Benjamin Harrison had 0 siblings
President Millard Fillmore had 0 siblings
Average Number of Siblings = 2.86
```

It looks like the number of siblings you have doesn't make a difference whether or not you will become president.
