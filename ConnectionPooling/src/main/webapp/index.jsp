<%@ page contentType="text/html; charset=UTF-8" %>
<h2>The SQL Gateway</h2>
<p>Enter an SQL statement and click the Execute button.</p>
<form method="post" action="sqlGateway">
  <textarea name="sql" rows="6" cols="70">select * from usertest;</textarea><br/>
  <button type="submit">Execute</button>
</form>
<div>SQL result:</div>
