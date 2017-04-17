<?php
include 'db_connect.php';
if (isset($_POST['FirstName'])&& isset($_POST['LastName'])&& isset($_POST['Email'])&& isset($_POST['Phone'])&& isset($_POST['Address'])&& isset($_POST['Vehicle'])) 
{
	$FirstName=$_POST['FirstName'];
	$LastName=$_POST['LastName'];
	$email=$_POST['Email'];
	$phone=$_POST['Phone'];
	$address=$_POST['Address'];
	$vehicle=$_POST['Vehicle'];
	$sql = "INSERT INTO `JunkAway Drivers`(`First_Name`,`Last_Name`,`E-mail`,`Phone_Number`,`Postal_Address`,`Vehicle_Type`)VALUES('".$FirstName."','".$LastName."','".$email."','".$phone."','".$address."','".$vehicle."')";	
			$stmt = $conn->prepare($sql);
				  $stmt->bindParam(':FirstName', $FirstName, PDO::PARAM_STR);
				  $stmt->bindParam(':LastName', $LastName, PDO::PARAM_STR);
				  $stmt->bindParam(':Email', $email, PDO::PARAM_STR);
				  $stmt->bindParam(':Phone', $phone, PDO::PARAM_STR);
				  $stmt->bindParam(':Address', $address, PDO::PARAM_STR);
				  $stmt->bindParam(':Vehicle', $vehicle, PDO::PARAM_STR);
				  $stmt->execute();
          if($stmt->rowCount())
          {
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
