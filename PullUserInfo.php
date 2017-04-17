<?php
include 'db_connect.php';
if (isset($_POST['Email'])) 
{
	$email=$_POST['Email'];
	$sql = "SELECT * FROM `JunkAway Users` WHERE `E-mail` = '$email'";
	$stmt = $conn->prepare($sql1);
        $stmt->bindParam(':Email', $email, PDO::PARAM_STR);
		$stmt->execute();
		if($stmt->rowCount())
          {
			 $result = "true";//$stmt->fetchColumn(0);
          }  
          elseif(!$stmt1->rowCount())
          {

			//$sql = "SELECT * FROM `Google Users` WHERE `E-mail` = '$email'";	
			//$stmt = $conn->prepare($sql);
				//  $stmt->bindParam(':Email', $email, PDO::PARAM_STR);
				 // $stmt->execute();	
				  //$result = $this->$stmt;
				  $result = "false";
          }  
		  
		  // send result back to android
   		  echo $result;
  	}
		else 
	{
		echo "fuuuccckkkk";
	}
?>
