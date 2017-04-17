<?php
include 'db_connect.php';
if (isset($_POST['FirstName'])&& isset($_POST['LastName'])&& isset($_POST['Email'])&& isset($_POST['Password'])&& isset($_POST['isJunkDriver'])) 
{
	$FirstName=$_POST['FirstName'];
	$LastName=$_POST['LastName'];
	$email=$_POST['Email'];
	$password=$_POST['Password'];
	$junkD=$_POST['isJunkDriver'];
	$hashed=md5($password);
	$sql1 = "SELECT * FROM `JunkAway Users` WHERE `E-mail` = '$email'";
	$stmt1 = $conn->prepare($sql1);
        $stmt1->bindParam(':Email', $email, PDO::PARAM_STR);
		$stmt1->execute();
		if($stmt1->rowCount())
          {
			 $result ="account exists";
          }  
          elseif(!$stmt1->rowCount())
          {

			$sql = "INSERT INTO `JunkAway Users`(`First_Name`,`Last_Name`,`E-mail`,`Password`,`isJunkDriver`)VALUES('".$FirstName."','".$LastName."','".$email."','".$password."','".$junkD."')";	
			$stmt = $conn->prepare($sql);
				  $stmt->bindParam(':FirstName', $FirstName, PDO::PARAM_STR);
				  $stmt->bindParam(':LastName', $LastName, PDO::PARAM_STR);
				  $stmt->bindParam(':Email', $email, PDO::PARAM_STR);
				  $stmt->bindParam(':Password', $password, PDO::PARAM_STR);
				  $stmt->bindParam(':isJunkDriver', $junkD, PDO::PARAM_STR);
				  $stmt->execute();
          
			 $result="true";	
          }  
          else
          {
			 $result="false";
          }
		  
		  // send result back to android
   		  echo $result;
  	}
?>
