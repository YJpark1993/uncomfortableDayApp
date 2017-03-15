<?php
//유진
 if(isset($_COOKIE['userID'])){

    $loginID = trim($_COOKIE['userID']);
  $conn = mysqli_connect('localhost', 'root', 'djsxhr12');
  mysqli_select_db($conn, 'app');



  $sql = "SELECT context_key_road, latitude, longitude, created, created_date FROM contextRoad_info WHERE user_email='".$loginID."'";
  mysqli_query($conn,"SET NAMES 'utf8'");
  $result = mysqli_query($conn,$sql);
  $output = array();
  while($row = mysqli_fetch_assoc($result)){
    array_push($output,
    array('context_key_road'=>$row['context_key_road'], 'latitude'=>$row['latitude'], 'longitude'=>$row['longitude'],'created'=>$row['created'],'created_date'=>$row['created_date']));
  }

   echo json_encode(array("output"=>$output));

}


?>
