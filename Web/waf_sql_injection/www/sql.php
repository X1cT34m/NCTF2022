<html>
<head>
	<meta charset="UTF-8">
	<title>SQL Injection</title>
        <link rel="stylesheet" type="text/css" href="/static/css/normalize.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
        <link rel="stylesheet" type="text/css" href="/static/css/style.css">
	<style>
	*{margin:0;padding:0;}
	body{background-color:#0096dd;}
	#firefox{
		width:156px;
		height:156px;
		margin:100px auto;
		background:url(/static/images/foxtail.png) no-repeat 0 0;
		animation:animate-tail 3.75s steps(44) infinite;
	}
	@keyframes animate-tail{
		0%{background-position:-6864px 0;}
		100%{background-position:0 0;}
	}
	</style>
</head>
<body>
<div class="container">
<h1>Try to bypass WAF</h1>
<h3>My query statement is:</h3>
<h3>$select = "select username from users.info where id = ". $id;</h3>
<h3>Can you find my password?</h3>
<div class="button-effect">
<?php
$username = 'root';
$password = 'sltyyds12345';
$dbname = 'users';
$con = new mysqli("modsecurity_mysql:3306",$username,$password,$dbname);
if ($con->connect_error) {
    die("Connection Error: " . $con->connect_error);
}
$id = $_GET['id'];
$select = "select username from users.info where id = ". $id;
$result = $con->query($select);
if(!$result){
	print_r($con->error);
}
else{
	while($row = $result->fetch_assoc()) {
    echo "<h2>Username: ". $row["username"] . "</h2>";
}
}
?>
</div>
</div>
<div style="text-align: center">
<a href="index.php">Back to HomePage</a>
</div>
<div id="firefox"></div>
</body>
</html>
