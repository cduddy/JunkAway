<?php

$servername = "mysql.eecs.ku.edu";
$username = "cduddy";
$password = "Cdud4567!";
$dbname = "cduddy";

try {
    	$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }
catch(PDOException $e)
    {
    	die("lolololol");
    }

?>