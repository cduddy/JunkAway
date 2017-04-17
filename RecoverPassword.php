<?php

/*
 * Following code will validate a users given credentials
 */


include 'db_connect.php';

// check for post data
if (isset($_POST['FirstName'])&& isset($_POST['LastName'])&& isset($_POST['Email'])) 
{
    $FirstName=$_POST['FirstName'];
	$LastName=$_POST['LastName'];
	$email=$_POST['Email'];
	$sql = "SELECT * FROM `JunkAway Users` WHERE `E-mail` = '$email' and `First_Name` = '$FirstName' and `Last_Name` = '$LastName'";
 $stmt = $conn->prepare($sql);
          $stmt->bindParam(':FirstName', $FirstName, PDO::PARAM_STR);
				  $stmt->bindParam(':LastName', $LastName, PDO::PARAM_STR);
				  $stmt->bindParam(':Email', $email, PDO::PARAM_STR);
          $stmt->execute();
          if($stmt->rowCount())
          {	
			$result = $stmt->fetchColumn(3);
          }  
          elseif(!$stmt->rowCount())
          {
			  	$result="false";
          }
		  
		  // send result back to android
   		  echo $result;
  	}

?>