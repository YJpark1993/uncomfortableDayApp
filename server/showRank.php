<?php
//À¯Áø
 if(isset($_COOKIE['userID'])){

    $loginID = trim($_COOKIE['userID']);
  $conn = mysqli_connect('localhost', 'root', 'djsxhr12');
  mysqli_select_db($conn, 'app');



  $sql = "SELECT user_email, count, score FROM user_info";
  mysqli_query($conn,"SET NAMES 'utf8'");
  $result = mysqli_query($conn,$sql);
  $output = array();
  while($row = mysqli_fetch_assoc($result)){
    array_push($output,
    array('user_email'=>$row['user_email'],'count'=>$row['count'],'score'=>$row['score']));
  }

   echo json_encode(array("output"=>$output));

}


?>
