
<?php

/*
 * Following code will validate a users given credentials
 */


include 'db_connect.php';

// check for post data
if (isset($_POST['username'])&& isset($_POST['password'])) 
{
    $username = $_POST['username'];
	$password = $_POST['password'];
	$hashed = md5($password);
	$sql = "SELECT * FROM `JunkAway Users` WHERE `E-mail` = '$username' and `Password` = '$password'";
 $stmt = $conn->prepare($sql);
          $stmt->bindParam(':username', $username, PDO::PARAM_STR);
          $stmt->bindParam(':password', $password, PDO::PARAM_STR);
          $stmt->execute();
          if($stmt->rowCount())
          {
			echo $stmt->fetchColumn(0);
			echo ":";
			 
          }  
          elseif(!$stmt->rowCount())
          {
			  	$result="false";
          }
		  $stmt1 = $conn->prepare($sql);
          $stmt1->bindParam(':username', $username, PDO::PARAM_STR);
          $stmt1->bindParam(':password', $password, PDO::PARAM_STR);
          $stmt1->execute();
          if($stmt1->rowCount())
          {
			echo $stmt1->fetchColumn(1);
			echo ":";
			 
          }  
          elseif(!$stmt1->rowCount())
          {
			  	$result="false";
          }
		  $stmt2 = $conn->prepare($sql);
          $stmt2->bindParam(':username', $username, PDO::PARAM_STR);
          $stmt2->bindParam(':password', $password, PDO::PARAM_STR);
          $stmt2->execute();
          if($stmt2->rowCount())
          {
			echo $stmt2->fetchColumn(4);
			 
          }  
          elseif(!$stmt2->rowCount())
          {
			  	$result="false";
          }
		  // send result back to android
   		  echo $result;
  	}

?>