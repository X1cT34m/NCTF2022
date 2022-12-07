<?php
$username = 'root';
$password = 'sltyyds12345';
$dbname = 'users';
$con = new mysqli("modsecurity_mysql:3306",$username,$password,$dbname);
if ($con->connect_error) {
    die("Connection Error: " . $con->connect_error);
}
?>
