<?php
$fname=$_POST["FirstName"];
$lname=$_POST["LastName"];
$username=$_POST["Email"];
$password=$_POST["Password"];
$hashed=md5($password);

$conn = new mysqli("mysql.eecs.ku.edu", "cduddy", "Cdud4567!", "cduddy");
/* check connection */
if ($conn->connect_errno) {
    printf("Connect failed: %s\n", $conn->connect_error);
    exit();
}



$sql = "INSERT INTO `JunkAway Users`(`First_Name`,`Last_Name`,`E-mail`,`Password`)VALUES('".$fname."','".$lname."','".$username."','".$hashed."')";
if ($conn->query($sql) === TRUE) {
   $result="true";
   
} else {
     $result="false";
}
echo $result;
 $conn->close();
//mysqli_close($mysqli);
?>