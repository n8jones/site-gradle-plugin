---
layout: post
title: "Tip: Favor IQueryable Over IEnumerable"
---
Recently I've started using [Microsoft's Entity Framework](http://msdn.microsoft.com/en-us/data/ef.aspx) for data access.  Entity Framework is an awesome ORM and I've been really happy with it.

One gotcha that I've ran into is that in my application I often need to composite a query in multiple steps.  This means that I need to cast the query into a variable in order to store it during the composition.  If I cast my query into an IEnumerable value, then the data will be filtered in memory rather than in SQL.  In almost every case it is preferable to have the filtering done in SQL.

Say for example I have a `DbSet<Node>` called Nodes.  If I compose my query using IEnumerable like this:

``` csharp
public IList<Node> GetNodes(int? skip = null, int? take = null)
{
    IEnumerable<Node> query = Nodes;
    if(skip.HasValue) query = query.Skip(skip.Value);
    if(take.HasValue) query = query.Take(take.Value);
    return query.ToList();
}
```

When this query executes, regardless of the skip and take values it will retrieve all the values from the Nodes table and then filter them in memory.  Obviously this can be a performance problem, especially when the number of rows in the Nodes table gets high.

Using IQueryable will work exactly the same way, but it will allow the filtering to be performed on the SQL side rather than in memory.

``` csharp
public IList<Node> GetNodes(int? skip = null, int? take = null)
{
    IQueryable<Node> query = Nodes;
    if(skip.HasValue) query = query.Skip(skip.Value);
    if(take.HasValue) query = query.Take(take.Value);
    return query.ToList();
}
```

If you call this function using GetNodes(100,100) the SQL for SQL Server will look something like this.

``` sql
DECLARE @p0 Int = 100
DECLARE @p1 Int = 100
SELECT [t1].[Id], [t1].[Value]
FROM (
    SELECT ROW_NUMBER() OVER (ORDER BY [t0].[Id], [t0].[Value]) AS [ROW_NUMBER], [t0].[Id], [t0].[Value]
    FROM [Node] AS [t0]
    ) AS [t1]
WHERE [t1].[ROW_NUMBER] BETWEEN @p0 + 1 AND @p0 + @p1
ORDER BY [t1].[ROW_NUMBER]
```

Conclusion 
==========
Linq and Linq to Sql gives us a lot of power in C#.  But there is a danger here where you could inadvertently be loading a large number of objects into memory without meaning to, since querying an IEnumerable looks the same as querying an IQueryable.  So my recommendation would be to prefer the IQueryable interface where possible, especially when using Linq to Sql.

For more discussion see [this thread on StackOverflow](http://stackoverflow.com/questions/2876616/returning-ienumerablet-vs-iqueryablet).
